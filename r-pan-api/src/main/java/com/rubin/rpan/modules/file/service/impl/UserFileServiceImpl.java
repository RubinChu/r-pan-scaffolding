package com.rubin.rpan.modules.file.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.rubin.rpan.common.constant.CommonConstant;
import com.rubin.rpan.common.exception.RPanException;
import com.rubin.rpan.common.response.ResponseCode;
import com.rubin.rpan.common.util.FileUtil;
import com.rubin.rpan.common.util.HttpUtil;
import com.rubin.rpan.common.util.UUIDUtil;
import com.rubin.rpan.common.util.file.type.context.FileTypeContext;
import com.rubin.rpan.modules.file.bo.RPanFileDownloadBO;
import com.rubin.rpan.modules.file.bo.RPanFilePreviewBO;
import com.rubin.rpan.modules.file.constant.FileConstant;
import com.rubin.rpan.modules.file.dao.RPanUserFileMapper;
import com.rubin.rpan.modules.file.entity.RPanFile;
import com.rubin.rpan.modules.file.entity.RPanUserFile;
import com.rubin.rpan.modules.file.service.IFileService;
import com.rubin.rpan.modules.file.service.IUserFileService;
import com.rubin.rpan.modules.file.vo.BreadcrumbVO;
import com.rubin.rpan.modules.file.vo.FolderTreeNode;
import com.rubin.rpan.modules.file.vo.RPanUserFileVO;
import com.rubin.rpan.modules.share.constant.ShareConstant;
import com.rubin.rpan.modules.share.service.IShareService;
import com.rubin.rpan.modules.user.service.IUserSearchHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户文件业务处理实现
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@Service(value = "userFileService")
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class UserFileServiceImpl implements IUserFileService {

    /**
     * 单位长度
     */
    private static final Integer UNIT_INT = 1024;

    @Autowired
    @Qualifier(value = "rPanUserFileMapper")
    private RPanUserFileMapper rPanUserFileMapper;

    @Autowired
    @Qualifier(value = "fileService")
    private IFileService iFileService;

    @Autowired
    @Qualifier(value = "userSearchHistoryService")
    private IUserSearchHistoryService iUserSearchHistoryService;

    @Autowired
    @Qualifier(value = "shareService")
    private IShareService iShareService;

    @Autowired
    @Qualifier(value = "fileTypeContext")
    private FileTypeContext fileTypeContext;

    /**
     * 获取文件列表
     *
     * @param parentId
     * @param fileTypes
     * @param userId
     * @return
     */
    @Override
    public List<RPanUserFileVO> list(String parentId, String fileTypes, String userId) {
        return list(parentId, fileTypes, userId, FileConstant.DelFlagEnum.NO.getCode());
    }

    /**
     * 获取文件列表
     *
     * @param parentId
     * @param fileTypes
     * @param userId
     * @param delFlag
     * @return
     */
    @Override
    public List<RPanUserFileVO> list(String parentId, String fileTypes, String userId, Integer delFlag) {
        List<String> fileTypeArray = null;
        if (Objects.equals(CommonConstant.TOP_STR, parentId)) {
            return Lists.newArrayList();
        }
        if (!Objects.equals(fileTypes, FileConstant.ALL_FILE_TYPE)) {
            fileTypeArray = Splitter.on(CommonConstant.COMMON_SEPARATOR).splitToList(fileTypes);
        }
        return rPanUserFileMapper.selectByUserIdAndFileTypeAndParentIdAndDelFlag(userId, fileTypeArray, parentId, delFlag);
    }

    /**
     * 获取文件列表
     *
     * @param fileIds
     * @return
     */
    @Override
    public List<RPanUserFileVO> list(String fileIds) {
        return rPanUserFileMapper.selectRPanUserFileVOListByFileIdList(Splitter.on(CommonConstant.COMMON_SEPARATOR).splitToList(fileIds));
    }

    /**
     * 创建文件夹
     *
     * @param parentId
     * @param folderName
     * @param userId
     * @return
     */
    @Override
    public List<RPanUserFileVO> createFolder(String parentId, String folderName, String userId) {
        saveUserFile(parentId, folderName, FileConstant.FolderFlagEnum.YES, null, null, userId);
        return list(parentId, FileConstant.ALL_FILE_TYPE, userId, FileConstant.DelFlagEnum.NO.getCode());
    }

    /**
     * 文件重命名
     *
     * @param fileId
     * @param newFilename
     * @param userId
     * @return
     */
    @Override
    public List<RPanUserFileVO> updateFilename(String fileId, String newFilename, String userId) {
        RPanUserFile originalUserFileInfo = rPanUserFileMapper.selectByFileIdAndUserId(fileId, userId);
        if (Objects.isNull(originalUserFileInfo)) {
            throw new RPanException("该用户没有此文件");
        }
        if (Objects.equals(originalUserFileInfo.getFilename(), newFilename)) {
            throw new RPanException("请使用一个新名称");
        }
        if (rPanUserFileMapper.selectCountByUserIdAndFilenameAndParentId(userId, newFilename, originalUserFileInfo.getParentId()) > CommonConstant.ZERO_INT) {
            throw new RPanException("名称已被占用");
        }
        originalUserFileInfo.setFilename(newFilename);
        if (rPanUserFileMapper.updateByPrimaryKeySelective(originalUserFileInfo) != CommonConstant.ONE_INT) {
            throw new RPanException("文件重命名失败");
        }
        return list(originalUserFileInfo.getParentId(), FileConstant.ALL_FILE_TYPE, userId);
    }

    /**
     * 删除文件(批量)
     *
     * @param parentId
     * @param fileIds
     * @param userId
     * @return
     */
    @Override
    public List<RPanUserFileVO> delete(String parentId, String fileIds, String userId) {
        List<String> idList = Splitter.on(CommonConstant.COMMON_SEPARATOR).splitToList(fileIds);
        if (rPanUserFileMapper.deleteBatch(idList, userId) != idList.size()) {
            throw new RPanException("删除失败");
        }
        iShareService.changeShareStatus(fileIds, ShareConstant.ShareStatus.FILE_DELETED);
        return list(parentId, FileConstant.ALL_FILE_TYPE, userId);
    }

    /**
     * 文件上传
     *
     * @param file
     * @param parentId
     * @return
     */
    @Override
    public List<RPanUserFileVO> upload(MultipartFile file, String parentId, String userId) {
        RPanFile rPanFile = uploadRealFile(file, userId);
        String filename = file.getOriginalFilename();
        saveUserFile(parentId, filename, FileConstant.FolderFlagEnum.NO, fileTypeContext.getFileTypeCode(filename), rPanFile.getFileId(), userId);
        return list(parentId, FileConstant.ALL_FILE_TYPE, userId);
    }

    /**
     * 文件下载
     *
     * @param fileId
     * @param response
     * @param userId
     */
    @Override
    public void download(String fileId, HttpServletResponse response, String userId) {
        if (Objects.isNull(rPanUserFileMapper.selectByFileIdAndUserId(fileId, userId))) {
            throw new RPanException("您没有下载权限");
        }
        download(fileId, response);
    }

    /**
     * 文件下载
     *
     * @param fileId
     * @param response
     */
    @Override
    public void download(String fileId, HttpServletResponse response) {
        if (checkIsFolder(fileId)) {
            throw new RPanException("不能选择文件夹下载");
        }
        RPanFileDownloadBO rPanFileDownloadBO = rPanUserFileMapper.selectRPanFileDownloadBOByFileId(fileId);
        if (Objects.isNull(rPanFileDownloadBO)) {
            throw new RPanException("文件不存在");
        }
        doDownload(new File(rPanFileDownloadBO.getRealPath()), response, rPanFileDownloadBO.getFilename());
    }

    /**
     * 获取文件夹树
     *
     * @param userId
     * @return
     */
    @Override
    public List<FolderTreeNode> getFolderTree(String userId) {
        List<RPanUserFile> folderList = rPanUserFileMapper.selectFolderListByUserId(userId);
        return assembleFolderTree(folderList);
    }

    /**
     * 转移文件(批量)
     *
     * @param fileIds
     * @param parentId
     * @param targetParentId
     * @param userId
     * @return
     */
    @Override
    public List<RPanUserFileVO> transfer(String fileIds, String parentId, String targetParentId, String userId) {
        if (!checkIsFolder(targetParentId)) {
            throw new RPanException("请选择要转移到的文件夹");
        }
        // 查询所有要被转移的文件信息
        List<RPanUserFile> toBeTransferredFileInfoList = rPanUserFileMapper.selectListByFileIdList(Splitter.on(CommonConstant.COMMON_SEPARATOR).splitToList(fileIds));
        toBeTransferredFileInfoList.stream().forEach(rPanUserFile -> transferOne(rPanUserFile, targetParentId));
        return list(parentId, FileConstant.ALL_FILE_TYPE, userId);
    }

    /**
     * 批量复制文件
     *
     * @param fileIds
     * @param parentId
     * @param targetParentId
     * @param userId
     * @return
     */
    @Override
    public List<RPanUserFileVO> copy(String fileIds, String parentId, String targetParentId, String userId) {
        if (fileIds.indexOf(targetParentId) != CommonConstant.MINUS_ONE_INT) {
            throw new RPanException("要复制的文件中包含选中的目标文件夹,请重新选择");
        }
        if (!checkIsFolder(targetParentId)) {
            throw new RPanException("请选择要转移到的文件夹");
        }
        doCopyUserFiles(fileIds, targetParentId, userId);
        return list(parentId, FileConstant.ALL_FILE_TYPE, userId);
    }

    /**
     * 通过文件名搜索文件
     *
     * @param keyword
     * @param userId
     * @return
     */
    @Override
    public List<RPanUserFileVO> search(String keyword, String fileTypes, String userId) {
        saveUserSearchHistory(keyword, userId);
        List<String> fileTypeArray = null;
        if (!Objects.equals(fileTypes, FileConstant.ALL_FILE_TYPE)) {
            fileTypeArray = Splitter.on(CommonConstant.COMMON_SEPARATOR).splitToList(fileTypes);
        }
        return rPanUserFileMapper.selectRPanUserFileVOListByUserIdAndFilenameAndFileTypes(userId, keyword, fileTypeArray);
    }

    /**
     * 查询文件详情
     *
     * @param fileId
     * @param userId
     * @return
     */
    @Override
    public RPanUserFileVO detail(String fileId, String userId) {
        return rPanUserFileMapper.selectRPanUserFileVOByFileIdAndUserId(fileId, userId);
    }


    /**
     * 获取面包屑列表
     *
     * @param fileId
     * @param userId
     * @return
     */
    @Override
    public List<BreadcrumbVO> getBreadcrumbs(String fileId, String userId) {
        List<RPanUserFile> rPanUserFileList = rPanUserFileMapper.selectFolderListByUserId(userId);
        if (CollectionUtils.isEmpty(rPanUserFileList)) {
            return Lists.newArrayList();
        }
        Map<String, BreadcrumbVO> breadcrumbVOTransferMap = rPanUserFileList.stream().collect(Collectors.toMap(RPanUserFile::getFileId, BreadcrumbVO::assembleBreadcrumbVO));
        List<BreadcrumbVO> breadcrumbVOList = Lists.newArrayList();
        BreadcrumbVO thisLevel = breadcrumbVOTransferMap.get(fileId);
        do {
            breadcrumbVOList.add(thisLevel);
            thisLevel = breadcrumbVOTransferMap.get(thisLevel.getParentId());
        } while (!Objects.isNull(thisLevel));
        Collections.reverse(breadcrumbVOList);
        return breadcrumbVOList;
    }

    /**
     * 预览单个文件
     *
     * @param fileId
     * @param userId
     * @param response
     */
    @Override
    public void preview(String fileId, HttpServletResponse response, String userId) {
        if (checkIsFolder(fileId, userId)) {
            throw new RPanException("不能预览文件夹");
        }
        RPanFilePreviewBO rPanFilePreviewBO = rPanUserFileMapper.selectRPanFilePreviewBOByFileId(fileId);
        if (Objects.isNull(rPanFilePreviewBO)) {
            throw new RPanException("文件不存在");
        }
        preview(new File(rPanFilePreviewBO.getRealPath()), response, rPanFilePreviewBO.getFilePreviewContentType());
    }

    /**
     * 批量还原用户文件的删除状态
     *
     * @param fileIds
     * @param userId
     * @return
     */
    @Override
    public void restoreUserFiles(String fileIds, String userId) {
        if (StringUtils.isAnyBlank(fileIds, userId)) {
            throw new RPanException("批量还原用户文件的删除状态失败");
        }
        List<String> fileIdList = Splitter.on(CommonConstant.COMMON_SEPARATOR).splitToList(fileIds);
        List<RPanUserFile> rPanUserFiles = rPanUserFileMapper.selectListByFileIdList(fileIdList);
        Set<String> cleanSet = rPanUserFiles.stream().map(rPanUserFile -> rPanUserFile.getParentId() + rPanUserFile.getFilename()).collect(Collectors.toSet());
        if (cleanSet.size() != rPanUserFiles.size()) {
            throw new RPanException("文件还原失败，该还原文件中存在同名文件，请逐个还原并重命名");
        }
        for (RPanUserFile rPanUserFile : rPanUserFiles) {
            if (rPanUserFileMapper.selectCountByUserIdAndFilenameAndParentId(rPanUserFile.getUserId(), rPanUserFile.getFilename(), rPanUserFile.getParentId()) != CommonConstant.ZERO_INT) {
                throw new RPanException("文件：" + rPanUserFile.getFilename() + "还原失败，该文件夹中已有相同名称的文件或文件夹，请重命名后重试");
            }
        }
        if (rPanUserFileMapper.updateUserFileDelFlagByFileIdListAndUserId(fileIdList, userId) != fileIdList.size()) {
            throw new RPanException("批量还原用户文件的删除状态失败");
        }
    }

    /**
     * 物理删除用户文件
     *
     * @param fileIds
     * @param userId
     * @return
     */
    @Override
    public void physicalDeleteUserFiles(String fileIds, String userId) {
        if (StringUtils.isAnyBlank(fileIds, userId)) {
            throw new RPanException("物理删除用户文件失败");
        }
        List<RPanUserFile> rPanUserFileList = assembleAllToBeDeletedUserFileList(fileIds);
        List<String> fileIdList = rPanUserFileList.stream().map(RPanUserFile::getFileId).collect(Collectors.toList());
        if (rPanUserFileMapper.physicalDeleteBatch(fileIdList, userId) != fileIdList.size()) {
            throw new RPanException("物理删除用户文件失败");
        }
        Set<String> realFileIdSet = assembleAllUnusedRealFileIdSet(rPanUserFileList);
        if (CollectionUtils.isEmpty(realFileIdSet)) {
            return;
        }
        iFileService.delete(StringUtils.join(realFileIdSet, CommonConstant.COMMON_SEPARATOR));
    }

    /**
     * 批量保存文件
     *
     * @param fileIds
     * @param targetParentId
     * @param userId
     * @return
     */
    @Override
    public void saveBatch(String fileIds, String targetParentId, String userId) {
        doCopyUserFiles(fileIds, targetParentId, userId);
    }

    /**
     * 获取对应文件列表的所有文件以及自文件信息
     *
     * @param fileIds
     * @return
     */
    @Override
    public List<RPanUserFileVO> allList(String fileIds) {
        if (StringUtils.isBlank(fileIds)) {
            throw new RPanException(ResponseCode.ERROR_PARAM);
        }
        List<RPanUserFileVO> rPanUserFileVOList = rPanUserFileMapper.selectRPanUserFileVOListByFileIdList(Splitter.on(CommonConstant.COMMON_SEPARATOR).splitToList(fileIds));
        if (CollectionUtils.isEmpty(rPanUserFileVOList)) {
            return Lists.newArrayList();
        }
        final List<RPanUserFileVO> allRPanUserFileVOList = Lists.newArrayList(rPanUserFileVOList);
        rPanUserFileVOList.stream().forEach(rPanUserFileVO -> findAllAvailableChildUserFile(allRPanUserFileVOList, rPanUserFileVO));
        return allRPanUserFileVOList;
    }

    /******************************************************私有****************************************************/

    /**
     * 拼装用户文件实体信息
     *
     * @param parentId
     * @param userId
     * @param filename
     * @param folderFlag
     * @param fileType
     * @param realFileId
     * @return
     */
    private RPanUserFile assembleRPanUserFile(String parentId, String userId, String filename, FileConstant.FolderFlagEnum folderFlag, Integer fileType, String realFileId) {
        RPanUserFile rPanUserFile = new RPanUserFile();
        rPanUserFile.setUserId(userId)
                .setParentId(parentId)
                .setFileId(UUIDUtil.getUUID())
                .setFilename(filename)
                .setType(fileType)
                .setFolderFlag(folderFlag.getCode())
                .setRealFileId(realFileId)
                .setCreateUser(userId)
                .setCreateTime(new Date())
                .setUpdateUser(userId)
                .setUpdateTime(new Date());
        handleDuplicateFileName(rPanUserFile);
        return rPanUserFile;
    }

    /**
     * 保存用户文件信息
     *
     * @param parentId
     * @param filename
     * @param folderFlag
     * @param fileType
     * @param realFileId
     * @return
     */
    private void saveUserFile(String parentId, String filename, FileConstant.FolderFlagEnum folderFlag, Integer fileType, String realFileId, String userId) {
        if (rPanUserFileMapper.insertSelective(assembleRPanUserFile(parentId, userId, filename, folderFlag, fileType, realFileId)) != CommonConstant.ONE_INT) {
            throw new RPanException("保存文件信息失败");
        }
    }

    /**
     * 处理重复文件名
     *
     * @param rPanUserFile
     */
    private void handleDuplicateFileName(RPanUserFile rPanUserFile) {
        String newFileName = rPanUserFile.getFilename(),
                newFileNameWithoutSuffix,
                newFileNameSuffix;
        int newFileNamePointPosition = newFileName.lastIndexOf(CommonConstant.POINT_STR);
        if (newFileNamePointPosition == CommonConstant.MINUS_ONE_INT) {
            newFileNameWithoutSuffix = newFileName;
            newFileNameSuffix = CommonConstant.EMPTY_STR;
        } else {
            newFileNameWithoutSuffix = newFileName.substring(CommonConstant.ZERO_INT, newFileNamePointPosition);
            newFileNameSuffix = FileUtil.getFileSuffix(newFileName);
        }
        List<RPanUserFileVO> rPanUserFileVOList = rPanUserFileMapper.selectByUserIdAndFileTypeAndParentIdAndDelFlag(rPanUserFile.getUserId(), null, rPanUserFile.getParentId(), FileConstant.DelFlagEnum.NO.getCode());
        boolean noDuplicateFileNameFlag = rPanUserFileVOList.stream().noneMatch(rPanUserFileVO -> rPanUserFile.getFilename().equals(rPanUserFileVO.getFilename()));
        if (noDuplicateFileNameFlag) {
            return;
        }
        List<String> duplicateFileNameList = rPanUserFileVOList.stream()
                .map(RPanUserFileVO::getFilename)
                .filter(fileName -> fileName.startsWith(newFileNameWithoutSuffix))
                .filter(fileName -> {
                    int pointPosition = fileName.lastIndexOf(CommonConstant.POINT_STR);
                    String fileNameSuffix = CommonConstant.EMPTY_STR;
                    if (pointPosition != CommonConstant.MINUS_ONE_INT) {
                        fileNameSuffix = FileUtil.getFileSuffix(fileName);
                    }
                    return Objects.equals(newFileNameSuffix, fileNameSuffix);
                })
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(duplicateFileNameList)) {
            return;
        }
        newFileName = new StringBuilder(newFileNameWithoutSuffix)
                .append(FileConstant.CN_LEFT_PARENTHESES_STR)
                .append((duplicateFileNameList.size()))
                .append(FileConstant.CN_RIGHT_PARENTHESES_STR)
                .append(newFileNameSuffix)
                .toString();
        rPanUserFile.setFilename(newFileName);
    }

    /**
     * 执行下载文件
     *
     * @param file
     * @param response
     * @param filename
     */
    private void doDownload(File file, HttpServletResponse response, String filename) {
        addCommonResponseHeader(response, FileConstant.APPLICATION_OCTET_STREAM_STR);
        try {
            response.setHeader(FileConstant.CONTENT_DISPOSITION_STR, FileConstant.CONTENT_DISPOSITION_VALUE_PREFIX_STR + new String(filename.getBytes(FileConstant.GB2312_STR), FileConstant.IOS_8859_1_STR));
        } catch (UnsupportedEncodingException e) {
            log.error("下载文件失败", e);
        }
        writeFile2Response(file, response);
    }

    /**
     * 补全要复制的文件列表
     *
     * @param toBeCopiedFileInfoList
     * @param targetParentId
     * @param userId
     */
    private void complementToBeCopiedFileInfoList(final List<RPanUserFile> toBeCopiedFileInfoList, final String targetParentId, final String userId) {
        final List<RPanUserFile> allChildUserFileList = Lists.newArrayList();
        toBeCopiedFileInfoList.stream().forEach(rPanUserFile -> {
            String fileId = rPanUserFile.getFileId(),
                    newFileId = UUIDUtil.getUUID();
            rPanUserFile.setParentId(targetParentId)
                    .setId(null)
                    .setUserId(userId)
                    .setFileId(newFileId)
                    .setCreateUser(userId)
                    .setCreateTime(new Date())
                    .setUpdateUser(userId)
                    .setUpdateTime(new Date());
            handleDuplicateFileName(rPanUserFile);
            if (checkIsFolder(rPanUserFile)) {
                assembleAllChildUserFile(allChildUserFileList, fileId, newFileId, userId);
            }
        });
        toBeCopiedFileInfoList.addAll(allChildUserFileList);
    }

    /**
     * 文件预览
     *
     * @param file
     * @param response
     */
    private void preview(File file, HttpServletResponse response, String filePreviewContentType) {
        addCommonResponseHeader(response, filePreviewContentType);
        writeFile2Response(file, response);
    }

    /**
     * 添加公用的响应头
     *
     * @param response
     * @param contentTypeValue
     */
    private void addCommonResponseHeader(HttpServletResponse response, String contentTypeValue) {
        response.reset();
        HttpUtil.addCorsResponseHeader(response);
        response.setHeader(FileConstant.CONTENT_TYPE_STR, contentTypeValue);
        response.setContentType(contentTypeValue);
    }

    /**
     * 文件写入响应实体
     *
     * @param file
     * @param response
     */
    private void writeFile2Response(File file, HttpServletResponse response) {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
            byte[] buffer = new byte[UNIT_INT];
            OutputStream os = response.getOutputStream();
            int i = bis.read(buffer);
            while (i != CommonConstant.MINUS_ONE_INT) {
                os.write(buffer, CommonConstant.ZERO_INT, i);
                i = bis.read(buffer);
            }
        } catch (Exception e) {
            log.error("文件写入响应实体失败", e);
        }
    }

    /**
     * 转移一个文件
     *
     * @param rPanUserFile
     * @param targetParentId
     */
    private void transferOne(RPanUserFile rPanUserFile, String targetParentId) {
        if (targetParentId.equals(rPanUserFile.getFileId()) || targetParentId.equals(rPanUserFile.getParentId())) {
            return;
        }
        rPanUserFile.setParentId(targetParentId)
                .setUpdateUser(rPanUserFile.getCreateUser())
                .setUpdateTime(new Date());
        handleDuplicateFileName(rPanUserFile);
        // 修改文件信息
        if (rPanUserFileMapper.updateByPrimaryKeySelective(rPanUserFile) != CommonConstant.ONE_INT) {
            log.error("文件转移失败,文件名称为:{}", rPanUserFile.getFilename());
            throw new RPanException("文件转移失败");
        }
    }

    /**
     * 上传真实文件
     *
     * @param file
     * @param userId
     * @return
     */
    private RPanFile uploadRealFile(MultipartFile file, String userId) {
        return iFileService.save(file, userId);
    }

    /**
     * 拼装文件夹树
     *
     * @param folderList
     * @return
     */
    private List<FolderTreeNode> assembleFolderTree(List<RPanUserFile> folderList) {
        if (CollectionUtils.isEmpty(folderList)) {
            return Lists.newArrayList();
        }
        List<FolderTreeNode> folderTreeNodeList = folderList.stream().map(FolderTreeNode::assembleFolderTreeNode).collect(Collectors.toList());
        Map<String, List<FolderTreeNode>> directoryTreeNodeParentGroup = folderTreeNodeList.stream().collect(Collectors.groupingBy(FolderTreeNode::getParentId));
        folderTreeNodeList.stream().forEach(node -> {
            List<FolderTreeNode> children = directoryTreeNodeParentGroup.get(node.getId());
            if (!CollectionUtils.isEmpty(children)) {
                node.setChildren(children);
            }
        });
        return folderTreeNodeList.stream().filter(node -> Objects.equals(CommonConstant.TOP_STR, node.getParentId())).collect(Collectors.toList());
    }

    /**
     * 校验是不是文件夹
     *
     * @param fileId
     * @return
     */
    private boolean checkIsFolder(String fileId) {
        return checkIsFolder(rPanUserFileMapper.selectByFileId(fileId));
    }

    /**
     * 校验是不是文件夹
     *
     * @param fileId
     * @param userId
     * @return
     */
    private boolean checkIsFolder(String fileId, String userId) {
        return checkIsFolder(rPanUserFileMapper.selectByFileIdAndUserId(fileId, userId));
    }

    /**
     * 校验是不是文件夹
     *
     * @param rPanUserFile
     * @return
     */
    private boolean checkIsFolder(RPanUserFile rPanUserFile) {
        if (Objects.isNull(rPanUserFile)) {
            throw new RPanException("文件夹不存在");
        }
        return Objects.equals(FileConstant.FolderFlagEnum.YES.getCode(), rPanUserFile.getFolderFlag());
    }

    /**
     * 校验是否是文件夹
     *
     * @param rPanUserFileVO
     * @return
     */
    private boolean checkIsFolder(RPanUserFileVO rPanUserFileVO) {
        if (Objects.isNull(rPanUserFileVO)) {
            throw new RPanException("文件夹不存在");
        }
        return Objects.equals(FileConstant.FolderFlagEnum.YES.getCode(), rPanUserFileVO.getFolderFlag());
    }

    /**
     * 查找并拼装子文件
     *
     * @param allChildUserFileList
     * @param parentUserFileId
     * @param newParentUserFileId
     * @param userId
     * @return
     */
    private void assembleAllChildUserFile(final List<RPanUserFile> allChildUserFileList, String parentUserFileId, String newParentUserFileId, String userId) {
        List<RPanUserFile> childUserFileList = rPanUserFileMapper.selectAvailableListByParentId(parentUserFileId);
        if (CollectionUtils.isEmpty(childUserFileList)) {
            return;
        }
        childUserFileList.stream().forEach(childUserFile -> {
            String fileId = childUserFile.getFileId(),
                    newFileId = UUIDUtil.getUUID();
            childUserFile.setParentId(newParentUserFileId)
                    .setId(null)
                    .setUserId(userId)
                    .setFileId(newFileId)
                    .setCreateUser(userId)
                    .setCreateTime(new Date())
                    .setUpdateUser(userId)
                    .setUpdateTime(new Date());
            allChildUserFileList.add(childUserFile);
            if (checkIsFolder(childUserFile)) {
                assembleAllChildUserFile(allChildUserFileList, fileId, newFileId, userId);
            }
        });
    }

    /**
     * 校验是不是该物理文件还在使用
     *
     * @param realFileId
     * @return
     */
    private boolean checkRealFileUsed(String realFileId) {
        return rPanUserFileMapper.selectCountByRealFileId(realFileId) > CommonConstant.ZERO_INT;
    }

    /**
     * 查询文件的所有子节点
     *
     * @param allChildUserFileList
     * @param parentId
     */
    private void findAllChildUserFile(final List<RPanUserFile> allChildUserFileList, String parentId) {
        List<RPanUserFile> childUserFileList = rPanUserFileMapper.selectAllListByParentId(parentId);
        if (CollectionUtils.isEmpty(childUserFileList)) {
            return;
        }
        allChildUserFileList.addAll(childUserFileList);
        childUserFileList.stream()
                .filter(rPanUserFile -> checkIsFolder(rPanUserFile))
                .forEach(rPanUserFile -> findAllChildUserFile(allChildUserFileList, rPanUserFile.getFileId()));
    }

    /**
     * 保存用户搜索关键字信息
     *
     * @param keyword
     * @param userId
     */
    private void saveUserSearchHistory(String keyword, String userId) {
        iUserSearchHistoryService.save(keyword, userId);
    }

    /**
     * 拼装所有的与删除的用户文件信息列表
     *
     * @param fileIds
     * @return
     */
    private List<RPanUserFile> assembleAllToBeDeletedUserFileList(String fileIds) {
        List<RPanUserFile> rPanUserFileList = rPanUserFileMapper.selectListByFileIdList(Splitter.on(CommonConstant.COMMON_SEPARATOR).splitToList(fileIds));
        final List<RPanUserFile> allChildUserFileList = Lists.newArrayList();
        rPanUserFileList.stream()
                .filter(rPanUserFile -> checkIsFolder(rPanUserFile))
                .forEach(rPanUserFile -> findAllChildUserFile(allChildUserFileList, rPanUserFile.getFileId()));
        rPanUserFileList.addAll(allChildUserFileList);
        return rPanUserFileList;
    }

    /**
     * 拼装要删除的真实文件id列表
     *
     * @param rPanUserFileList
     * @return
     */
    private Set<String> assembleAllUnusedRealFileIdSet(List<RPanUserFile> rPanUserFileList) {
        Set<String> realFileIdSet = rPanUserFileList.stream()
                .filter(rPanUserFile -> !checkIsFolder(rPanUserFile))
                .filter(rPanUserFile -> !checkRealFileUsed(rPanUserFile.getRealFileId()))
                .map(RPanUserFile::getRealFileId)
                .collect(Collectors.toSet());
        return realFileIdSet;
    }

    /**
     * 保存复制文件
     *
     * @param fileIds
     * @param targetParentId
     * @param userId
     * @return
     */
    private void doCopyUserFiles(String fileIds, String targetParentId, String userId) {
        if (!checkIsFolder(targetParentId, userId)) {
            throw new RPanException("请选择要复制到的文件夹");
        }
        // 查询所有要被复制的文件信息
        List<RPanUserFile> toBeCopiedFileInfoList = rPanUserFileMapper.selectListByFileIdList(Splitter.on(CommonConstant.COMMON_SEPARATOR).splitToList(fileIds));
        complementToBeCopiedFileInfoList(toBeCopiedFileInfoList, targetParentId, userId);
        // 批量新增文件信息
        if (rPanUserFileMapper.insertBatch(toBeCopiedFileInfoList) != toBeCopiedFileInfoList.size()) {
            throw new RPanException("文件复制失败");
        }
    }

    /**
     * 递归查找所有子文件信息
     *
     * @param allRPanUserFileVOList
     * @param rPanUserFileVO
     */
    private void findAllAvailableChildUserFile(List<RPanUserFileVO> allRPanUserFileVOList, RPanUserFileVO rPanUserFileVO) {
        if (!checkIsFolder(rPanUserFileVO)) {
            return;
        }
        List<RPanUserFileVO> rPanUserFileVOList = rPanUserFileMapper.selectAvailableRPanUserFileVOListByParentId(rPanUserFileVO.getFileId());
        if (CollectionUtils.isEmpty(rPanUserFileVOList)) {
            return;
        }
        allRPanUserFileVOList.addAll(rPanUserFileVOList);
        rPanUserFileVOList.stream().forEach(rPanUserFileVOItem -> findAllAvailableChildUserFile(allRPanUserFileVOList, rPanUserFileVOItem));
    }

}
