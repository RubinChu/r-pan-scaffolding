package com.rubin.rpan.util.file.type;

import com.rubin.rpan.util.FileUtil;

import java.util.List;
import java.util.Objects;

/**
 * 文件类型定义接口
 * Created by RubinChu on 2021/1/22 下午 2:44.
 */
public interface FileTypeDefiner {

    public static final String EMPTY_STR = "";

    /**
     * 判断是否匹配该类型
     *
     * @param filename
     * @return
     */
    default boolean isMatched(String filename) {
        String fileSuffix = FileUtil.getFileSuffix(filename);
        if (Objects.equals(EMPTY_STR, fileSuffix)) {
            return false;
        }
        return getIncludeSuffixes().contains(fileSuffix);
    }

    /**
     * 获取该文件类型所包含的文件的后缀（后缀统一大写）
     *
     * @return
     */
    List<String> getIncludeSuffixes();

    /**
     * 获取文件类型标识
     *
     * @return
     */
    Integer getFileTypeCode();

    /**
     * 获取该文件类型的描述
     *
     * @return
     */
    String getFileTypeDesc();

    /**
     * 获取该定义器的顺序 从大到小倒序
     *
     * @return
     */
    Integer getOrder();

}
