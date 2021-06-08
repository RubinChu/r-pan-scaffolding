package com.rubin.rpan.modules.file.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * 文件返回实体
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@ApiModel(value = "文件返回实体")
public class RPanUserFileVO implements Serializable {

    private static final long serialVersionUID = -8132138457555677897L;

    /**
     * 主键
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("主键")
    private Long fileId;

    /**
     * 父id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("父id")
    private Long parentId;

    /**
     * 文件名称
     */
    @ApiModelProperty("文件名称")
    private String filename;

    /**
     * 文件夹标识 0 否 1 是
     */
    @ApiModelProperty("文件夹标识 0 否 1 是")
    private Integer folderFlag;

    /**
     * 文件大小
     */
    @ApiModelProperty("文件大小")
    private String fileSizeDesc;

    /**
     * 文件类型 文件类型（1 文件 2 压缩文件 3 excel 4 word 5 pdf 6 txt 7 图片 8 音频 9 视频 10 ppt 11 源码文件）
     */
    @ApiModelProperty("文件类型 文件类型（1 文件 2 压缩文件 3 excel 4 word 5 pdf 6 txt 7 图片 8 音频 9 视频 10 ppt 11 源码文件）")
    private Integer fileType;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updateTime;

    public RPanUserFileVO() {
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Integer getFolderFlag() {
        return folderFlag;
    }

    public void setFolderFlag(Integer folderFlag) {
        this.folderFlag = folderFlag;
    }

    public String getFileSizeDesc() {
        return fileSizeDesc;
    }

    public void setFileSizeDesc(String fileSizeDesc) {
        this.fileSizeDesc = fileSizeDesc;
    }

    public Integer getFileType() {
        return fileType;
    }

    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RPanUserFileVO that = (RPanUserFileVO) o;
        return Objects.equals(fileId, that.fileId) &&
                Objects.equals(parentId, that.parentId) &&
                Objects.equals(filename, that.filename) &&
                Objects.equals(folderFlag, that.folderFlag) &&
                Objects.equals(fileSizeDesc, that.fileSizeDesc) &&
                Objects.equals(fileType, that.fileType) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(updateTime, that.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileId, parentId, filename, folderFlag, fileSizeDesc, fileType, createTime, updateTime);
    }

    @Override
    public String toString() {
        return "RPanUserFileVO{" +
                "fileId=" + fileId +
                ", parentId=" + parentId +
                ", filename='" + filename + '\'' +
                ", folderFlag=" + folderFlag +
                ", fileSizeDesc='" + fileSizeDesc + '\'' +
                ", fileType=" + fileType +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }

}
