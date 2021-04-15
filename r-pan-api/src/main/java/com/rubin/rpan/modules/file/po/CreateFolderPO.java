package com.rubin.rpan.modules.file.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 创建文件夹PO
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "创建文件夹PO")
public class CreateFolderPO implements Serializable {

    private static final long serialVersionUID = -4926298360074320330L;

    /**
     * 父级ID
     */
    @ApiModelProperty(value = "父级ID", required = true)
    @NotBlank(message = "父id不能为空")
    private String parentId;

    /**
     * 文件夹名称
     */
    @ApiModelProperty(value = "文件夹名称", required = true)
    @NotBlank(message = "文件夹名称不能为空")
    private String folderName;

}
