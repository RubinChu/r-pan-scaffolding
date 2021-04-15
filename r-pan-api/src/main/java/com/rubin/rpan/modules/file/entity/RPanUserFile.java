package com.rubin.rpan.modules.file.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户文件信息实体
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@Data
@Accessors(chain = true)
public class RPanUserFile implements Serializable {

    private static final long serialVersionUID = 781030562993386681L;

    /**
     * 自增主键
     */
    private Integer id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 父级id
     */
    private String parentId;

    /**
     * 文件id
     */
    private String fileId;

    /**
     * 物理文件id
     */
    private String realFileId;

    /**
     * 文件名称
     */
    private String filename;

    /**
     * 文件夹标识 0 否 1 是
     */
    private Integer folderFlag;

    /**
     * 是否是文件夹 0 否 1 是
     */
    private Integer integer;

    /**
     * 文件类型（1 文件 2 压缩文件 3 excel 4 word 5 pdf 6 txt 7 图片 8 音频 9 视频 10 ppt 11 源码文件）
     */
    private Integer type;

    /**
     * 删除标识（0 否 1 是）
     */
    private Boolean delFlag;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人
     */
    private String updateUser;

    /**
     * 更新时间
     */
    private Date updateTime;

}