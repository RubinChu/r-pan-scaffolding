package com.rubin.rpan.modules.share.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * 创建分享链接PO
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@ApiModel(value = "创建分享链接PO")
public class CreateSharePO implements Serializable {

    private static final long serialVersionUID = -7390316224681009882L;

    /**
     * 分享名称
     */
    @ApiModelProperty(value = "分享名称", required = true)
    @NotBlank(message = "分享名称不能为空")
    private String shareName;

    /**
     * 分享类型 0 有提取码
     */
    @ApiModelProperty(value = "分享类型 0 有提取码", required = true)
    @NotNull(message = "分享类型不能为空")
    private Integer shareType;

    /**
     * 分享期限类型 0 永久有效 1 7天有效 2 30天有效
     */
    @ApiModelProperty(value = "分享期限类型 0 永久有效 1 7天有效 2 30天有效", required = true)
    @NotNull(message = "分享期限类型不能为空")
    private Integer shareDayType;

    /**
     * 分享文件的id 多个用__,__隔开
     */
    @ApiModelProperty(value = "分享文件的id 多个用__,__隔开", required = true)
    @NotBlank(message = "请选择要分享的文件")
    private String shareFileIds;

    public CreateSharePO() {
    }

    public String getShareName() {
        return shareName;
    }

    public void setShareName(String shareName) {
        this.shareName = shareName;
    }

    public Integer getShareType() {
        return shareType;
    }

    public void setShareType(Integer shareType) {
        this.shareType = shareType;
    }

    public Integer getShareDayType() {
        return shareDayType;
    }

    public void setShareDayType(Integer shareDayType) {
        this.shareDayType = shareDayType;
    }

    public String getShareFileIds() {
        return shareFileIds;
    }

    public void setShareFileIds(String shareFileIds) {
        this.shareFileIds = shareFileIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateSharePO that = (CreateSharePO) o;
        return Objects.equals(shareName, that.shareName) &&
                Objects.equals(shareType, that.shareType) &&
                Objects.equals(shareDayType, that.shareDayType) &&
                Objects.equals(shareFileIds, that.shareFileIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shareName, shareType, shareDayType, shareFileIds);
    }

    @Override
    public String toString() {
        return "CreateSharePO{" +
                "shareName='" + shareName + '\'' +
                ", shareType=" + shareType +
                ", shareDayType=" + shareDayType +
                ", shareFileIds='" + shareFileIds + '\'' +
                '}';
    }

}
