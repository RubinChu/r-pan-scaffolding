package com.rubin.rpan.po;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Created by rubin on 2020/6/14.
 */

@Data
@Accessors(chain = true)
public class CopyPO implements Serializable {

    private static final long serialVersionUID = 1647689436377006254L;

    /**
     * file ID to be copied, multiple separated by __,__
     */
    private String fileIds;

    /**
     * list parentId
     */
    private String parentId;

    /**
     * targetParentId
     */
    private String targetParentId;

}
