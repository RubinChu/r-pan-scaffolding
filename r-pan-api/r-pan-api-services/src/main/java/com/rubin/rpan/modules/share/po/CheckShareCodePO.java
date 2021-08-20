package com.rubin.rpan.modules.share.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * 校验分享码参数实体
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@ApiModel(value = "校验分享码参数实体")
public class CheckShareCodePO implements Serializable {

    private static final long serialVersionUID = 1573366453472435825L;

    /**
     * 分享id
     */
    @ApiModelProperty(value = "分享id", required = true)
    @NotNull(message = "分享id不能为空")
    private Long shareId;

    /**
     * 分享码
     */
    @ApiModelProperty(value = "分享码", required = true)
    @NotBlank(message = "分享码不能为空")
    private String shareCode;

    public CheckShareCodePO() {
    }

    public Long getShareId() {
        return shareId;
    }

    public void setShareId(Long shareId) {
        this.shareId = shareId;
    }

    public String getShareCode() {
        return shareCode;
    }

    public void setShareCode(String shareCode) {
        this.shareCode = shareCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CheckShareCodePO that = (CheckShareCodePO) o;
        return Objects.equals(shareId, that.shareId) &&
                Objects.equals(shareCode, that.shareCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shareId, shareCode);
    }

    @Override
    public String toString() {
        return "CheckShareCodePO{" +
                "shareId=" + shareId +
                ", shareCode='" + shareCode + '\'' +
                '}';
    }

}
