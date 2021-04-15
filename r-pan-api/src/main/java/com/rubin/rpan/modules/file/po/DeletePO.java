package com.rubin.rpan.modules.file.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 删除文件PO
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "删除文件PO")
public class DeletePO implements Serializable {

    private static final long serialVersionUID = -1604961110716421918L;

    /**
     * 父级ID
     */
    @ApiModelProperty(value = "父级ID", required = true)
    @NotBlank(message = "父id不能为空")
    private String parentId;

    /**
     * 要删除的文件记录ID，多个用__,__隔开
     */
    @ApiModelProperty(value = "要删除的文件记录ID，多个用__,__隔开", required = true)
    @NotBlank(message = "文件id不能为空")
    private String fileIds;

}
