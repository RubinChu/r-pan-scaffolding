package com.rubin.rpan.modules.share.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户分享实体
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@Data
@Accessors(chain = true)
public class RPanShare implements Serializable {

    private static final long serialVersionUID = -4890574785223788444L;

    /**
     * 主键
     */
    private Integer id;

    /**
     * 分享id
     */
    private String shareId;

    /**
     * 分项名称
     */
    private String shareName;

    /**
     * 分享类型（0 有提取码）
     */
    private Integer shareType;

    /**
     * 分享类型（0 永久有效；1 7天有效；2 30天有效）
     */
    private Integer shareDayType;

    /**
     * 分享有效天数（永久有效为-1）
     */
    private Integer shareDay;

    /**
     * 分享结束时间
     */
    private Date shareEndTime;

    /**
     * 分享链接地址
     */
    private String shareUrl;

    /**
     * 分享提取码
     */
    private String shareCode;

    /**
     * 分享状态（0 正常；1 有文件被删除）
     */
    private Integer shareStatus;

    /**
     * 分享创建人
     */
    private String createUser;

    /**
     * 创建时间
     */
    private Date createTime;

}