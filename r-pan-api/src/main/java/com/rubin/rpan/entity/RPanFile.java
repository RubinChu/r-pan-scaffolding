package com.rubin.rpan.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
public class RPanFile implements Serializable {

    private static final long serialVersionUID = -1396349578724391118L;

    /**
     * primary
     */
    private Integer id;

    /**
     * file record unique identifier
     */
    private String fileId;

    /**
     * parentId
     */
    private String parentId;

    /**
     * userId
     */
    private Integer userId;

    /**
     * fileName
     */
    private String fileName;

    /**
     * realPath
     */
    private String realPath;

    /**
     * showPath
     */
    private String showPath;

    /**
     * fileSize
     */
    private String fileSize;

    /**
     * file type
     *
     * @see com.rubin.rpan.common.constants.Constants.FileType
     */
    private Integer type;

    /**
     * createUser
     */
    private Integer createUser;

    /**
     * createTime
     */
    private Date createTime;

    /**
     * updateUser
     */
    private Integer updateUser;

    /**
     * updateTime
     */
    private Date updateTime;

}