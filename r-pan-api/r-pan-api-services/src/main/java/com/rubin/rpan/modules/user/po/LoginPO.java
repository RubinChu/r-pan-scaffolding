package com.rubin.rpan.modules.user.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Objects;

/**
 * 用户登录PO
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@ApiModel(value = "用户登录PO")
public class LoginPO implements Serializable {

    private static final long serialVersionUID = -1602630613457615685L;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名", required = true)
    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = "^[0-9A-Za-z]{6,16}$", message = "请输入6-16位只包含数字和字母的用户名")
    private String username;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码", required = true)
    @NotBlank(message = "密码不能为空")
    @Length(min = 8, max = 16, message = "请输入8-16位的密码")
    private String password;

    public LoginPO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginPO loginPO = (LoginPO) o;
        return Objects.equals(username, loginPO.username) &&
                Objects.equals(password, loginPO.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }

    @Override
    public String toString() {
        return "LoginPO{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
