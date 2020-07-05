package com.rubin.rpan.po;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * create folder PO
 * Created by rubin on 2020/5/31.
 */

@Data
@Accessors(chain = true)
public class CreateDirectoryPO implements Serializable {
    private static final long serialVersionUID = -5796095416386821639L;

    /**
     * parentId
     */
    private String parentId;

    /**
     * directoryName
     */
    private String directoryName;
}
