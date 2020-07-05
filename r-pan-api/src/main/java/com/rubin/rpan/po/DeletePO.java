package com.rubin.rpan.po;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * delete file PO
 * Created by rubin on 2020/5/31.
 */

@Data
@Accessors(chain = true)
public class DeletePO implements Serializable {
    private static final long serialVersionUID = -7780618623130768453L;

    /**
     * parentId
     */
    private String parentId;

    /**
     * the record ID of the file to be deleted, multiple separated by __,__
     */
    private String fileIds;

}
