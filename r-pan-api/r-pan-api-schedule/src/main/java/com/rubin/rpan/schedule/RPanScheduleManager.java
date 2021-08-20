package com.rubin.rpan.schedule;

import com.rubin.rpan.cache.Cache;
import com.rubin.rpan.cache.local.LocalCache;
import com.rubin.rpan.util.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.ScheduledFuture;

/**
 * 定时任务调度器
 */
@Component(value = "rPanScheduleManager")
public class RPanScheduleManager {

    private final static Logger log = LoggerFactory.getLogger(RPanScheduleManager.class);

    @Autowired
    @Qualifier(value = "threadPoolTaskScheduler")
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    private Cache cache = new LocalCache();

    /**
     * 启动定时任务
     *
     * @param rPanScheduleTask
     * @param cron
     */
    public String startTask(RPanScheduleTask rPanScheduleTask, String cron) {
        ScheduledFuture<?> scheduledFuture = threadPoolTaskScheduler.schedule(rPanScheduleTask, new CronTrigger(cron));
        String key = UUIDUtil.getUUID();
        RPanScheduleTaskHolder rPanScheduleTaskHolder = new RPanScheduleTaskHolder(rPanScheduleTask, scheduledFuture);
        cache.put(key, rPanScheduleTaskHolder);
        log.info("{}启动成功！唯一标识为{}", rPanScheduleTask.getName(), key);
        return key;
    }

    /**
     * 停止定时任务
     *
     * @param key
     */
    public void stopTask(String key) {
        if (StringUtils.isBlank(key)) {
            return;
        }
        Object cacheValue = cache.get(key);
        if (Objects.isNull(cacheValue)) {
            return;
        }
        ((RPanScheduleTaskHolder) cacheValue).getScheduledFuture().cancel(true);
        log.info("{}停止成功！唯一标识为{}", ((RPanScheduleTaskHolder) cacheValue).getrPanScheduleTask().getName(), key);
    }

    /**
     * 变更任务时间
     *
     * @param key
     * @param cron
     * @return
     */
    public String changeTask(String key, String cron) {
        if (StringUtils.isBlank(key)) {
            throw new RuntimeException(key + "标识不存在");
        }
        Object cacheValue = cache.get(key);
        if (Objects.isNull(cacheValue)) {
            throw new RuntimeException(key + "标识不存在");
        }
        stopTask(key);// 先停止，在开启.
        return startTask(((RPanScheduleTaskHolder) cacheValue).getrPanScheduleTask(), cron);
    }


}
