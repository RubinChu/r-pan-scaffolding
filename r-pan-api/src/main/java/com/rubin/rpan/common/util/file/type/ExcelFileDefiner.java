package com.rubin.rpan.common.util.file.type;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * excel文件定义类
 * Created by RubinChu on 2021/1/22 下午 3:01
 */
@Component(value = "excelFileDefiner")
public class ExcelFileDefiner implements FileTypeDefiner {

    /**
     * 文件类型标识
     */
    private static final Integer FILE_TYPE_CODE = 3;

    /**
     * 文件类型描述
     */
    private static final String FILE_TYPE_DESC = "EXCEL";

    /**
     * 获取该文件类型所包含的文件的后缀（后缀统一小写）
     *
     * @return
     */
    @Override
    public List<String> getIncludeSuffixes() {
        return Arrays.asList(".xlsx", ".xls");
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
