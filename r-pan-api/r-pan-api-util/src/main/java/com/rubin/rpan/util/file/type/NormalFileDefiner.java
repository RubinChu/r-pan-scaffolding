package com.rubin.rpan.util.file.type;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * 普通文件定义类
 * Created by RubinChu on 2021/1/22 下午 3:01
 */
public class NormalFileDefiner implements FileTypeDefiner {

    /**
     * 文件类型标识
     */
    private static final Integer FILE_TYPE_CODE = 1;

    /**
     * 文件类型描述
     */
    private static final String FILE_TYPE_DESC = "NORMAL_FILE";

    /**
     * 判断是否匹配该类型
     *
     * @param filename
     * @return
     */
    @Override
    public boolean isMatched(String filename) {
        return true;
    }

    /**
     * 获取该文件类型所包含的文件的后缀（后缀统一小写）
     *
     * @return
     */
    @Override
    public List<String> getIncludeSuffixes() {
        return Lists.newArrayList();
    }

    /**
     * 获取文件类型标识
     *
     * @return
     */
    @Override
    public Integer getFileTypeCode() {
        return FILE_TYPE_CODE;
    }

    /**
     * 获取该文件类型的描述
     *
     * @return
     */
    @Override
    public String getFileTypeDesc() {
        return FILE_TYPE_DESC;
    }

    /**
     * 获取该定义器的顺序 从大到小倒序
     *
     * @return
     */
    @Override
    public Integer getOrder() {
        return FILE_TYPE_CODE;
    }

}
