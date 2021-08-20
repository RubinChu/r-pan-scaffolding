package com.rubin.rpan.util.io;

import java.io.InputStream;

/**
 * 资源读取类
 */
public class Resources {

    /**
     * 读取资源为输入流
     *
     * @param filePath
     * @return
     */
    public static InputStream readFileAsInputStream(String filePath) {
        return Resources.class.getClassLoader().getResourceAsStream(filePath);
    }

}
