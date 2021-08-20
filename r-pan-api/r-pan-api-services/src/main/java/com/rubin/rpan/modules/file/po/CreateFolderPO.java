package com.rubin.rpan.modules.file.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * 创建文件夹PO
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@ApiModel(value = "创建文件夹PO")
public class CreateFolderPO implements Serializable {

    private static final long serialVersionUID = -4926298360074320330L;

    /**
     * 父级ID
     */
    @ApiModelProperty(value = "父级ID", required = true)
    @NotNull(message = "父id不能为空")
    private Long parentId;

    /**
     * 文件夹名称
     */
    @ApiModelProperty(value = "文件夹名称", required = true)
    @NotBlank(message = "文件夹名称不能为空")
    private String folderName;

    public CreateFolderPO() {
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateFolderPO that = (CreateFolderPO) o;
        return Objects.equals(parentId, that.parentId) &&
                Objects.equals(folderName, that.folderName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parentId, folderName);
    }

    @Override
    public String toString() {
        return "CreateFolderPO{" +
                "parentId=" + parentId +
                ", folderName='" + folderName + '\'' +
                '}';
    }

}
