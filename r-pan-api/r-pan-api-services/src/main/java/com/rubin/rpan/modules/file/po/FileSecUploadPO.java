package com.rubin.rpan.modules.file.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@ApiModel(value = "文件秒传PO")
public class FileSecUploadPO implements Serializable {

    private static final long serialVersionUID = 1883102378277804464L;

    @ApiModelProperty(value = "文件名称", required = true)
    @NotBlank(message = "文件名称不能为空")
    private String name;

    @ApiModelProperty(value = "文件对应的md5", required = true)
    @NotBlank(message = "md5不能为空")
    private String md5;

    @ApiModelProperty(value = "父id", required = true)
    @NotNull(message = "父id不能为空")
    private Long parentId;

    public FileSecUploadPO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileSecUploadPO that = (FileSecUploadPO) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(md5, that.md5) &&
                Objects.equals(parentId, that.parentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, md5, parentId);
    }

    @Override
    public String toString() {
        return "FileSecUploadPO{" +
                "name='" + name + '\'' +
                ", md5='" + md5 + '\'' +
                ", parentId=" + parentId +
                '}';
    }

}
