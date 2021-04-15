package com.rubin.rpan.modules.file.bo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 文件下载BO
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@Data
@Accessors(chain = true)
public class RPanFileDownloadBO implements Serializable {

    private static final long serialVersionUID = -2810054846960460925L;

    /**
     * 文件展示名称
     */
    private String filename;

    /**
     * 文件真实路径
     */
    private String realPath;

}
