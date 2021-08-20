package com.rubin.rpan.modules.file.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@ApiModel(value = "文件上传PO")
public class FileUploadPO implements Serializable {

    private static final long serialVersionUID = -6712494353573613150L;

    @ApiModelProperty(value = "文件名称", required = true)
    @NotBlank(message = "文件名称不能为空")
    private String name;

    @ApiModelProperty(value = "文件对应的md5", required = true)
    @NotBlank(message = "md5不能为空")
    private String md5;

    @ApiModelProperty(value = "总分片数")
    private Integer chunks;

    @ApiModelProperty(value = "分片下标")
    private Integer chunk;

    @ApiModelProperty(value = "文件大小", required = true)
    @NotNull(message = "文件大小不能为空")
    private Long size;

    @ApiModelProperty(value = "父id", required = true)
    @NotNull(message = "父id不能为空")
    private Long parentId;

    @ApiModelProperty(value = "上传文件", required = true)
    @NotNull(message = "上传文件不能为空")
    private MultipartFile file;

    public FileUploadPO() {
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Integer getChunks() {
        return chunks;
    }

    public void setChunks(Integer chunks) {
        this.chunks = chunks;
    }

    public Integer getChunk() {
        return chunk;
    }

    public void setChunk(Integer chunk) {
        this.chunk = chunk;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileUploadPO that = (FileUploadPO) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(md5, that.md5) &&
                Objects.equals(chunks, that.chunks) &&
                Objects.equals(chunk, that.chunk) &&
                Objects.equals(size, that.size) &&
                Objects.equals(parentId, that.parentId) &&
                Objects.equals(file, that.file);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, md5, chunks, chunk, size, parentId, file);
    }

    @Override
    public String toString() {
        return "FileUploadPO{" +
                "name='" + name + '\'' +
                ", md5='" + md5 + '\'' +
                ", chunks=" + chunks +
                ", chunk=" + chunk +
                ", size=" + size +
                ", parentId=" + parentId +
                ", file=" + file +
                '}';
    }

    /**
     * 校验是不是分片上传
     *
     * @return
     */
    public boolean isChunked() {
        return !Objects.isNull(chunk) && !Objects.isNull(chunks);
    }

}
