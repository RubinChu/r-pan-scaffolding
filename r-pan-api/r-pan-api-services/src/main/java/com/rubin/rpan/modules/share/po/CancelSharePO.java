package com.rubin.rpan.modules.share.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;

/**
 * 取消分享链接PO
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@ApiModel(value = "取消分享链接PO")
public class CancelSharePO implements Serializable {

    private static final long serialVersionUID = -2389804454900224792L;

    /**
     * 分享id，多个用__,__分隔
     */
    @ApiModelProperty(value = "分享id，多个用__,__分隔", required = true)
    @NotBlank(message = "分享id不能为空")
    private String shareIds;

    public CancelSharePO() {
    }

    public String getShareIds() {
        return shareIds;
    }

    public void setShareIds(String shareIds) {
        this.shareIds = shareIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CancelSharePO that = (CancelSharePO) o;
        return Objects.equals(shareIds, that.shareIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shareIds);
    }

    @Override
    public String toString() {
        return "CancelSharePO{" +
                "shareIds='" + shareIds + '\'' +
                '}';
    }

}
