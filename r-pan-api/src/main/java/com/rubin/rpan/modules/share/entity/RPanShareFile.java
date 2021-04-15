package com.rubin.rpan.modules.share.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户分享文件实体
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@Data
@Accessors(chain = true)
public class RPanShareFile implements Serializable {

    private static final long serialVersionUID = -3950411320080930219L;

    /**
     * 主键
     */
    private Integer id;

    /**
     * 分享id
     */
    private String shareId;

    /**
     * 文件id
     */
    private String fileId;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 创建时间
     */
    private Date createTime;

}