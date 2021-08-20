package com.rubin.rpan.util.file.type;

import java.util.Arrays;
import java.util.List;

/**
 * 压缩文件定义类
 * Created by RubinChu on 2021/1/22 下午 3:01
 */
public class ArchiveFileDefiner implements FileTypeDefiner {

    /**
     * 文件类型标识
     */
    private static final Integer FILE_TYPE_CODE = 2;

    /**
     * 文件类型描述
     */
    private static final String FILE_TYPE_DESC = "ARCHIVE";

    /**
     * 获取该文件类型所包含的文件的后缀（后缀统一小写）
     *
     * @return
     */
    @Override
    public List<String> getIncludeSuffixes() {
        return Arrays.asList(".rar", ".zip", ".cab", ".iso", ".jar", ".ace", ".7z", ".tar", ".gz", ".arj", ".lah", ".uue", ".bz2", ".z", ".war");
    }

    /**
     * 通过文件名获取文件类型标识
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
