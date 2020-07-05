package com.rubin.rpan.po;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * transfer files PO
 * Created by rubin on 2020/5/31.
 */

@Data
@Accessors(chain = true)
public class TransferPO implements Serializable {
    private static final long serialVersionUID = 6897905891544095322L;

    /**
     * file ID to be transferred, multiple separated by __,__
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
