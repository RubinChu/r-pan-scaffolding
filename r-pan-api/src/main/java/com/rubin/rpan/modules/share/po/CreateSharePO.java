package com.rubin.rpan.modules.share.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 创建分享链接PO
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "创建分享链接PO")
public class CreateSharePO implements Serializable {

    private static final long serialVersionUID = -7390316224681009882L;

    /**
     * 分享名称
     */
    @ApiModelProperty(value = "分享名称", required = true)
    @NotBlank(message = "分享名称不能为空")
    private String shareName;

    /**
     * 分享类型 0 有提取码
     */
    @ApiModelProperty(value = "分享类型 0 有提取码", required = true)
    @NotNull(message = "分享类型不能为空")
    private Integer shareType;

    /**
     * 分享期限类型 0 永久有效 1 7天有效 2 30天有效
     */
    @ApiModelProperty(value = "分享期限类型 0 永久有效 1 7天有效 2 30天有效", required = true)
    @NotNull(message = "分享期限类型不能为空")
    private Integer shareDayType;

    /**
     * 分享文件的id 多个用__,__隔开
     */
    @ApiModelProperty(value = "分享文件的id 多个用__,__隔开", required = true)
    @NotBlank(message = "请选择要分享的文件")
    private String shareFileIds;

}
