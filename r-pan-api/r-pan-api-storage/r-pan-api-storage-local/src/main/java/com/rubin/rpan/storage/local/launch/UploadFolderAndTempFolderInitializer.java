package com.rubin.rpan.storage.local.launch;

import com.rubin.rpan.storage.local.config.LocalStorageConfig;
import com.rubin.rpan.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * 初始化上传文件跟目录和临时文件目录处理器
 */
@Component(value = "uploadFolderAndTempFolderInitializer")
@ConditionalOnProperty(prefix = "rpan.storage.processor", name = "type", havingValue = "com.rubin.rpan.storage.local.LocalStorageProcessor")
public class UploadFolderAndTempFolderInitializer implements RPanLaunchedProcessor {

    private final static Logger log = LoggerFactory.getLogger(RPanLaunchedProcessor.class);

    @Autowired
    @Qualifier(value = "localStorageConfig")
    private LocalStorageConfig localStorageConfig;

    /**
     * 处理初始化任务
     */
    @Override
    public void process() {
        FileUtil.createFolder(FileUtil.generateTempFolderPath(localStorageConfig.getRootFilePath()));
        log.info("临时目录初始化完成！");
    }

}
