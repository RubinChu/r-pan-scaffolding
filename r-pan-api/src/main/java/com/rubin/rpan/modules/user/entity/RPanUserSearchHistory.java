package com.rubin.rpan.modules.user.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * 用户搜索历史实体类
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
public class RPanUserSearchHistory implements Serializable {

    private static final long serialVersionUID = 8448951580009868978L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 搜索文本
     */
    private String searchContent;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    public RPanUserSearchHistory() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getSearchContent() {
        return searchContent;
    }

    public void setSearchContent(String searchContent) {
        this.searchContent = searchContent;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RPanUserSearchHistory that = (RPanUserSearchHistory) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(searchContent, that.searchContent) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(updateTime, that.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, searchContent, createTime, updateTime);
    }

    @Override
    public String toString() {
        return "RPanUserSearchHistory{" +
                "id=" + id +
                ", userId=" + userId +
                ", searchContent='" + searchContent + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }

}