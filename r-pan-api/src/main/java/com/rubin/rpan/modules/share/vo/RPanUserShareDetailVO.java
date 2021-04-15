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
 * 分享链接详情返回实体
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "分享链接详情返回实体")
public class RPanUserShareDetailVO implements Serializable {

    private static final long serialVersionUID = 5467063905263311932L;

    public RPanUserShareDetailVO(RPanShare rPanShare) {
        this.shareId = rPanShare.getShareId();
        this.shareName = rPanShare.getShareName();
        this.createTime = rPanShare.getCreateTime();
        this.shareDay = rPanShare.getShareDay();
        this.shareEndTime = rPanShare.getShareEndTime();
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
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 分享有效天数（永久有效为0）
     */
    @ApiModelProperty("分享有效天数（永久有效为0）")
    private Integer shareDay;

    /**
     * 分享结束时间
     */
    @ApiModelProperty("分享结束时间")
    private Date shareEndTime;

    /**
     * 分享的文件列表
     */
    @ApiModelProperty("分享的文件列表")
    private List<RPanUserFileVO> rPanUserFileVOList;

    /**
     * 分享者信息
     */
    @ApiModelProperty("分享者信息")
    private ShareUserInfoVO shareUserInfoVO;

}
