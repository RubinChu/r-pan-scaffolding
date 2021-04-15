package com.rubin.rpan.modules.file.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 更新文件名PO
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "更新文件名PO")
public class UpdateFileNamePO implements Serializable {

    private static final long serialVersionUID = 169947393401301942L;

    /**
     * 文件记录ID
     */
    @ApiModelProperty(value = "文件记录ID", required = true)
    @NotBlank(message = "文件id不能为空")
    private String fileId;

    /**
     * 新文件名
     */
    @ApiModelProperty(value = "新文件名", required = true)
    @NotBlank(message = "新文件名称不能为空")
    private String newFilename;

}
