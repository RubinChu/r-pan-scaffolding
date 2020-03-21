package com.rubin.rpan.common.base;

import com.rubin.rpan.common.constants.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @Description Service基础父类
 * @auther rubin
 * @create 2019-09-20 9:52
 */
@Service
public class BaseService {

    // 文件根目录
    @Value("${file.root.path}")
    public String rootPath;

    /**
     * 返回文件的真实路径
     *
     * @param filePath
     * @param fileName
     * @return
     */
    public String getFileRealPathStr(String filePath, String fileName) {
        return new StringBuffer()
                .append(rootPath)
                .append(filePath)
                .append(Constants.VIRGULE_STR)
                .append(fileName)
                .toString();
    }

}
