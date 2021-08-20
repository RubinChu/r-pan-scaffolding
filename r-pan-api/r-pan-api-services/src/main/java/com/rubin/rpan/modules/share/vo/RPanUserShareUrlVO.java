package com.rubin.rpan.modules.share.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * 分享链接返回实体
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@ApiModel(value = "分享链接返回实体")
public class RPanUserShareUrlVO implements Serializable {

    private static final long serialVersionUID = -2192569016493000246L;

    public RPanUserShareUrlVO() {
    }

    /**
     * 分享id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("分享id")
    private Long shareId;

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

    public Long getShareId() {
        return shareId;
    }

    public void setShareId(Long shareId) {
        this.shareId = shareId;
    }

    public String getShareName() {
        return shareName;
    }

    public void setShareName(String shareName) {
        this.shareName = shareName;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getShareCode() {
        return shareCode;
    }

    public void setShareCode(String shareCode) {
        this.shareCode = shareCode;
    }

    public Integer getShareStatus() {
        return shareStatus;
    }

    public void setShareStatus(Integer shareStatus) {
        this.shareStatus = shareStatus;
    }

    public Integer getShareType() {
        return shareType;
    }

    public void setShareType(Integer shareType) {
        this.shareType = shareType;
    }

    public Integer getShareDayType() {
        return shareDayType;
    }

    public void setShareDayType(Integer shareDayType) {
        this.shareDayType = shareDayType;
    }

    public Date getShareEndTime() {
        return shareEndTime;
    }

    public void setShareEndTime(Date shareEndTime) {
        this.shareEndTime = shareEndTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RPanUserShareUrlVO that = (RPanUserShareUrlVO) o;
        return Objects.equals(shareId, that.shareId) &&
                Objects.equals(shareName, that.shareName) &&
                Objects.equals(shareUrl, that.shareUrl) &&
                Objects.equals(shareCode, that.shareCode) &&
                Objects.equals(shareStatus, that.shareStatus) &&
                Objects.equals(shareType, that.shareType) &&
                Objects.equals(shareDayType, that.shareDayType) &&
                Objects.equals(shareEndTime, that.shareEndTime) &&
                Objects.equals(createTime, that.createTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shareId, shareName, shareUrl, shareCode, shareStatus, shareType, shareDayType, shareEndTime, createTime);
    }

    @Override
    public String toString() {
        return "RPanUserShareUrlVO{" +
                "shareId=" + shareId +
                ", shareName='" + shareName + '\'' +
                ", shareUrl='" + shareUrl + '\'' +
                ", shareCode='" + shareCode + '\'' +
                ", shareStatus=" + shareStatus +
                ", shareType=" + shareType +
                ", shareDayType=" + shareDayType +
                ", shareEndTime=" + shareEndTime +
                ", createTime=" + createTime +
                '}';
    }

}
