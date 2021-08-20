package com.rubin.rpan.schedule;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@SpringBootConfiguration
public class RPanScheduleConfig {

    @Bean(value = "threadPoolTaskScheduler")
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler executor = new ThreadPoolTaskScheduler();
        return executor;
    }

}
