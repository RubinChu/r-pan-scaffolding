package com.rubin.rpan.modules.file.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.rubin.rpan.modules.file.entity.RPanUserFile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * 面包屑实体返回信息
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@ApiModel(value = "面包屑实体返回信息")
public class BreadcrumbVO implements Serializable {

    private static final long serialVersionUID = 7255547858149446544L;

    /**
     * 面包屑主键
     */
    @ApiModelProperty("面包屑主键")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 父id
     */
    @ApiModelProperty("父id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long parentId;

    /**
     * 面包屑名称
     */
    @ApiModelProperty("面包屑名称")
    private String name;

    public static BreadcrumbVO assembleBreadcrumbVO(RPanUserFile rPanUserFile) {
        BreadcrumbVO breadcrumbVO = new BreadcrumbVO();
        breadcrumbVO.setId(rPanUserFile.getFileId());
        breadcrumbVO.setParentId(rPanUserFile.getParentId());
        breadcrumbVO.setName(rPanUserFile.getFilename());
        return breadcrumbVO;
    }

    public BreadcrumbVO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
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
        BreadcrumbVO that = (BreadcrumbVO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(parentId, that.parentId) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, parentId, name);
    }

    @Override
    public String toString() {
        return "BreadcrumbVO{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", name='" + name + '\'' +
                '}';
    }

}
