package com.rubin.rpan.modules.file.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * 物理文件信息实体
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
public class RPanFile implements Serializable {

    private static final long serialVersionUID = -717520378331457192L;

    /**
     * 文件id
     */
    private Long fileId;

    /**
     * 文件名称
     */
    private String filename;

    /**
     * 真实物理存放路径
     */
    private String realPath;

    /**
     * 文件大小
     */
    private String fileSize;

    /**
     * 文件大小显示文案
     */
    private String fileSizeDesc;

    /**
     * 文件后缀
     */
    private String fileSuffix;

    /**
     * 文件预览Content-Type响应头的值
     */
    private String filePreviewContentType;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    public RPanFile() {
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getRealPath() {
        return realPath;
    }

    public void setRealPath(String realPath) {
        this.realPath = realPath;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileSizeDesc() {
        return fileSizeDesc;
    }

    public void setFileSizeDesc(String fileSizeDesc) {
        this.fileSizeDesc = fileSizeDesc;
    }

    public String getFileSuffix() {
        return fileSuffix;
    }

    public void setFileSuffix(String fileSuffix) {
        this.fileSuffix = fileSuffix;
    }

    public String getFilePreviewContentType() {
        return filePreviewContentType;
    }

    public void setFilePreviewContentType(String filePreviewContentType) {
        this.filePreviewContentType = filePreviewContentType;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RPanFile rPanFile = (RPanFile) o;
        return Objects.equals(fileId, rPanFile.fileId) &&
                Objects.equals(filename, rPanFile.filename) &&
                Objects.equals(realPath, rPanFile.realPath) &&
                Objects.equals(fileSize, rPanFile.fileSize) &&
                Objects.equals(fileSizeDesc, rPanFile.fileSizeDesc) &&
                Objects.equals(fileSuffix, rPanFile.fileSuffix) &&
                Objects.equals(filePreviewContentType, rPanFile.filePreviewContentType) &&
                Objects.equals(createUser, rPanFile.createUser) &&
                Objects.equals(createTime, rPanFile.createTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileId, filename, realPath, fileSize, fileSizeDesc, fileSuffix, filePreviewContentType, createUser, createTime);
    }

    @Override
    public String toString() {
        return "RPanFile{" +
                "fileId=" + fileId +
                ", filename='" + filename + '\'' +
                ", realPath='" + realPath + '\'' +
                ", fileSize='" + fileSize + '\'' +
                ", fileSizeDesc='" + fileSizeDesc + '\'' +
                ", fileSuffix='" + fileSuffix + '\'' +
                ", filePreviewContentType='" + filePreviewContentType + '\'' +
                ", createUser=" + createUser +
                ", createTime=" + createTime +
                '}';
    }

}