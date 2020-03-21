package com.rubin.rpan.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by rubin on 2019/9/7.
 * 文件信息实体
 */

@Data
public class FileInfo implements Serializable {

    private static final long serialVersionUID = 7586670894647844771L;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件类型 详情参考静态类文件类型
     */
    private Integer type;

    /**
     * 文件展示
     */
    private String fileShowPath;

    /**
     * 是否选中 0 否 1 是
     */
    private Integer isChecked;

}
