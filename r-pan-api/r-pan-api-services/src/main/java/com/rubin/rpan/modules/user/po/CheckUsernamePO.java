package com.rubin.rpan.modules.user.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Objects;

/**
 * 检查用户名PO
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@ApiModel(value = "检查用户名PO")
public class CheckUsernamePO implements Serializable {

    private static final long serialVersionUID = -947670268079483200L;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名", required = true)
    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = "^[0-9A-Za-z]{6,16}$", message = "请输入6-16位只包含数字和字母的用户名")
    private String username;

    public CheckUsernamePO() {
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
        CheckUsernamePO that = (CheckUsernamePO) o;
        return Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public String toString() {
        return "CheckUsernamePO{" +
                "username='" + username + '\'' +
                '}';
    }

}
