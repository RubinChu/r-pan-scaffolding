package com.rubin.rpan.util;

import org.apache.commons.lang3.StringUtils;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 文件工具类
 * Created by RubinChu on 2021/1/22 下午 3:07
 */
public class FileUtil {

    private static final String KB_STR = "K";
    private static final String MB_STR = "M";
    private static final String GB_STR = "G";
    private static final Integer UNIT = 1024;
    private static final String FILE_SIZE_DESC_FORMAT = "%.2f";
    private static final String POINT_STR = ".";
    private static final Long ZERO_LONG = 0L;
    private static final Integer ZERO_INT = 0;
    private static final Integer ONE_INT = 1;
    private static final Integer MINUS_ONE_INT = -1;
    private static final String EMPTY_STR = "";
    private static final String SLASH = "/";
    private final static String TEMP_FOLDER_NAME = "temp";
    private static final String DEFAULT_ROOT_FILE_NAME = "rpan";
    private static final String COMMON_SEPARATOR = "__,__";

    private static Map<String, AtomicInteger> chunkNumContainer = new ConcurrentHashMap<>();

    /**
     * 添加分片并返回是否全部写入完毕
     *
     * @param md5
     * @param chunks
     * @return
     */
    public static boolean addChunkAndCheckAllDone(String md5, Integer chunks) {
        chunkNumContainer.putIfAbsent(md5, new AtomicInteger());
        int currentChunks = chunkNumContainer.get(md5).incrementAndGet();
        if (currentChunks == chunks.intValue()) {
            deleteChunks(md5);
            return true;
        }
        return false;
    }

    /**
     * 清除md5对应的文件记录
     *
     * @param md5
     * @return
     */
    public static void deleteChunks(String md5) {
        chunkNumContainer.remove(md5);
    }

    /**
     * 从临时文件名称获取md5
     *
     * @param tempFilename
     * @return
     */
    public static String getMd5FromTempFilename(String tempFilename) {
        if (StringUtils.isBlank(tempFilename)) {
            return StringUtils.EMPTY;
        }
        String[] tempFilenameArr = tempFilename.split(COMMON_SEPARATOR);
        return tempFilenameArr[tempFilenameArr.length - ONE_INT];
    }

    /**
     * 生成临时文件名称
     *
     * @param originFilename
     * @param md5
     * @return
     */
    public static String generateTempFilename(String originFilename, String md5) {
        return new StringBuffer(originFilename).append(COMMON_SEPARATOR).append(md5).toString();
    }

    /**
     * 生成临时文件路径
     *
     * @param tempFilePrefix
     * @param originFilename
     * @param md5
     * @return
     */
    public static String generateTempFilePath(String tempFilePrefix, String originFilename, String md5) {
        return new StringBuffer(tempFilePrefix).append(File.separator).append(generateTempFilename(originFilename, md5)).toString();
    }

    /**
     * 生成文件本地的保存路径
     *
     * @param filePrefix
     * @param originFilename
     * @return
     */
    public static String generateFilePath(String filePrefix, String originFilename) {
        return new StringBuffer(filePrefix)
                .append(File.separator)
                .append(DateUtil.getTodayDayString())
                .append(File.separator)
                .append(UUIDUtil.getUUID())
                .append(getFileSuffix(originFilename))
                .toString();
    }

    /**
     * 分块写入文件
     *
     * @param target
     * @param targetSize
     * @param src
     * @param srcSize
     * @param chunks
     * @param chunk
     * @throws IOException
     */
    public synchronized static void writeWithChunk(String target, Long targetSize, InputStream src, Long srcSize, Integer chunks, Integer chunk) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(target, "rw");
        randomAccessFile.setLength(targetSize);
        if (chunk == chunks - ONE_INT.intValue()) {
            randomAccessFile.seek(targetSize - srcSize);
        } else {
            randomAccessFile.seek(chunk * srcSize);
        }
        byte[] buf = new byte[1024];
        int len;
        while (MINUS_ONE_INT.intValue() != (len = src.read(buf))) {
            randomAccessFile.write(buf, ZERO_INT, len);
        }
        randomAccessFile.close();
    }

    /**
     * 获取输入流写入输出流
     *
     * @param fileInputStream
     * @param outputStream
     * @param size
     * @throws IOException
     */
    public static void writeFileToStream(FileInputStream fileInputStream, OutputStream outputStream, Long size) throws IOException {
        FileChannel fileChannel = fileInputStream.getChannel();
        WritableByteChannel writableByteChannel = Channels.newChannel(outputStream);
        fileChannel.transferTo(ZERO_LONG, size, writableByteChannel);
        outputStream.flush();
        fileInputStream.close();
        outputStream.close();
        fileChannel.close();
        writableByteChannel.close();
    }

    /**
     * 获取输入流写入输出流
     *
     * @param inputStream
     * @param fileOutputStream
     * @param size
     * @throws IOException
     */
    public static void writeStreamToFile(InputStream inputStream, FileOutputStream fileOutputStream, Long size) throws IOException {
        FileChannel fileChannel = fileOutputStream.getChannel();
        ReadableByteChannel readableByteChannel = Channels.newChannel(inputStream);
        fileChannel.transferFrom(readableByteChannel, ZERO_LONG, size);
        fileOutputStream.flush();
        inputStream.close();
        fileOutputStream.close();
        fileChannel.close();
        readableByteChannel.close();
    }

    /**
     * 传统流对流传输
     *
     * @param inputStream
     * @param outputStream
     * @throws IOException
     */
    public static void writeStreamToStreamNormal(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) != MINUS_ONE_INT.intValue()) {
            outputStream.write(buffer, ZERO_INT, len);
        }
        outputStream.flush();
        inputStream.close();
        outputStream.close();
    }

    /**
     * 获取文件大小字符串
     *
     * @param size
     * @return
     */
    public static String getFileSizeDesc(long size) {
        double fileSize = (double) size;
        String fileSizeSuffix = KB_STR;
        fileSize = fileSize / UNIT;
        if (fileSize >= UNIT) {
            fileSize = fileSize / UNIT;
            fileSizeSuffix = MB_STR;
        }
        if (fileSize >= UNIT) {
            fileSize = fileSize / UNIT;
            fileSizeSuffix = GB_STR;
        }
        return String.format(FILE_SIZE_DESC_FORMAT, fileSize) + fileSizeSuffix;
    }

    /**
     * 通过文件名获取文件后缀
     *
     * @param filename
     * @return
     */
    public static String getFileSuffix(String filename) {
        if (StringUtils.isBlank(filename) || (filename.indexOf(POINT_STR) == MINUS_ONE_INT)) {
            return EMPTY_STR;
        }
        return filename.substring(filename.lastIndexOf(POINT_STR)).toLowerCase();
    }

    /**
     * 获取文件扩展名
     *
     * @param filename
     * @return
     */
    public static String getFileExtName(String filename) {
        String fileSuffix = getFileSuffix(filename);
        if (StringUtils.isBlank(fileSuffix)) {
            return fileSuffix;
        }
        return fileSuffix.substring(ONE_INT);
    }

    /**
     * 通过文件路径获取文件名称
     *
     * @param filePath
     * @return
     */
    public static String getFilename(String filePath) {
        String filename = EMPTY_STR;
        if (StringUtils.isBlank(filePath)) {
            return filename;
        }
        if (filePath.indexOf(File.separator) != MINUS_ONE_INT) {
            filename = filePath.substring(filePath.lastIndexOf(File.separator) + ONE_INT);
        }
        if (filePath.indexOf(SLASH) != MINUS_ONE_INT) {
            filename = filePath.substring(filePath.lastIndexOf(SLASH) + ONE_INT);
        }
        return filename;
    }

    /**
     * 获取文件的content-type
     *
     * @param filePath
     * @return
     */
    public static String getContentType(String filePath) {
        //利用nio提供的类判断文件ContentType
        File file = new File(filePath);
        String contentType = null;
        try {
            contentType = Files.probeContentType(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //若失败则调用另一个方法进行判断
        if (StringUtils.isBlank(contentType)) {
            contentType = new MimetypesFileTypeMap().getContentType(file);
        }
        return contentType;
    }

    /**
     * 创建文件夹
     *
     * @param folderPath
     */
    public static void createFolder(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    /**
     * 生成临时文件目录路径
     *
     * @param basePath
     * @return
     */
    public static String generateTempFolderPath(String basePath) {
        return basePath + File.separator + TEMP_FOLDER_NAME;
    }

    /**
     * 生成临时文件目录路径
     *
     * @return
     */
    public static String generateDefaultRootFolderPath() {
        return System.getProperty("user.home") + File.separator + DEFAULT_ROOT_FILE_NAME;
    }

    /**
     * 删除物理文件
     *
     * @param file
     */
    public static void delete(File file) {
        file.delete();
    }

    /**
     * 移动物理文件
     *
     * @param tempFilePath
     * @param realPath
     */
    public static void moveFile(String tempFilePath, String realPath) throws IOException {
        File source = new File(tempFilePath),
                target = new File(realPath);
        if (!target.getParentFile().exists()) {
            target.getParentFile().mkdirs();
        }
        Files.move(source.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

}
