package com.rubin.rpan.storage;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 存储处理器选择器
 */
@Component(value = "storageProcessorSelector")
public class StorageProcessorSelector implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    private StorageProcessor storageProcessor;

    @Value("${rpan.storage.processor.type}")
    private Class<? extends StorageProcessor> storageProcessorType;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        if (Objects.isNull(storageProcessorType)) {
            throw new RuntimeException("the storage processor type can not be null!<rpan.storage.processor.type>");
        }
        this.storageProcessor = this.applicationContext.getBean(storageProcessorType);
        if (Objects.isNull(this.storageProcessor)) {
            throw new RuntimeException("the storage processor type:" + storageProcessorType.getSimpleName() + " can not be found!");
        }
    }

    /**
     * 选择StorageProcessor实现类
     *
     * @return
     */
    public StorageProcessor select() {
        return storageProcessor;
    }

}
