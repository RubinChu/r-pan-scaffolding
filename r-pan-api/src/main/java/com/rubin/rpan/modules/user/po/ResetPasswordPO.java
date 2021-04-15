package com.rubin.rpan.modules.user.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 用户重置密码PO
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "用户重置密码PO")
public class ResetPasswordPO implements Serializable {

    private static final long serialVersionUID = 8786438472271278257L;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名", required = true)
    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = "^[0-9A-Za-z]{6,16}$", message = "请输入6-16位只包含数字和字母的用户名")
    private String username;

    /**
     * 新密码
     */
    @ApiModelProperty(value = "新密码", required = true)
    @NotBlank(message = "密码不能为空")
    @Length(min = 8, max = 16, message = "请输入8-16位的密码")
    private String newPassword;

    /**
     * 忘记密码验证token
     */
    @ApiModelProperty(value = "忘记密码验证token", required = true)
    @NotBlank(message = "token不能为空")
    private String token;

}
