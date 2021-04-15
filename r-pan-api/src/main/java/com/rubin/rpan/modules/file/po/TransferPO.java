package com.rubin.rpan.modules.file.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 转移文件PO
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "转移文件PO")
public class TransferPO implements Serializable {

    private static final long serialVersionUID = 5547494462940457810L;

    /**
     * 要转移的文件ID，多个用__,__隔开
     */
    @ApiModelProperty(value = "转移的文件ID，多个用__,__隔开", required = true)
    @NotBlank(message = "请选择要转移的文件")
    private String fileIds;

    /**
     * 当前目录的父级目录ID
     */
    @ApiModelProperty(value = "当前目录的父级目录ID", required = true)
    @NotBlank(message = "父id不能为空")
    private String parentId;

    /**
     * 要转移到的文件夹ID
     */
    @ApiModelProperty(value = "要转移到的文件夹ID", required = true)
    @NotBlank(message = "请选择要转移到的文件夹")
    private String targetParentId;
}
