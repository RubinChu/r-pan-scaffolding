package com.rubin.rpan.modules.user.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 修改密码PO
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "修改密码PO")
public class ChangePasswordPO implements Serializable {

    private static final long serialVersionUID = -7503691157142209504L;

    /**
     * 旧密码
     */
    @ApiModelProperty(value = "旧密码", required = true)
    @NotBlank(message = "原密码不能为空")
    @Length(min = 8, max = 16, message = "请输入8-16位的原密码")
    private String password;

    /**
     * 新密码
     */
    @ApiModelProperty(value = "新密码", required = true)
    @NotBlank(message = "新密码不能为空")
    @Length(min = 8, max = 16, message = "请输入8-16位的新密码")
    private String newPassword;

}
