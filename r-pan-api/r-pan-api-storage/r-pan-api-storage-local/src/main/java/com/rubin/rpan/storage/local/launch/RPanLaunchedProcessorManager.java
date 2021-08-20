package com.rubin.rpan.storage.local.launch;

import com.rubin.rpan.storage.StorageProcessor;
import com.rubin.rpan.storage.local.LocalStorageProcessor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 初始化处理器
 */
@Component(value = "rPanInitProcessorManager")
@ConditionalOnProperty(prefix = "rpan.storage.processor", name = "type", havingValue = "com.rubin.rpan.storage.local.LocalStorageProcessor")
public class RPanLaunchedProcessorManager implements InitializingBean {

    @Autowired
    private List<RPanLaunchedProcessor> rPanLaunchedProcessors;

    @Value("${rpan.storage.processor.type}")
    private Class<? extends StorageProcessor> storageProcessorType;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (CollectionUtils.isNotEmpty(rPanLaunchedProcessors) && storageProcessorType.equals(LocalStorageProcessor.class)) {
            rPanLaunchedProcessors.forEach(RPanLaunchedProcessor::process);
        }
    }

}
