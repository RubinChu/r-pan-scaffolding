package com.rubin.rpan.storage.fastdfs;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.DefaultAppendFileStorageClient;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.rubin.rpan.cache.Cache;
import com.rubin.rpan.cache.local.LocalCache;
import com.rubin.rpan.storage.StorageProcessor;
import com.rubin.rpan.util.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Component(value = "fastDFSStorageProcessor")
@ConditionalOnProperty(prefix = "rpan.storage.processor", name = "type", havingValue = "com.rubin.rpan.storage.fastdfs.FastDFSStorageProcessor")
public class FastDFSStorageProcessor implements StorageProcessor {

    private static final Integer ZERO_INT = 0;
    private static final Integer ONE_INT = 1;
    private static final String SLASH = "/";

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @Autowired
    private DefaultAppendFileStorageClient defaultAppendFileStorageClient;

    @Value("${rpan.storage.fdfs.group:group1}")
    private String group;

    private Cache cache = new LocalCache();

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
        StorePath storePath = fastFileStorageClient.uploadFile(group, inputStream, size, FileUtil.getFileExtName(name));
        return storePath.getFullPath();
    }

    /**
     * 文件分片存储
     * 注意：此方法不保证并发分片上传不出问题，所以使用此模式，要有序单线程分片上传
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
        String fileExtName = FileUtil.getFileExtName(name);
        StorePath storePath;
        if (chunk.equals(ZERO_INT)) {
            storePath = defaultAppendFileStorageClient.uploadAppenderFile(group, inputStream, chunkSize, fileExtName);
            cache.put(md5, storePath.getPath());
        } else {
            Long offset;
            if (chunk == chunks - 1) {
                offset = size - chunkSize;
            } else {
                offset = chunk * chunkSize;
            }
            defaultAppendFileStorageClient.modifyFile(group, String.valueOf(cache.get(md5)), inputStream, chunkSize, offset);
        }
        if (FileUtil.addChunkAndCheckAllDone(md5, chunks)) {
            String filePath = String.valueOf(group).concat(SLASH).concat(String.valueOf(cache.get(md5)));
            cache.delete(md5);
            return filePath;
        }
        return null;
    }

    /**
     * 读取文件进输出流
     *
     * @param filePath     文件路径
     * @param outputStream 输出流
     * @throws IOException
     */
    @Override
    public void read2OutputStream(String filePath, OutputStream outputStream) throws IOException {
        String group = filePath.substring(ZERO_INT, filePath.indexOf(SLASH));
        String path = filePath.substring(filePath.indexOf(SLASH) + ONE_INT);
        DownloadByteArray downloadByteArray = new DownloadByteArray();
        byte[] bytes = fastFileStorageClient.downloadFile(group, path, downloadByteArray);
        outputStream.write(bytes);
        outputStream.flush();
        outputStream.close();
    }

    /**
     * 删除文件
     *
     * @param filePath 文件路径
     * @throws IOException
     */
    @Override
    public void delete(String filePath) throws IOException {
        fastFileStorageClient.deleteFile(filePath);
    }

}
