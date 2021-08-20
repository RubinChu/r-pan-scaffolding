package com.rubin.rpan.storage.local.launch;

import com.rubin.rpan.schedule.ExpireTempFileCleaner;
import com.rubin.rpan.schedule.RPanScheduleManager;
import com.rubin.rpan.storage.local.config.LocalStorageConfig;
import com.rubin.rpan.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * 清理过期临时文件任务启动器
 */
@Component(value = "expireTempFileCleanerLauncher")
@ConditionalOnProperty(prefix = "rpan.storage.processor", name = "type", havingValue = "com.rubin.rpan.storage.local.LocalStorageProcessor")
public class ExpireTempFileCleanerLauncher implements RPanLaunchedProcessor {

    private final static String CRON = "1 0 0 * * ? ";

    @Autowired
    @Qualifier(value = "rPanScheduleManager")
    private RPanScheduleManager rPanScheduleManager;

    @Autowired
    @Qualifier(value = "localStorageConfig")
    private LocalStorageConfig localStorageConfig;

    /**
     * 处理初始化任务
     */
    @Override
    public void process() {
        rPanScheduleManager.startTask(new ExpireTempFileCleaner(FileUtil.generateTempFolderPath(localStorageConfig.getRootFilePath()), localStorageConfig.getIntervalDaysForClearingExpiredTempFiles()), CRON);
    }

}
