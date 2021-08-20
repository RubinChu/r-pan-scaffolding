package com.rubin.rpan.modules.recycle.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;

/**
 * 还原文件PO
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@ApiModel(value = "还原文件PO")
public class RestorePO implements Serializable {

    private static final long serialVersionUID = -1455754581383356687L;

    /**
     * 要还原的文件记录ID，多个用__,__隔开
     */
    @ApiModelProperty(value = "要还原的文件记录ID，多个用__,__隔开", required = true)
    @NotBlank(message = "文件id不能为空")
    private String fileIds;

    public RestorePO() {
    }

    public String getFileIds() {
        return fileIds;
    }

    public void setFileIds(String fileIds) {
        this.fileIds = fileIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestorePO restorePO = (RestorePO) o;
        return Objects.equals(fileIds, restorePO.fileIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileIds);
    }

    @Override
    public String toString() {
        return "RestorePO{" +
                "fileIds='" + fileIds + '\'' +
                '}';
    }

}
