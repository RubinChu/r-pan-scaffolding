package com.rubin.rpan.schedule;

import com.rubin.rpan.util.DateUtil;
import com.rubin.rpan.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Date;

public class ExpireTempFileCleaner implements RPanScheduleTask {

    private final static Logger log = LoggerFactory.getLogger(ExpireTempFileCleaner.class);

    private String tempFolderPath;

    private Integer expireDays;

    public ExpireTempFileCleaner(String tempFolderPath, Integer expireDays) {
        this.tempFolderPath = tempFolderPath;
        this.expireDays = expireDays;
    }

    @Override
    public void run() {
        File tempFolder = new File(tempFolderPath);
        if (!tempFolder.exists()) {
            log.error("临时文件目录不存在！路径为：" + tempFolderPath);
        }
        if (!tempFolder.isDirectory()) {
            log.error("非法临时文件目录！路径为：" + tempFolderPath);
        }
        log.info(getName() + "开始！");
        File[] tempFiles = tempFolder.listFiles();
        Date deadlineDate = DateUtil.beforeDays(expireDays);
        for (int i = 0; i < tempFiles.length; i++) {
            File tempFile = tempFiles[i];
            if (new Date(tempFile.lastModified()).before(deadlineDate)) {
                FileUtil.delete(tempFile);
                FileUtil.deleteChunks(FileUtil.getMd5FromTempFilename(tempFile.getName()));
                log.info("清理了文件：" + tempFile.getPath());
            }
        }
        log.info(getName() + "结束！");
    }

    @Override
    public String getName() {
        return "过期文件清理任务";
    }

}
