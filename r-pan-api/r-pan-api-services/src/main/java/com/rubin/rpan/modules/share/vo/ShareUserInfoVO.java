package com.rubin.rpan.modules.share.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * 分享链接详情返回实体
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@ApiModel(value = "分享链接用户详情返回实体")
public class ShareUserInfoVO implements Serializable {

    private static final long serialVersionUID = -3389565809049484829L;

    public ShareUserInfoVO() {
    }

    /**
     * 分享者id
     */
    @ApiModelProperty("分享者id")
    private String userId;

    /**
     * 分享者昵称
     */
    @ApiModelProperty("分享者昵称")
    private String username;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShareUserInfoVO that = (ShareUserInfoVO) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username);
    }

    @Override
    public String toString() {
        return "ShareUserInfoVO{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

}
