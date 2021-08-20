package com.rubin.rpan.storage.oss;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.*;
import com.rubin.rpan.cache.Cache;
import com.rubin.rpan.cache.local.LocalCache;
import com.rubin.rpan.storage.StorageProcessor;
import com.rubin.rpan.util.DateUtil;
import com.rubin.rpan.util.FileUtil;
import com.rubin.rpan.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

@Component(value = "ossStorageProcessor")
@ConditionalOnProperty(prefix = "rpan.storage.processor", name = "type", havingValue = "com.rubin.rpan.storage.oss.OssStorageProcessor")
public class OssStorageProcessor implements StorageProcessor {

    private static final String SLASH = "/";
    private static final String TAG = "PartETag";
    private static final Integer TEN_THOUSAND_INT = 10000;
    private static final Integer ONE_INT = 1;

    @Autowired
    @Qualifier(value = "ossClient")
    private OSSClient ossClient;

    @Value("${rpan.storage.oss.bucket-name}")
    private String bucketName;

    @Value("${rpan.storage.oss.auto-create-bucket:true}")
    private Boolean autoCreateBucket;

    private Cache cache = new LocalCache();
    private Object LOCK = new Object();

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
        checkBucket();
        String filePath = getFilePath(name);
        ossClient.putObject(bucketName, filePath, inputStream);
        return filePath;
    }

    /**
     * 文件分片存储
     * 注意：分片上传支持并发随机上传分片
     *
     * @param inputStream 文件输入流
     * @param md5         文件唯一标识
     * @param chunks      分片总数 注意：本模式分片总数不得大于10000片
     * @param chunk       当前分片下标
     * @param size        文件总大小
     * @param chunkSize   分片文件大小
     * @param name        文件名称
     * @return null 代表未传完 有值代表已经传输完毕
     * @throws IOException
     */
    @Override
    public String storeWithChunk(InputStream inputStream, String md5, Integer chunks, Integer chunk, Long size, Long chunkSize, String name) throws IOException {
        checkBucket();
        if (chunks > TEN_THOUSAND_INT) {
            throw new RuntimeException("分片数超过了限制，分片数不得大于" + TEN_THOUSAND_INT);
        }
        synchronized (LOCK) {
            if (Objects.isNull(cache.get(md5))) {
                InitiateMultipartUpload(md5, name);
            }
        }
        doUploadMultipart(inputStream, md5, name, chunk, chunkSize);
        if (FileUtil.addChunkAndCheckAllDone(md5, chunks)) {
            completeMultipart(md5, name);
            String filePathKey = getFilePathKey(md5, name);
            String filePath = String.valueOf(cache.get(filePathKey));
            cache.delete(md5);
            cache.delete(filePathKey);
            cache.delete(getPartETagKey(md5));
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
        OSSObject ossObject = ossClient.getObject(bucketName, filePath);
        FileUtil.writeStreamToStreamNormal(ossObject.getObjectContent(), outputStream);
    }

    /**
     * 删除文件
     *
     * @param filePath 文件路径
     * @throws IOException
     */
    @Override
    public void delete(String filePath) throws IOException {
        ossClient.deleteObject(bucketName, filePath);
    }

    /*************************************************************私有*************************************************************/

    /**
     * 生成文件存储路径
     *
     * @param name
     * @return
     */
    private String getFilePath(String name) {
        return new StringBuffer(DateUtil.getTodayDayString()).append(SLASH).append(UUIDUtil.getUUID()).append(FileUtil.getFileSuffix(name)).toString();
    }

    /**
     * 生成文件路径缓存key
     *
     * @param md5
     * @param name
     * @return
     */
    private String getFilePathKey(String md5, String name) {
        return new StringBuffer(md5).append(SLASH).append(name).toString();
    }

    /**
     * 生成tag的key
     *
     * @param md5
     * @return
     */
    private String getPartETagKey(String md5) {
        return new StringBuffer(md5).append(SLASH).append(TAG).toString();
    }

    /**
     * 初始化分片请求 缓存分片id
     *
     * @param md5
     * @param name
     */
    private void InitiateMultipartUpload(String md5, String name) {
        String filePath = getFilePath(name);
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucketName, filePath);
        InitiateMultipartUploadResult initiateMultipartUploadResult = ossClient.initiateMultipartUpload(request);
        cache.put(md5, initiateMultipartUploadResult.getUploadId());
        cache.put(getFilePathKey(md5, name), filePath);
        cache.put(getPartETagKey(md5), new CopyOnWriteArrayList<>());
    }

    /**
     * 上传分片文件
     *
     * @param inputStream
     * @param md5
     * @param name
     * @param chunk
     * @param chunkSize
     */
    private void doUploadMultipart(InputStream inputStream, String md5, String name, Integer chunk, Long chunkSize) {
        UploadPartRequest uploadPartRequest = new UploadPartRequest();
        uploadPartRequest.setBucketName(bucketName);
        uploadPartRequest.setKey(String.valueOf(cache.get(getFilePathKey(md5, name))));
        uploadPartRequest.setUploadId(String.valueOf(cache.get(md5)));
        uploadPartRequest.setInputStream(inputStream);
        // 设置分片大小。除了最后一个分片没有大小限制，其他的分片最小为100 KB。
        uploadPartRequest.setPartSize(chunkSize);
        // 设置分片号。每一个上传的分片都有一个分片号，取值范围是1~10000，如果超出此范围，OSS将返回InvalidArgument错误码。
        uploadPartRequest.setPartNumber(chunk + ONE_INT);
        // 每个分片不需要按顺序上传，甚至可以在不同客户端上传，OSS会按照分片号排序组成完整的文件。
        UploadPartResult uploadPartResult = ossClient.uploadPart(uploadPartRequest);
        // 每次上传分片之后，OSS的返回结果包含PartETag。PartETag将被保存在partETags中。
        ((CopyOnWriteArrayList) cache.get(getPartETagKey(md5))).add(uploadPartResult.getPartETag());
    }

    /**
     * 完成分片上传
     *
     * @param md5
     * @param name
     */
    private void completeMultipart(String md5, String name) {
        String filePath = String.valueOf(cache.get(getFilePathKey(md5, name))),
                uploadId = String.valueOf(cache.get(md5));
        CopyOnWriteArrayList partETags = (CopyOnWriteArrayList) cache.get(getPartETagKey(md5));
        // 在执行完成分片上传操作时，需要提供所有有效的partETags。OSS收到提交的partETags后，会逐一验证每个分片的有效性。当所有的数据分片验证通过后，OSS将把这些分片组合成一个完整的文件。
        CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest(bucketName, filePath, uploadId, partETags);
        // 完成上传。
        ossClient.completeMultipartUpload(completeMultipartUploadRequest);
    }

    /**
     * 校验并根据需要创建bucket
     */
    private void checkBucket() {
        boolean bucketExist = ossClient.doesBucketExist(bucketName);
        if (!bucketExist && autoCreateBucket) {
            ossClient.createBucket(bucketName);
        }
        if (!bucketExist && !autoCreateBucket) {
            throw new RuntimeException("the bucket " + bucketName + " is not available!");
        }
    }

}
