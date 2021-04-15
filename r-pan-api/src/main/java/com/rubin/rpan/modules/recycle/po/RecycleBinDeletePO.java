package com.rubin.rpan.modules.recycle.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 回收站删除文件PO
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "回收站删除文件PO")
public class RecycleBinDeletePO implements Serializable {

    private static final long serialVersionUID = -3274470891486524920L;

    /**
     * 要删除的文件记录ID，多个用__,__隔开
     */
    @ApiModelProperty(value = "要删除的文件记录ID，多个用__,__隔开", required = true)
    @NotBlank(message = "文件id不能为空")
    private String fileIds;

}
