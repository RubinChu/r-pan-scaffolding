package com.rubin.rpan.modules.share.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 分享链接返回实体
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "分享链接返回实体")
public class RPanUserShareUrlVO implements Serializable {

    private static final long serialVersionUID = -2192569016493000246L;

    /**
     * 分享id
     */
    @ApiModelProperty("分享id")
    private String shareId;

    /**
     * 分享名称
     */
    @ApiModelProperty("分享名称")
    private String shareName;

    /**
     * 分享链接
     */
    @ApiModelProperty("分享链接")
    private String shareUrl;

    /**
     * 分享码
     */
    @ApiModelProperty("分享码")
    private String shareCode;

    /**
     * 分享状态 0 正常 1 有文件被删除
     */
    @ApiModelProperty("分享状态 0 正常 1 有文件被删除")
    private Integer shareStatus;

    /**
     * 分享类型（0 有提取码）
     */
    @ApiModelProperty("分享类型（0 有提取码）")
    private Integer shareType;

    /**
     * 分享类型（0 永久有效；1 7天有效；2 30天有效）
     */
    @ApiModelProperty("分享类型（0 永久有效；1 7天有效；2 30天有效）")
    private Integer shareDayType;

    /**
     * 分享结束时间
     */
    @ApiModelProperty("分享结束时间")
    private Date shareEndTime;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

}
