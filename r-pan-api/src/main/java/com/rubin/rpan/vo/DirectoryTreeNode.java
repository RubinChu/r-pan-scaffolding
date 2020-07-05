package com.rubin.rpan.vo;


import com.google.common.collect.Lists;
import com.rubin.rpan.common.constants.Constants;
import com.rubin.rpan.entity.RPanFile;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * folder information entity class
 * Created by rubin on 2019/9/7.
 */
@Data
@Accessors(chain = true)
public class DirectoryTreeNode implements Serializable {

    private static final long serialVersionUID = 2869729269782342194L;

    /**
     * label
     */
    private String label;

    /**
     * id
     */
    private String id;

    /**
     * children
     */
    private List<DirectoryTreeNode> children;

    /**
     * parentId
     */
    private String parentId;

    /**
     * assembleDirectoryTreeNode
     *
     * @param rPanFile
     * @return
     */
    public static DirectoryTreeNode assembleDirectoryTreeNode(RPanFile rPanFile) {
        DirectoryTreeNode directoryTreeNode = new DirectoryTreeNode();
        directoryTreeNode.setLabel(rPanFile.getFileName())
                .setId(rPanFile.getFileId())
                .setChildren(Lists.newArrayList())
                .setParentId(rPanFile.getParentId());
        return directoryTreeNode;
    }

}
