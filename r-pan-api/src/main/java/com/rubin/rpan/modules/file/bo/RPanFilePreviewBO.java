package com.rubin.rpan.modules.file.bo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 文件预览BO
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@Data
@Accessors(chain = true)
public class RPanFilePreviewBO implements Serializable {

    private static final long serialVersionUID = -39827772374581121L;

    /**
     * 文件真实路径
     */
    private String realPath;

    /**
     * 文件预览Content-Type响应头的值
     */
    private String filePreviewContentType;

}
