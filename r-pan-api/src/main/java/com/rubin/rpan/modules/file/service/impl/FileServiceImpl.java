package com.rubin.rpan.modules.file.service.impl;

import com.google.common.base.Splitter;
import com.rubin.rpan.common.config.props.FilePathConfig;
import com.rubin.rpan.common.constant.CommonConstant;
import com.rubin.rpan.common.exception.RPanException;
import com.rubin.rpan.common.util.DateUtil;
import com.rubin.rpan.common.util.FileContentTypeUtil;
import com.rubin.rpan.common.util.FileUtil;
import com.rubin.rpan.common.util.UUIDUtil;
import com.rubin.rpan.modules.file.constant.FileConstant;
import com.rubin.rpan.modules.file.dao.RPanFileMapper;
import com.rubin.rpan.modules.file.entity.RPanFile;
import com.rubin.rpan.modules.file.service.IFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * 物理文件业务处理类
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@Service(value = "fileService")
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class FileServiceImpl implements IFileService {

    @Autowired
    @Qualifier(value = "rPanFileMapper")
    private RPanFileMapper rPanFileMapper;

    @Autowired
    @Qualifier(value = "filePathConfig")
    private FilePathConfig filePathConfig;

    /**
     * 保存物理文件
     *
     * @param file
     * @param userId
     * @return
     */
    @Override
    public RPanFile save(MultipartFile file, String userId) {
        RPanFile rPanFile = assembleRPanFile(file, userId);
        saveFileInfo(rPanFile);
        uploadFile(file, rPanFile);
        return rPanFile;
    }

    /**
     * 删除物理文件
     *
     * @param fileIds
     * @return
     */
    @Override
    public void delete(String fileIds) {
        if (StringUtils.isBlank(fileIds)) {
            throw new RPanException("文件id不能为空");
        }
        deleteFileInfos(fileIds);
        deletePhysicalFiles(fileIds);
    }

    /************************************************************************私有************************************************************************/

    /**
     * 保存物理文件信息
     *
     * @param rPanFile
     */
    private void saveFileInfo(RPanFile rPanFile) {
        if (rPanFileMapper.insertSelective(rPanFile) != CommonConstant.ONE_INT) {
            throw new RPanException("上传失败");
        }
    }

    /**
     * 上传物理文件
     *
     * @param file
     * @param rPanFile
     */
    private void uploadFile(MultipartFile file, RPanFile rPanFile) {
        File targetFile = new File(rPanFile.getRealPath());
        if (!targetFile.getParentFile().exists()) {
            targetFile.getParentFile().mkdirs();
        }
        try {
            targetFile.createNewFile();
            file.transferTo(targetFile);
        } catch (IOException e) {
            log.error("上传失败", e);
            throw new RPanException("上传失败");
        }
    }

    /**
     * 获取文件的物理上传路径
     *
     * @param newFileName
     * @return
     */
    private String getRealFilePath(String newFileName) {
        return filePathConfig.getPath() + FileConstant.SEPARATOR_STR + DateUtil.getTodayDayString() + FileConstant.SEPARATOR_STR + newFileName;
    }

    /**
     * 拼装物理文件信息
     *
     * @param file
     * @param userId
     * @return
     */
    private RPanFile assembleRPanFile(MultipartFile file, String userId) {
        RPanFile rPanFile = new RPanFile();
        String fileName = file.getOriginalFilename();
        String suffix = FileUtil.getFileSuffix(fileName);
        String newFileName = UUIDUtil.getUUID() + suffix;
        long fileSize = file.getSize();
        rPanFile.setFileId(UUIDUtil.getUUID())
                .setFilename(newFileName)
                .setRealPath(getRealFilePath(newFileName))
                .setFileSize(String.valueOf(fileSize))
                .setFileSizeDesc(FileUtil.getFileSizeDesc(fileSize))
                .setFileSuffix(suffix)
                .setFilePreviewContentType(FileContentTypeUtil.get(fileName))
                .setCreateUser(userId)
                .setCreateTime(new Date());
        return rPanFile;
    }

    /**
     * 删除物理文件信息
     *
     * @param fileIds
     * @return
     */
    private void deleteFileInfos(String fileIds) {
        List<String> fileIdList = Splitter.on(CommonConstant.COMMON_SEPARATOR).splitToList(fileIds);
        if (rPanFileMapper.deleteBatch(fileIdList) != fileIdList.size()) {
            throw new RPanException("删除物理文件信息失败");
        }
    }

    /**
     * 批量删除物理文件
     *
     * @param fileIds
     * @return
     */
    private void deletePhysicalFiles(String fileIds) {
        List<RPanFile> rPanFileList = rPanFileMapper.selectByFileIdList(Splitter.on(CommonConstant.COMMON_SEPARATOR).splitToList(fileIds));
        rPanFileList.stream().map(RPanFile::getRealPath).forEach(path -> {
            File file = new File(path);
            if (!file.delete()) {
                log.error("文件路径：{}，删除失败！", path);
            }
        });
    }

}

