package com.rubin.rpan.modules.file.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 文件返回实体
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "文件返回实体")
public class RPanUserFileVO implements Serializable {

    private static final long serialVersionUID = -8132138457555677897L;

    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private String fileId;

    /**
     * 父id
     */
    @ApiModelProperty("父id")
    private String parentId;

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
    private Integer type;

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

}
