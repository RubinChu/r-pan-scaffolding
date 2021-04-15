package com.rubin.rpan.modules.share.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 校验分享码参数实体
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "校验分享码参数实体")
public class CheckShareCodePO implements Serializable {

    private static final long serialVersionUID = 1573366453472435825L;

    /**
     * 分享id
     */
    @ApiModelProperty(value = "分享id", required = true)
    @NotBlank(message = "分享id不能为空")
    private String shareId;

    /**
     * 分享码
     */
    @ApiModelProperty(value = "分享码", required = true)
    @NotBlank(message = "分享码不能为空")
    private String shareCode;

}
