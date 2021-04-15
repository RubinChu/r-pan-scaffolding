package com.rubin.rpan.modules.share.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 分享链接详情返回实体
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "分享链接用户详情返回实体")
public class ShareUserInfoVO implements Serializable {

    private static final long serialVersionUID = -3389565809049484829L;

    /**
     * 分享者id
     */
    @ApiModelProperty("分享者id")
    private String userId;

    /**
     * 分享者昵称
     */
    @ApiModelProperty("分享者昵称")
    private String username;

}
