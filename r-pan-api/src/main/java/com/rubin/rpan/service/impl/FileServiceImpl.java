package com.rubin.rpan.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.rubin.rpan.common.config.props.FilePathConfig;
import com.rubin.rpan.common.constants.Constants;
import com.rubin.rpan.common.exception.RPanException;
import com.rubin.rpan.common.response.ResponseCode;
import com.rubin.rpan.common.response.ServerResponse;
import com.rubin.rpan.common.util.UUIDUtil;
import com.rubin.rpan.dao.RPanFileMapper;
import com.rubin.rpan.entity.RPanFile;
import com.rubin.rpan.service.IFileService;
import com.rubin.rpan.vo.BreadcrumbVO;
import com.rubin.rpan.vo.DirectoryTreeNode;
import com.rubin.rpan.vo.RPanFileVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * file business processing
 * Created by rubin on 2019/9/7.
 */

@Service
@AllArgsConstructor
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class FileServiceImpl implements IFileService {

    private RPanFileMapper rPanFileMapper;

    private FilePathConfig filePathConfig;

    /**
     * list
     *
     * @param parentId
     * @param fileTypes
     * @param userId
     * @return
     */
    @Override
    public ServerResponse<List<RPanFileVO>> list(String parentId, String fileTypes, Integer userId) {
        if (Objects.isNull(userId)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        List<String> fileTypeArray = null;
        if (Objects.equals(Constants.TOP_STR, parentId)) {
            return ServerResponse.createBySuccess(Lists.newArrayList());
        }
        if (!Objects.equals(fileTypes, Constants.MINUS_ONE_STR)) {
            fileTypeArray = Splitter.on(Constants.COMMON_SEPARATOR).splitToList(fileTypes);
        }
        return ServerResponse.createBySuccess(rPanFileMapper.selectByUserIdAndFileTypeAndParentId(userId, fileTypeArray, parentId).stream().map(RPanFileVO::assembleRPanFileVO).collect(Collectors.toList()));
    }

    /**
     * createDirectory
     *
     * @param parentId
     * @param directoryName
     * @param userId
     * @return
     */
    @Override
    public ServerResponse<List<RPanFileVO>> createDirectory(String parentId, String directoryName, Integer userId) {
        if (Objects.isNull(userId)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        if (StringUtils.isEmpty(directoryName)) {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        RPanFile rPanFile = assembleRPanFile(parentId, userId, directoryName, Constants.FileType.DIRECTORY.getCode());
        if (rPanFileMapper.insertSelective(rPanFile) != Constants.ONE_INT) {
            return ServerResponse.createByErrorMessage("文件夹创建失败");
        }
        File directory = new File(rPanFile.getRealPath());
        if (!directory.mkdirs()) {
            throw new RPanException("文件夹创建失败");
        }
        return list(parentId, Constants.MINUS_ONE_STR, userId);
    }

    /**
     * updateFileName
     *
     * @param fileId
     * @param newFileName
     * @param userId
     * @return
     */
    @Override
    public ServerResponse<List<RPanFileVO>> updateFileName(String fileId, String newFileName, Integer userId) {
        if (Objects.isNull(userId)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        if (StringUtils.isEmpty(fileId) || StringUtils.isEmpty(newFileName)) {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        RPanFile originalFileInfo = rPanFileMapper.selectByFileId(fileId);
        if (Objects.equals(originalFileInfo.getFileName(), newFileName)) {
            return ServerResponse.createByErrorMessage("请使用一个新名称");
        }
        if (rPanFileMapper.selectCountByUserIdAndFileNameAndParentId(userId, newFileName, originalFileInfo.getParentId()) > 0) {
            return ServerResponse.createByErrorMessage("名称已被占用");
        }
        if (Objects.equals(originalFileInfo.getType(), Constants.FileType.DIRECTORY.getCode())) {
            renameDirectory(originalFileInfo, newFileName);
        } else {
            renameFile(originalFileInfo, newFileName, userId);
        }
        return list(originalFileInfo.getParentId(), Constants.MINUS_ONE_STR, userId);
    }

    /**
     * delete files (batch)
     *
     * @param parentId
     * @param fileIds
     * @param userId
     * @return
     */
    @Override
    public ServerResponse<List<RPanFileVO>> delete(String parentId, String fileIds, Integer userId) {
        if (Objects.isNull(userId)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        if (Objects.isNull(parentId) || StringUtils.isEmpty(fileIds)) {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        List<RPanFile> toBeDeletedFileInfos = rPanFileMapper.selectByFileIds(Splitter.on(Constants.COMMON_SEPARATOR).splitToList(fileIds));
        deleteFileInfo(toBeDeletedFileInfos, userId);
        doDeleteFile(toBeDeletedFileInfos);
        return list(parentId, Constants.MINUS_ONE_STR, userId);
    }

    /**
     * file upload
     *
     * @param file
     * @param parentId
     * @param userId
     * @return
     */
    @Override
    public ServerResponse<List<RPanFileVO>> upload(MultipartFile file, String parentId, Integer userId) {
        if (Objects.isNull(userId)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        if (file.isEmpty()) {
            return ServerResponse.createByErrorMessage("上传文件为空");
        }
        String fileName = file.getOriginalFilename();
        RPanFile fileInfo = assembleRPanFile(parentId, userId, fileName, getFileType(fileName));
        fileInfo.setFileSize(getFileSize(file));
        if (rPanFileMapper.insertSelective(fileInfo) != Constants.ONE_INT) {
            return ServerResponse.createByErrorMessage("上传失败");
        }
        File targetFile = new File(fileInfo.getRealPath());
        try {
            targetFile.createNewFile();
            file.transferTo(targetFile);
        } catch (IOException e) {
            log.error("上传失败", e);
            throw new RPanException("上传失败");
        }
        return list(parentId, Constants.MINUS_ONE_STR, userId);
    }

    /**
     * file download
     *
     * @param fileId
     * @param userId
     * @param response
     */
    @Override
    public void download(String fileId, Integer userId, HttpServletResponse response) {
        if (Objects.isNull(userId)) {
            return;
        }
        RPanFile targetFileInfo = rPanFileMapper.selectByFileId(fileId);
        download(new File(targetFileInfo.getRealPath()), response);
    }

    /**
     * getDirectoryTree
     *
     * @param userId
     * @return
     */
    @Override
    public ServerResponse<List<DirectoryTreeNode>> getDirectoryTree(Integer userId) {
        if (Objects.isNull(userId)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        List<RPanFile> directoryFileInfos = rPanFileMapper.selectByUserIdAndFileType(userId, Constants.FileType.DIRECTORY.getCode());
        List<DirectoryTreeNode> result = assembleDirectoryTree(directoryFileInfos);
        return ServerResponse.createBySuccess(result);
    }

    /**
     * transfer files (batch)
     *
     * @param fileIds
     * @param parentId
     * @param targetParentId
     * @param userId
     * @return
     */
    @Override
    public ServerResponse<List<RPanFileVO>> transfer(String fileIds, String parentId, String targetParentId, Integer userId) {
        if (Objects.isNull(userId)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        if (StringUtils.isEmpty(fileIds) || Objects.isNull(targetParentId)) {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        List<RPanFile> toBeTransferredFileInfos = rPanFileMapper.selectByFileIds(Splitter.on(Constants.COMMON_SEPARATOR).splitToList(fileIds));
        RPanFile targetParentFileInfo = rPanFileMapper.selectByFileId(targetParentId);
        String newParentRealPath = targetParentFileInfo.getRealPath(),
                newParentShowPath = targetParentFileInfo.getShowPath();
        Map<String, List<RPanFile>> allFileInfoGroup = getAllFileInfoGroup(userId);
        toBeTransferredFileInfos.stream().forEach(toBeTransferredFileInfo -> {
            // 转移的目标文件为文件的原父文件夹或者目标文件为选中的文件列表的某一个文件夹 这两种情况不操作此文件
            if (!Objects.equals(toBeTransferredFileInfo.getParentId(), targetParentId) && !Objects.equals(toBeTransferredFileInfo.getFileId(), targetParentId)) {
                Map<String, String> filePhysicalAddress = Maps.newHashMap();
                RPanFile originParentFileInfo = rPanFileMapper.selectByFileId(toBeTransferredFileInfo.getParentId());
                String originParentRealPath = originParentFileInfo.getRealPath(),
                        originParentShowPath = originParentFileInfo.getShowPath();
                String nodeOriginRealPath = toBeTransferredFileInfo.getRealPath(),
                        nodeOriginShowPath = toBeTransferredFileInfo.getShowPath(),
                        nodeNewRealPath,
                        nodeNewShowPath;
                filePhysicalAddress.put(Constants.ORIGIN_FILE_REAL_PATH_STR, nodeOriginRealPath);
                List<RPanFile> thisAndAllChildren = getAllNodes(allFileInfoGroup, Arrays.asList(toBeTransferredFileInfo));
                toBeTransferredFileInfo.setParentId(targetParentId)
                        .setRealPath(nodeOriginRealPath.replace(originParentRealPath, newParentRealPath))
                        .setShowPath(nodeOriginShowPath.replace(originParentShowPath, newParentShowPath));
                handleDuplicateFileName(toBeTransferredFileInfo, newParentShowPath, newParentRealPath);
                nodeNewRealPath = toBeTransferredFileInfo.getRealPath();
                nodeNewShowPath = toBeTransferredFileInfo.getShowPath();
                filePhysicalAddress.put(Constants.NEW_FILE_REAL_PATH_STR, nodeNewRealPath);
                thisAndAllChildren.stream().forEach(node -> {
                    node.setRealPath(node.getRealPath().replace(nodeOriginRealPath, nodeNewRealPath))
                            .setShowPath(node.getShowPath().replace(nodeOriginShowPath, nodeNewShowPath));
                });
                transferOne(thisAndAllChildren, filePhysicalAddress);
            }
        });
        return list(parentId, Constants.MINUS_ONE_STR, userId);
    }

    /**
     * copy files in bulk
     *
     * @param fileIds
     * @param parentId
     * @param targetParentId
     * @param userId
     * @return
     */
    @Override
    public ServerResponse<List<RPanFileVO>> copy(String fileIds, String parentId, String targetParentId, Integer userId) {
        if (Objects.isNull(userId)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        if (StringUtils.isEmpty(fileIds) || Objects.isNull(parentId) || Objects.isNull(targetParentId)) {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        if (fileIds.indexOf(targetParentId) != Constants.MINUS_ONE_INT) {
            return ServerResponse.createByErrorMessage("要复制的文件中包含选中的目标文件夹,请重新选择");
        }
        List<RPanFile> toBeCopiedFileInfos = rPanFileMapper.selectByFileIds(Splitter.on(Constants.COMMON_SEPARATOR).splitToList(fileIds));
        List<RPanFile> allNodes = Lists.newArrayList();
        List<Map<String, String>> filePhysicalAddresses = Lists.newArrayList();
        RPanFile targetParentFileInfo = rPanFileMapper.selectByFileId(targetParentId);
        String newParentRealPath = targetParentFileInfo.getRealPath(),
                newParentShowPath = targetParentFileInfo.getShowPath();
        Map<String, List<RPanFile>> allFileInfoGroup = getAllFileInfoGroup(userId);
        toBeCopiedFileInfos.stream().forEach(toBeCopiedFileInfo -> {
            Map<String, String> filePhysicalAddress = Maps.newHashMap();
            RPanFile originParentFileInfo = rPanFileMapper.selectByFileId(toBeCopiedFileInfo.getParentId());
            String originParentRealPath = originParentFileInfo.getRealPath(),
                    originParentShowPath = originParentFileInfo.getShowPath();
            String nodeOriginRealPath = toBeCopiedFileInfo.getRealPath(),
                    nodeOriginShowPath = toBeCopiedFileInfo.getShowPath(),
                    nodeNewRealPath;
            filePhysicalAddress.put(Constants.ORIGIN_FILE_REAL_PATH_STR, nodeOriginRealPath);
            RPanFile copiedFileInfo = new RPanFile();
            copiedFileInfo.setFileId(UUIDUtil.getUUID())
                    .setFileName(toBeCopiedFileInfo.getFileName())
                    .setParentId(targetParentId)
                    .setType(toBeCopiedFileInfo.getType())
                    .setShowPath(nodeOriginShowPath.replace(originParentShowPath, newParentShowPath))
                    .setRealPath(nodeOriginRealPath.replace(originParentRealPath, newParentRealPath))
                    .setUserId(userId)
                    .setCreateUser(userId)
                    .setCreateTime(new Date())
                    .setUpdateUser(userId)
                    .setUpdateTime(new Date());
            if (!Objects.isNull(toBeCopiedFileInfo.getFileSize())) {
                copiedFileInfo.setFileSize(toBeCopiedFileInfo.getFileSize());
            }
            handleDuplicateFileName(copiedFileInfo, newParentShowPath, newParentRealPath);
            nodeNewRealPath = copiedFileInfo.getRealPath();
            filePhysicalAddress.put(Constants.NEW_FILE_REAL_PATH_STR, nodeNewRealPath);
            filePhysicalAddresses.add(filePhysicalAddress);
            if (Objects.equals(Constants.FileType.DIRECTORY.getCode(), copiedFileInfo.getType())) {
                allNodes.addAll(assembleChildrenCopiedFileInfos(Lists.newArrayList(), allFileInfoGroup, copiedFileInfo, toBeCopiedFileInfo));
            }
            allNodes.add(copiedFileInfo);
        });

        if (rPanFileMapper.insertBatch(allNodes) != allNodes.size()) {
            return ServerResponse.createByErrorMessage("文件复制失败");
        }

        filePhysicalAddresses.stream().forEach(filePhysicalAddress -> {
            File originFile = new File(filePhysicalAddress.get(Constants.ORIGIN_FILE_REAL_PATH_STR)),
                    targetFile = new File(filePhysicalAddress.get(Constants.NEW_FILE_REAL_PATH_STR));
            if (!originFile.exists()) {
                log.error("文件{}不存在!", originFile.getPath());
                return;
            }
            try {
                if (originFile.isDirectory()) {
                    FileUtils.copyDirectory(originFile, targetFile);
                } else {
                    FileUtils.copyFile(originFile, targetFile);
                }
            } catch (IOException e) {
                log.error("文件复制失败", e);
            }
        });
        return list(parentId, Constants.MINUS_ONE_STR, userId);
    }

    /**
     * search files by file name
     *
     * @param keyword
     * @param userId
     * @return
     */
    @Override
    public ServerResponse<List<RPanFileVO>> search(String keyword, Integer userId) {
        if (Objects.isNull(userId)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return ServerResponse.createBySuccess(rPanFileMapper.selectByUserIdAndFileName(userId, keyword).stream().map(RPanFileVO::assembleRPanFileVO).collect(Collectors.toList()));
    }

    /**
     * check file details
     *
     * @param fileId
     * @param userId
     * @return
     */
    @Override
    public ServerResponse<RPanFileVO> detail(String fileId, Integer userId) {
        if (Objects.isNull(userId)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return ServerResponse.createBySuccess(RPanFileVO.assembleRPanFileVO(rPanFileMapper.selectByFileIdAndUserId(fileId, userId)));
    }

    /**
     * bulk package download
     *
     * @param fileIds
     * @param response
     * @param userId
     */
    @Override
    public void downloadZip(String fileIds, HttpServletResponse response, Integer userId) {
        if (Objects.isNull(userId) || StringUtils.isEmpty(fileIds)) {
            return;
        }
        List<RPanFile> rPanFiles = rPanFileMapper.selectByFileIds(Splitter.on(Constants.COMMON_SEPARATOR).splitToList(fileIds));
        download(zipFiles(rPanFiles), response);
    }

    /**
     * get a list of breadcrumbs
     *
     * @param fileId
     * @param userId
     * @return
     */
    @Override
    public ServerResponse<List<BreadcrumbVO>> getBreadcrumbs(String fileId, Integer userId) {
        if (Objects.isNull(userId)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        List<RPanFile> rPanFiles = rPanFileMapper.selectByUserIdAndFileType(userId, Constants.FileType.DIRECTORY.getCode());
        Map<String, BreadcrumbVO> breadcrumbVOTransferMap = rPanFiles.stream().collect(Collectors.toMap(RPanFile::getFileId, BreadcrumbVO::assembleBreadcrumbVO));
        List<BreadcrumbVO> breadcrumbVOS = Lists.newArrayList();
        BreadcrumbVO thisLevel = breadcrumbVOTransferMap.get(fileId);
        do {
            breadcrumbVOS.add(thisLevel);
            thisLevel = breadcrumbVOTransferMap.get(thisLevel.getParentId());
        } while (!Objects.isNull(thisLevel));
        Collections.reverse(breadcrumbVOS);
        return ServerResponse.createBySuccess(breadcrumbVOS);
    }

    /******************************************************private****************************************************/

    /**
     * get file type
     *
     * @param fileName
     * @return
     */
    private Integer getFileType(String fileName) {
        if (fileName.indexOf(Constants.POINT_STR) == Constants.MINUS_ONE_INT) {
            return Constants.FileType.DIRECTORY.getCode();
        }
        String tag = fileName.substring(fileName.lastIndexOf(Constants.POINT_STR) + Constants.ONE_INT).toUpperCase();
        switch (tag) {
            case "RAR":
            case "ZIP":
            case "CAB":
            case "ISO":
            case "JAR":
            case "ACE":
            case "7Z":
            case "TAR":
            case "GZ":
            case "ARJ":
            case "LZH":
            case "UUE":
            case "BZ2":
            case "Z":
            case "WAR":
                return Constants.FileType.ARCHIVE.getCode();
            case "XLSX":
            case "XLS":
            case "CSV":
                return Constants.FileType.EXCEL.getCode();
            case "DOC":
            case "DOCX":
                return Constants.FileType.WORD.getCode();
            case "PDF":
                return Constants.FileType.PDF.getCode();
            case "TXT":
                return Constants.FileType.TXT.getCode();
            case "BMP":
            case "GIF":
            case "PNG":
            case "ICO":
            case "EPS":
            case "PSD":
            case "TGA":
            case "TIFF":
            case "JPG":
            case "JPEG":
                return Constants.FileType.IMG.getCode();
            case "MP3":
            case "MKV":
            case "MPG":
            case "RM":
            case "WMA":
                return Constants.FileType.AUDIO.getCode();
            case "AVI":
            case "3GP":
            case "MP4":
            case "FLV":
            case "RMVB":
            case "MOV":
                return Constants.FileType.VIDEO.getCode();
            case "PPT":
                return Constants.FileType.POWERPOINT.getCode();
            case "JAVA":
            case "OBJ":
            case "H":
            case "C":
            case "HTML":
            case "NET":
            case "PHP":
            case "CSS":
            case "JS":
            case "FTL":
            case "JSP":
            case "ASP":
                return Constants.FileType.CODE.getCode();
            default:
                return Constants.FileType.FILE.getCode();
        }
    }

    /**
     * delete files or folders
     *
     * @param file
     * @return
     */
    private boolean delFile(File file) {
        if (!file.exists()) {
            return false;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File childFile : files) {
                delFile(childFile);
            }
        }
        return file.delete();
    }

    /**
     * obtain the new name of the file. When uploading the file, if the file name is duplicated, add 1
     *
     * @param parentFilePath
     * @param fileName
     * @return
     */
    private String getNewFileName(String parentFilePath, String fileName) {
        File parentDirectory = new File(parentFilePath);
        int lastPointIndex;
        String fileNameWithoutTag = fileName,
                fileSuffix = Constants.EMPTY_STR;
        if ((lastPointIndex = fileName.lastIndexOf(Constants.POINT_STR)) != Constants.MINUS_ONE_INT) {
            fileNameWithoutTag = fileName.substring(Constants.ZERO_INT, lastPointIndex);
            fileSuffix = fileName.substring(lastPointIndex);
        }
        String compareFileName = fileNameWithoutTag;
        int num = Constants.ONE_INT;
        while (checkSameFileName(parentDirectory, fileName)) {
            fileName = new StringBuffer(compareFileName)
                    .append(Constants.CN_LEFT_PARENTHESES_STR)
                    .append(num)
                    .append(Constants.CN_RIGHT_PARENTHESES_STR)
                    .append(fileSuffix)
                    .toString();
            num++;
        }
        return fileName;
    }

    /**
     * assembly file entity information
     *
     * @param parentId
     * @param userId
     * @param fileName
     * @param fileType
     * @return
     */
    private RPanFile assembleRPanFile(String parentId, Integer userId, String fileName, Integer fileType) {
        RPanFile rPanFile = new RPanFile();
        String parentShowPath, parentRealPath;
        if (Objects.equals(Constants.TOP_STR, parentId)) {
            parentShowPath = filePathConfig.getNginxPath();
            parentRealPath = filePathConfig.getPath();
        } else {
            RPanFile parentDirectory = rPanFileMapper.selectByFileId(parentId);
            parentShowPath = parentDirectory.getShowPath();
            parentRealPath = parentDirectory.getRealPath();
        }
        rPanFile.setFileId(UUIDUtil.getUUID())
                .setParentId(parentId)
                .setUserId(userId)
                .setFileName(fileName)
                .setShowPath(assemblePath(parentShowPath, fileName))
                .setRealPath(assemblePath(parentRealPath, fileName))
                .setFileSize(Constants.EMPTY_STR)
                .setType(fileType)
                .setCreateUser(userId)
                .setCreateTime(new Date())
                .setUpdateUser(userId)
                .setUpdateTime(new Date());
        handleDuplicateFileName(rPanFile, parentShowPath, parentRealPath);
        return rPanFile;
    }

    /**
     * handle duplicate file names
     *
     * @param rPanFile
     * @param parentShowPath
     * @param parentRealPath
     */
    private void handleDuplicateFileName(RPanFile rPanFile, String parentShowPath, String parentRealPath) {
        File targetFile = new File(rPanFile.getRealPath());
        if (targetFile.exists()) {
            String newFileName = getNewFileName(parentRealPath, rPanFile.getFileName());
            rPanFile.setFileName(newFileName)
                    .setRealPath(assemblePath(parentRealPath, newFileName))
                    .setShowPath(assemblePath(parentShowPath, newFileName));
        }
    }

    /**
     * assembly file path
     *
     * @param parentPath
     * @param fileName
     * @return
     */
    private String assemblePath(String parentPath, String fileName) {
        return new StringBuilder(parentPath)
                .append(Constants.SEPARATOR_STR)
                .append(fileName)
                .toString();
    }

    /**
     * replace the file path with the new file name
     *
     * @param oldFilePath
     * @param newFileName
     * @return
     */
    private String changeFileName(String oldFilePath, String newFileName) {
        return new StringBuilder(oldFilePath)
                .replace(oldFilePath.lastIndexOf(Constants.SEPARATOR_STR) + Constants.ONE_INT, oldFilePath.length(), newFileName)
                .toString();
    }

    /**
     * assemble the folder tree
     *
     * @param directoryFileInfos
     * @return
     */
    private List<DirectoryTreeNode> assembleDirectoryTree(List<RPanFile> directoryFileInfos) {
        List<DirectoryTreeNode> directoryTreeNodes = directoryFileInfos.stream().map(DirectoryTreeNode::assembleDirectoryTreeNode).collect(Collectors.toList());
        Map<String, List<DirectoryTreeNode>> directoryTreeNodeParentGroup = directoryTreeNodes.stream().collect(Collectors.groupingBy(DirectoryTreeNode::getParentId));
        directoryTreeNodes.stream().forEach(node -> {
            List<DirectoryTreeNode> children = directoryTreeNodeParentGroup.get(node.getId());
            if (!CollectionUtils.isEmpty(children)) {
                node.setChildren(children);
            }
        });
        directoryTreeNodes = directoryTreeNodes.stream().filter(node -> Objects.equals(Constants.TOP_STR, node.getParentId())).collect(Collectors.toList());
        directoryTreeNodes.get(0).setLabel(Constants.ALL_FILE_CN_STR);
        return directoryTreeNodes;
    }

    /**
     * compressed file
     *
     * @param fileList
     * @return
     */
    private File zipFiles(List<RPanFile> fileList) {
        File zipFile = new File(filePathConfig.getZipPath() + Constants.SEPARATOR_STR + UUIDUtil.getUUID() + Constants.ZIP_FILE_NAME_SUFFIX_STR);
        if (zipFile.exists()) {
            if (!zipFile.delete()) {
                throw new RuntimeException("旧文件删除失败");
            }
        }
        try {
            if (!zipFile.createNewFile()) {
                throw new RuntimeException("压缩文件创建失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile))) {
            fileList.stream().forEach(rPanFile -> zipFile(new File(rPanFile.getRealPath()), zipOutputStream, rPanFile.getFileName()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return zipFile;
    }

    /**
     * compressed file
     *
     * @param file
     * @param zipOutputStream
     * @param fileName
     */
    private void zipFile(File file, ZipOutputStream zipOutputStream, String fileName) {
        if (file.exists()) {
            if (file.isFile()) {
                try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file), 512)) {
                    zipOutputStream.putNextEntry(new ZipEntry(fileName));
                    int nNumber;
                    byte[] buffer = new byte[512];
                    while ((nNumber = bis.read(buffer)) != Constants.MINUS_ONE_INT) {
                        zipOutputStream.write(buffer, Constants.ZERO_INT, nNumber);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                File[] files = file.listFiles();
                try {
                    if (files.length == Constants.ZERO_INT) {
                        zipOutputStream.putNextEntry(new ZipEntry(fileName + Constants.SEPARATOR_STR));
                    } else {
                        Arrays.stream(files).forEach(subFile -> zipFile(subFile, zipOutputStream, fileName + Constants.SEPARATOR_STR + subFile.getName()));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * download file
     *
     * @param file
     * @param response
     */
    private void download(File file, HttpServletResponse response) {
        response.reset();
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        response.setHeader(Constants.CONTENT_TYPE_STR, Constants.APPLICATION_OCTET_STREAM_STR);
        response.setContentType(Constants.APPLICATION_OCTET_STREAM_STR);
        try {
            response.setHeader(Constants.CONTENT_DISPOSITION_STR, Constants.CONTENT_DISPOSITION_VALUE_PREFIX_STR + new String(file.getName().getBytes(Constants.GB2312_STR), Constants.IOS_8859_1_STR));
        } catch (UnsupportedEncodingException e) {
            log.error("下载文件失败", e);
        }
        byte[] buffer = new byte[1024];
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
            OutputStream os = response.getOutputStream();
            int i = bis.read(buffer);
            while (i != Constants.MINUS_ONE_INT) {
                os.write(buffer, Constants.ZERO_INT, i);
                i = bis.read(buffer);
            }
        } catch (Exception e) {
            log.error("下载文件失败", e);
        }
    }

    /**
     * recursively query all child nodes
     *
     * @param nodes
     * @param allFileInfoGroup
     * @param originalFileInfo
     */
    private List<RPanFile> filterAllChildrenNodes(List<RPanFile> nodes, Map<String, List<RPanFile>> allFileInfoGroup, RPanFile originalFileInfo) {
        if (!nodes.contains(originalFileInfo)) {
            nodes.add(originalFileInfo);
        }
        List<RPanFile> childrenNodes = allFileInfoGroup.get(originalFileInfo.getFileId());
        if (CollectionUtils.isEmpty(childrenNodes)) {
            return nodes;
        }
        nodes.addAll(childrenNodes);
        childrenNodes.stream().forEach(childrenNode -> filterAllChildrenNodes(nodes, allFileInfoGroup, childrenNode));
        return nodes;
    }

    /**
     * file renaming (operating hard disk files)
     *
     * @param oldFileRealPath
     * @param newFileRealPath
     */
    private void doRenameFile(String oldFileRealPath, String newFileRealPath) {
        File oldFile = new File(oldFileRealPath),
                newFile = new File(newFileRealPath);
        if (!oldFile.renameTo(newFile)) {
            throw new RPanException("文件重命名失败");
        }
    }

    /**
     * rename folder
     *
     * @param originalFileInfo
     * @param newFileName
     */
    private void renameDirectory(RPanFile originalFileInfo, String newFileName) {
        String oldFileRealPath = originalFileInfo.getRealPath(),
                oldFileShowPath = originalFileInfo.getShowPath(),
                newFileRealPath = changeFileName(oldFileRealPath, newFileName),
                newFileShowPath = changeFileName(oldFileShowPath, newFileName);
        List<RPanFile> thisAndAllChildren = getAllNodes(getAllFileInfoGroup(originalFileInfo.getUserId()), Arrays.asList(originalFileInfo));
        thisAndAllChildren.stream().forEach(node -> {
            if (node.equals(originalFileInfo)) {
                node.setFileName(newFileName);
            }
            node.setRealPath(node.getRealPath().replace(oldFileRealPath, newFileRealPath));
            node.setShowPath(node.getShowPath().replace(oldFileShowPath, newFileShowPath));
        });
        if (rPanFileMapper.updateFileInfoBatch(thisAndAllChildren) != thisAndAllChildren.size()) {
            throw new RPanException("文件重命名失败");
        }
        doRenameFile(oldFileRealPath, newFileRealPath);
    }

    /**
     * rename file
     *
     * @param originalFileInfo
     * @param newFileName
     * @param userId
     */
    private void renameFile(RPanFile originalFileInfo, String newFileName, Integer userId) {
        String oldFileRealPath = originalFileInfo.getRealPath(),
                newFileRealPath = changeFileName(originalFileInfo.getRealPath(), newFileName);
        originalFileInfo.setFileName(newFileName);
        originalFileInfo.setRealPath(newFileRealPath);
        originalFileInfo.setShowPath(changeFileName(originalFileInfo.getShowPath(), newFileName));
        originalFileInfo.setUpdateUser(userId);
        originalFileInfo.setUpdateTime(new Date());
        if (rPanFileMapper.updateByPrimaryKeySelective(originalFileInfo) != Constants.ONE_INT) {
            throw new RPanException("文件重命名失败");
        }
        doRenameFile(oldFileRealPath, newFileRealPath);
    }

    /**
     * physically delete file information
     *
     * @param toBeDeletedFileInfos
     */
    private void doDeleteFile(List<RPanFile> toBeDeletedFileInfos) {
        toBeDeletedFileInfos.stream().forEach(fileInfo -> {
            if (!delFile(new File(fileInfo.getRealPath()))) {
                log.error("文件路径:{},物理删除失败", fileInfo.getRealPath());
            }
        });
    }

    /**
     * delete file info
     *
     * @param toBeDeletedFileInfos
     * @param userId
     */
    private void deleteFileInfo(List<RPanFile> toBeDeletedFileInfos, Integer userId) {
        List<RPanFile> allNodes = getAllNodes(getAllFileInfoGroup(userId), toBeDeletedFileInfos);
        if (rPanFileMapper.deleteBatch(allNodes, userId) != allNodes.size()) {
            throw new RPanException("删除失败");
        }
    }

    /**
     * improve all child nodes of the folders in the collection to facilitate batch operations
     *
     * @param allFileInfoGroup
     * @param originNodes
     * @return
     */
    private List<RPanFile> getAllNodes(Map<String, List<RPanFile>> allFileInfoGroup, List<RPanFile> originNodes) {
        List<RPanFile> allNodes = Lists.newArrayList();
        originNodes.stream().forEach(fileInfo -> {
            if (Objects.equals(Constants.FileType.DIRECTORY.getCode(), fileInfo.getType())) {
                allNodes.addAll(filterAllChildrenNodes(Lists.newArrayList(), allFileInfoGroup, fileInfo));
            } else {
                allNodes.add(fileInfo);
            }
        });
        return allNodes;
    }

    /**
     * get grouping information of all files of this user (grouped according to parentId)
     *
     * @param userId
     * @return
     */
    private Map<String, List<RPanFile>> getAllFileInfoGroup(Integer userId) {
        List<RPanFile> allFileInfos = rPanFileMapper.selectByUserIdAndFileType(userId, Constants.FileType.ALL.getCode());
        return allFileInfos.stream().collect(Collectors.groupingBy(RPanFile::getParentId));
    }

    /**
     * check if there is a file with the same file name under the parent folder
     *
     * @param parentDirectory
     * @param fileName
     * @return
     */
    private boolean checkSameFileName(File parentDirectory, String fileName) {
        return parentDirectory.list((dir, name) -> Objects.equals(name, fileName)).length != Constants.ZERO_INT;
    }

    /**
     * assemble all the sub-node file information to be copied
     *
     * @param childrenCopiedFileInfos
     * @param allFileInfoGroup
     * @param copiedFileInfo
     * @param toBeCopiedFileInfo
     * @return
     */
    private List<RPanFile> assembleChildrenCopiedFileInfos(List<RPanFile> childrenCopiedFileInfos, Map<String, List<RPanFile>> allFileInfoGroup, RPanFile copiedFileInfo, RPanFile toBeCopiedFileInfo) {
        List<RPanFile> originChildrenFileInfos = allFileInfoGroup.get(toBeCopiedFileInfo.getFileId());
        if (CollectionUtils.isEmpty(originChildrenFileInfos)) {
            return childrenCopiedFileInfos;
        }
        originChildrenFileInfos.stream().forEach(originChildrenFileInfo -> {
            String fileName = originChildrenFileInfo.getFileName();
            Integer userId = originChildrenFileInfo.getUserId();
            RPanFile copiedChildrenFileInfo = new RPanFile();
            copiedChildrenFileInfo.setFileId(UUIDUtil.getUUID())
                    .setFileName(originChildrenFileInfo.getFileName())
                    .setParentId(copiedFileInfo.getFileId())
                    .setType(originChildrenFileInfo.getType())
                    .setShowPath(assemblePath(copiedFileInfo.getShowPath(), fileName))
                    .setRealPath(assemblePath(copiedFileInfo.getRealPath(), fileName))
                    .setUserId(userId)
                    .setCreateUser(userId)
                    .setCreateTime(new Date())
                    .setUpdateUser(userId)
                    .setUpdateTime(new Date());
            childrenCopiedFileInfos.add(copiedChildrenFileInfo);
            if (Objects.equals(Constants.FileType.DIRECTORY.getCode(), copiedChildrenFileInfo.getType())) {
                assembleChildrenCopiedFileInfos(childrenCopiedFileInfos, allFileInfoGroup, copiedChildrenFileInfo, originChildrenFileInfo);
            }
        });
        return childrenCopiedFileInfos;
    }

    /**
     * transfer a file
     *
     * @param thisAndAllChildren
     * @param filePhysicalAddress
     */
    private void transferOne(List<RPanFile> thisAndAllChildren, Map<String, String> filePhysicalAddress) {
        if (rPanFileMapper.updateFileInfoBatch(thisAndAllChildren) != thisAndAllChildren.size()) {
            log.error("文件转移失败,文件路径为:{}", filePhysicalAddress.get(Constants.ORIGIN_FILE_REAL_PATH_STR));
            return;
        }
        File originFile = new File(filePhysicalAddress.get(Constants.ORIGIN_FILE_REAL_PATH_STR)),
                targetFile = new File(filePhysicalAddress.get(Constants.NEW_FILE_REAL_PATH_STR));
        if (!originFile.exists()) {
            log.error("文件{}不存在!", originFile.getPath());
            return;
        }
        if (!originFile.renameTo(targetFile)) {
            log.error("文件{}转移到{}失败", originFile.getPath(), targetFile.getPath());
        }
    }

    /**
     * get file size string
     *
     * @param file
     * @return
     */
    private String getFileSize(MultipartFile file) {
        double fileSize = (double) file.getSize();
        String fileSizeSuffix = Constants.KB_STR;
        fileSize = fileSize / 1024;
        if (fileSize > 1024) {
            fileSize = fileSize / 1024;
            fileSizeSuffix = Constants.MB_STR;
        }
        if (fileSize > 1024) {
            fileSize = fileSize / 1024;
            fileSizeSuffix = Constants.GB_STR;
        }
        return String.format("%.2f", fileSize) + fileSizeSuffix;
    }

}

