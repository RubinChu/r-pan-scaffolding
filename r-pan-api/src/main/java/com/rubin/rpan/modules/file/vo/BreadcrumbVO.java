package com.rubin.rpan.modules.file.vo;

import com.rubin.rpan.modules.file.entity.RPanUserFile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 面包屑实体返回信息
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "面包屑实体返回信息")
public class BreadcrumbVO implements Serializable {

    private static final long serialVersionUID = 7255547858149446544L;

    /**
     * 面包屑主键
     */
    @ApiModelProperty("面包屑主键")
    private String id;

    /**
     * 父id
     */
    @ApiModelProperty("父id")
    private String parentId;

    /**
     * 面包屑名称
     */
    @ApiModelProperty("面包屑名称")
    private String name;

    public static BreadcrumbVO assembleBreadcrumbVO(RPanUserFile rPanUserFile) {
        BreadcrumbVO breadcrumbVO = new BreadcrumbVO();
        breadcrumbVO.setId(rPanUserFile.getFileId())
                .setParentId(rPanUserFile.getParentId())
                .setName(rPanUserFile.getFilename());
        return breadcrumbVO;
    }

}
