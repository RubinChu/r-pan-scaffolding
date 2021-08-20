package com.rubin.rpan.storage.local;

import com.rubin.rpan.storage.StorageProcessor;
import com.rubin.rpan.storage.local.config.LocalStorageConfig;
import com.rubin.rpan.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.*;

@Component(value = "localStorageProcessor")
@ConditionalOnProperty(prefix = "rpan.storage.processor", name = "type", havingValue = "com.rubin.rpan.storage.local.LocalStorageProcessor")
public class LocalStorageProcessor implements StorageProcessor {

    @Autowired
    @Qualifier(value = "localStorageConfig")
    private LocalStorageConfig localStorageConfig;

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
        String filePath = FileUtil.generateFilePath(localStorageConfig.getRootFilePath(), name);
        File targetFile = new File(filePath);
        if (!targetFile.getParentFile().exists()) {
            targetFile.getParentFile().mkdirs();
        }
        targetFile.createNewFile();
        FileUtil.writeStreamToFile(inputStream, new FileOutputStream(targetFile), size);
        return filePath;
    }

    /**
     * 文件分片存储
     * 注意：此方法可以保证并发分片上传不出问题
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
        String tempFilePath = FileUtil.generateTempFilePath(localStorageConfig.getTempPath(), name, md5);
        FileUtil.writeWithChunk(tempFilePath,
                size,
                inputStream,
                chunkSize,
                chunks,
                chunk);
        if (FileUtil.addChunkAndCheckAllDone(md5, chunks)) {
            String filePath = FileUtil.generateFilePath(localStorageConfig.getRootFilePath(), name);
            FileUtil.moveFile(tempFilePath, filePath);
            return filePath;
        }
        return null;
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
        File file = new File(filePath);
        FileUtil.writeFileToStream(new FileInputStream(file), outputStream, file.length());
    }

    /**
     * 删除文件
     *
     * @param filePath 文件路径
     * @throws IOException
     */
    @Override
    public void delete(String filePath) throws IOException {
        FileUtil.delete(new File(filePath));
    }

}
