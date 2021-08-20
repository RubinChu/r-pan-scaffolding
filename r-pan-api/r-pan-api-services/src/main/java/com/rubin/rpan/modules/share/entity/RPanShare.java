package com.rubin.rpan.modules.share.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * 用户分享实体
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
public class RPanShare implements Serializable {

    private static final long serialVersionUID = -4890574785223788444L;

    /**
     * 分享id
     */
    private Long shareId;

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
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    public RPanShare() {
    }

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

    public Integer getShareDay() {
        return shareDay;
    }

    public void setShareDay(Integer shareDay) {
        this.shareDay = shareDay;
    }

    public Date getShareEndTime() {
        return shareEndTime;
    }

    public void setShareEndTime(Date shareEndTime) {
        this.shareEndTime = shareEndTime;
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

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
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
        RPanShare rPanShare = (RPanShare) o;
        return Objects.equals(shareId, rPanShare.shareId) &&
                Objects.equals(shareName, rPanShare.shareName) &&
                Objects.equals(shareType, rPanShare.shareType) &&
                Objects.equals(shareDayType, rPanShare.shareDayType) &&
                Objects.equals(shareDay, rPanShare.shareDay) &&
                Objects.equals(shareEndTime, rPanShare.shareEndTime) &&
                Objects.equals(shareUrl, rPanShare.shareUrl) &&
                Objects.equals(shareCode, rPanShare.shareCode) &&
                Objects.equals(shareStatus, rPanShare.shareStatus) &&
                Objects.equals(createUser, rPanShare.createUser) &&
                Objects.equals(createTime, rPanShare.createTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shareId, shareName, shareType, shareDayType, shareDay, shareEndTime, shareUrl, shareCode, shareStatus, createUser, createTime);
    }

    @Override
    public String toString() {
        return "RPanShare{" +
                "shareId=" + shareId +
                ", shareName='" + shareName + '\'' +
                ", shareType=" + shareType +
                ", shareDayType=" + shareDayType +
                ", shareDay=" + shareDay +
                ", shareEndTime=" + shareEndTime +
                ", shareUrl='" + shareUrl + '\'' +
                ", shareCode='" + shareCode + '\'' +
                ", shareStatus=" + shareStatus +
                ", createUser=" + createUser +
                ", createTime=" + createTime +
                '}';
    }

}