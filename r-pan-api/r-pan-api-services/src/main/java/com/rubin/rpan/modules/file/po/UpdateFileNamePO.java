package com.rubin.rpan.modules.file.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * 更新文件名PO
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@ApiModel(value = "更新文件名PO")
public class UpdateFileNamePO implements Serializable {

    private static final long serialVersionUID = 169947393401301942L;

    /**
     * 文件记录ID
     */
    @ApiModelProperty(value = "文件记录ID", required = true)
    @NotNull(message = "文件id不能为空")
    private Long fileId;

    /**
     * 新文件名
     */
    @ApiModelProperty(value = "新文件名", required = true)
    @NotBlank(message = "新文件名称不能为空")
    private String newFilename;

    public UpdateFileNamePO() {
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getNewFilename() {
        return newFilename;
    }

    public void setNewFilename(String newFilename) {
        this.newFilename = newFilename;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdateFileNamePO that = (UpdateFileNamePO) o;
        return Objects.equals(fileId, that.fileId) &&
                Objects.equals(newFilename, that.newFilename);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileId, newFilename);
    }

    @Override
    public String toString() {
        return "UpdateFileNamePO{" +
                "fileId=" + fileId +
                ", newFilename='" + newFilename + '\'' +
                '}';
    }

}
