package com.rubin.rpan.modules.user.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * 用户返回实体
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@ApiModel(value = "用户返回实体")
public class RPanUserVO implements Serializable {

    private static final long serialVersionUID = -2654165181753780312L;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String username;

    /**
     * 用户根目录ID
     */
    @ApiModelProperty("用户根目录ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long rootFileId;

    /**
     * 用户根目录文件名称
     */
    @ApiModelProperty("用户根目录文件名称")
    private String rootFilename;

    public RPanUserVO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getRootFileId() {
        return rootFileId;
    }

    public void setRootFileId(Long rootFileId) {
        this.rootFileId = rootFileId;
    }

    public String getRootFilename() {
        return rootFilename;
    }

    public void setRootFilename(String rootFilename) {
        this.rootFilename = rootFilename;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RPanUserVO that = (RPanUserVO) o;
        return Objects.equals(username, that.username) &&
                Objects.equals(rootFileId, that.rootFileId) &&
                Objects.equals(rootFilename, that.rootFilename);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, rootFileId, rootFilename);
    }

    @Override
    public String toString() {
        return "RPanUserVO{" +
                "username='" + username + '\'' +
                ", rootFileId=" + rootFileId +
                ", rootFilename='" + rootFilename + '\'' +
                '}';
    }

}
