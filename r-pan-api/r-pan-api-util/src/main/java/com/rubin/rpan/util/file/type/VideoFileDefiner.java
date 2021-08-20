package com.rubin.rpan.util.file.type;

import java.util.Arrays;
import java.util.List;

/**
 * video文件定义类
 * Created by RubinChu on 2021/1/22 下午 3:01
 */
public class VideoFileDefiner implements FileTypeDefiner {

    /**
     * 文件类型标识
     */
    private static final Integer FILE_TYPE_CODE = 9;

    /**
     * 文件类型描述
     */
    private static final String FILE_TYPE_DESC = "VIDEO";

    /**
     * 获取该文件类型所包含的文件的后缀（后缀统一小写）
     *
     * @return
     */
    @Override
    public List<String> getIncludeSuffixes() {
        return Arrays.asList(".avi", ".3gp", ".mp4", ".flv", ".rmvb", ".mov");
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
