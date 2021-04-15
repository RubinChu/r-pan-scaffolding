package com.rubin.rpan.modules.file.vo;


import com.google.common.collect.Lists;
import com.rubin.rpan.modules.file.entity.RPanUserFile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 文件夹信息返回实体
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "文件夹信息返回实体")
public class FolderTreeNode implements Serializable {

    private static final long serialVersionUID = 3942552174021452303L;

    /**
     * 节点显示标题
     */
    @ApiModelProperty("节点显示标题")
    private String label;

    /**
     * 唯一标识
     */
    @ApiModelProperty("唯一标识")
    private String id;

    /**
     * 子节点
     */
    @ApiModelProperty("子节点")
    private List<FolderTreeNode> children;

    /**
     * 父文件夹ID
     */
    @ApiModelProperty("父文件夹ID")
    private String parentId;

    /**
     * 拼装树节点
     *
     * @param rPanUserFile
     * @return
     */
    public static FolderTreeNode assembleFolderTreeNode(RPanUserFile rPanUserFile) {
        FolderTreeNode folderTreeNode = new FolderTreeNode();
        folderTreeNode.setLabel(rPanUserFile.getFilename())
                .setId(rPanUserFile.getFileId())
                .setChildren(Lists.newArrayList())
                .setParentId(rPanUserFile.getParentId());
        return folderTreeNode;
    }

}
