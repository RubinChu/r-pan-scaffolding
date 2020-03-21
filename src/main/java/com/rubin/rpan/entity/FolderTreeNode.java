package com.rubin.rpan.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rubin on 2019/9/7.
 * 文件夹信息实体类
 */
@Data
public class FolderTreeNode implements Serializable {

    private static final long serialVersionUID = 2869729269782342194L;

    /**
     * 节点显示标题
     */
    private String title;

    /**
     * 唯一标识
     */
    private String id;

    /**
     * 子节点
     */
    private List<FolderTreeNode> children;

    /**
     * 点击跳转链接
     */
    private String href;

    /**
     * 是否默认展开
     */
    private Boolean spread;

    /**
     * 是否默认选中
     */
    private Boolean checked;

    /**
     * 是否禁用
     */
    private Boolean disabled;

    /**
     * 文件夹相对路径
     */
    private String parentId;

}
