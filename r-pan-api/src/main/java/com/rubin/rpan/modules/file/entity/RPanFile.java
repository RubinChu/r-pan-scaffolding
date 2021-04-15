package com.rubin.rpan.modules.file.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 物理文件信息实体
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@Data
@Accessors(chain = true)
public class RPanFile implements Serializable {

    private static final long serialVersionUID = -717520378331457192L;

    /**
     * 自增主键
     */
    private Integer id;

    /**
     * 文件id
     */
    private String fileId;

    /**
     * 文件名称
     */
    private String filename;

    /**
     * 真实物理存放路径
     */
    private String realPath;

    /**
     * 文件大小
     */
    private String fileSize;

    /**
     * 文件大小显示文案
     */
    private String fileSizeDesc;

    /**
     * 文件后缀
     */
    private String fileSuffix;

    /**
     * 文件预览Content-Type响应头的值
     */
    private String filePreviewContentType;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 创建时间
     */
    private Date createTime;
}