package com.rubin.rpan.modules.file.service;

import com.rubin.rpan.modules.file.entity.RPanFile;
import org.springframework.web.multipart.MultipartFile;

/**
 * 实体文件业务处理接口
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
public interface IFileService {

    RPanFile save(MultipartFile file, Long userId);

    void delete(String fileIds);

    RPanFile getFileDetail(Long realFileId);

}
