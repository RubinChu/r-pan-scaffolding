package com.rubin.rpan.modules.share.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 创建分享链接PO
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "保存到我的文件夹PO")
public class ShareSavePO implements Serializable {

    private static final long serialVersionUID = 1805035523468416712L;

    /**
     * 要保存的文件id，多个用逗号隔开
     */
    @ApiModelProperty(value = "要保存的文件id，多个用逗号隔开", required = true)
    @NotBlank(message = "请选择要保存的文件")
    private String fileIds;

    /**
     * 要保存到的文件夹id
     */
    @ApiModelProperty(value = "要保存到的文件夹id", required = true)
    @NotBlank(message = "请选择要保存到的文件夹")
    private String targetParentId;

}
