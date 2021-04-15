package com.rubin.rpan.modules.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 用户搜索历史返回实体
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "用户搜索历史返回实体")
public class RPanUserSearchHistoryVO implements Serializable {

    private static final long serialVersionUID = 3989329822023576785L;

    /**
     * 搜索历史文案
     */
    @ApiModelProperty("搜索历史文案")
    private String value;

}