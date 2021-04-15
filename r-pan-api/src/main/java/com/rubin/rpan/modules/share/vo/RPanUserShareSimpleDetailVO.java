package com.rubin.rpan.modules.share.vo;

import com.rubin.rpan.modules.file.vo.RPanUserFileVO;
import com.rubin.rpan.modules.share.entity.RPanShare;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 分享链接简单详情返回实体
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "分享链接简单详情返回实体")
public class RPanUserShareSimpleDetailVO implements Serializable {

    private static final long serialVersionUID = 1601779429991293326L;

    public RPanUserShareSimpleDetailVO(RPanShare rPanShare) {
        this.shareId = rPanShare.getShareId();
        this.shareName = rPanShare.getShareName();
    }

    /**
     * 分享id
     */
    @ApiModelProperty("分享id")
    private String shareId;

    /**
     * 分享名称
     */
    @ApiModelProperty("分享名称")
    private String shareName;

    /**
     * 分享者信息
     */
    @ApiModelProperty("分享者信息")
    private ShareUserInfoVO shareUserInfoVO;

}
