package com.rubin.rpan.modules.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 用户返回实体
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "用户返回实体")
public class RPanUserVO implements Serializable {

    private static final long serialVersionUID = -2654165181753780312L;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private String userId;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String username;

    /**
     * 用户根目录ID
     */
    @ApiModelProperty("用户根目录ID")
    private String rootFileId;

    /**
     * 用户根目录文件名称
     */
    @ApiModelProperty("用户根目录文件名称")
    private String rootFilename;

}
