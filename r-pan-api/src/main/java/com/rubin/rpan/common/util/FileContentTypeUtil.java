package com.rubin.rpan.common.util;

import com.google.common.collect.Maps;
import com.rubin.rpan.common.constant.CommonConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * 文件内容类型工具类
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
public class FileContentTypeUtil {

    private static final Logger log = LoggerFactory.getLogger(FileContentTypeUtil.class);

    /**
     * 文件内容类型容器
     */
    private static final Map<String, String> fileContentTypeMap = Maps.newHashMap();

    /**
     * 配置文件路径
     */
    private static final String CONFIG_FILE_PATH = "config/file-content-type.properties";

    /**
     * 默认文件内容类型
     */
    private static final String DEFAULT_FILE_CONTENT_TYPE = "application/octet-stream";

    static {
        Resource resource = new ClassPathResource(CONFIG_FILE_PATH);
        InputStream inputStream = null;
        try {
            inputStream = resource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Enumeration<?> enumeration = properties.propertyNames();
        while (enumeration.hasMoreElements()) {
            String key = enumeration.nextElement().toString();
            fileContentTypeMap.put(key, properties.getProperty(key));
        }
        log.debug("文件预览类型加载完毕！");
    }

    /**
     * 通过文件名获取该文件对应的预览类型
     *
     * @param filename
     * @return
     */
    public static String get(String filename) {
        if (filename.indexOf(CommonConstant.POINT_STR) == CommonConstant.MINUS_ONE_INT) {
            return DEFAULT_FILE_CONTENT_TYPE;
        }
        String key = fileContentTypeMap.keySet().stream().filter(fileSuffix -> filename.endsWith(fileSuffix)).findFirst().orElse(CommonConstant.EMPTY_STR);
        if (Objects.equals(key, CommonConstant.EMPTY_STR)) {
            return DEFAULT_FILE_CONTENT_TYPE;
        }
        return fileContentTypeMap.get(key);
    }

}
