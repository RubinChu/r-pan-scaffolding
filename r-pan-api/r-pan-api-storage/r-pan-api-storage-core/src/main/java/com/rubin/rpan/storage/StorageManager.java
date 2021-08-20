package com.rubin.rpan.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 存储管理器
 */
@Component(value = "storageManager")
public class StorageManager implements StorageProcessor {

    @Autowired
    @Qualifier(value = "storageProcessorSelector")
    private StorageProcessorSelector storageProcessorSelector;

    /**
     * 文件存储
     *
     * @param inputStream 文件输入流
     * @param size        文件大小
     * @param name        文件原名
     * @return 文件存储路径
     * @throws IOException
     */
    @Override
    public String store(InputStream inputStream, Long size, String name) throws IOException {
        return storageProcessorSelector.select().store(inputStream, size, name);
    }

    /**
     * 文件分片存储
     *
     * @param inputStream 文件输入流
     * @param md5         文件唯一标识
     * @param chunks      分片总数
     * @param chunk       当前分片下标 从0开始
     * @param size        文件总大小
     * @param chunkSize   分片文件大小
     * @param name        文件名称
     * @return null 代表未传完 有值代表已经传输完毕
     * @throws IOException
     */
    @Override
    public String storeWithChunk(InputStream inputStream, String md5, Integer chunks, Integer chunk, Long size, Long chunkSize, String name) throws IOException {
        return storageProcessorSelector.select().storeWithChunk(inputStream, md5, chunks, chunk, size, chunkSize, name);
    }

    /**
     * 读取文件为输入流
     *
     * @param filePath     文件路径
     * @param outputStream 输出流
     * @throws IOException
     */
    @Override
    public void read2OutputStream(String filePath, OutputStream outputStream) throws IOException {
        storageProcessorSelector.select().read2OutputStream(filePath, outputStream);
    }

    /**
     * 删除文件
     *
     * @param filePath 文件路径
     * @throws IOException
     */
    @Override
    public void delete(String filePath) throws IOException {
        storageProcessorSelector.select().delete(filePath);
    }

}
