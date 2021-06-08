package com.rubin.rpan.modules.share.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * 用户分享文件实体
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
public class RPanShareFile implements Serializable {

    private static final long serialVersionUID = -3950411320080930219L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 分享id
     */
    private Long shareId;

    /**
     * 文件id
     */
    private Long fileId;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    public RPanShareFile() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getShareId() {
        return shareId;
    }

    public void setShareId(Long shareId) {
        this.shareId = shareId;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
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
        RPanShareFile that = (RPanShareFile) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(shareId, that.shareId) &&
                Objects.equals(fileId, that.fileId) &&
                Objects.equals(createUser, that.createUser) &&
                Objects.equals(createTime, that.createTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, shareId, fileId, createUser, createTime);
    }

    @Override
    public String toString() {
        return "RPanShareFile{" +
                "id=" + id +
                ", shareId=" + shareId +
                ", fileId=" + fileId +
                ", createUser=" + createUser +
                ", createTime=" + createTime +
                '}';
    }

}