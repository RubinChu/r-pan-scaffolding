package com.rubin.rpan.po;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * update file name PO
 * Created by rubin on 2020/5/31.
 */
@Data
@Accessors(chain = true)
public class UpdateFileNamePO implements Serializable {
    private static final long serialVersionUID = 6354021506022172922L;

    /**
     * fileId
     */
    private String fileId;

    /**
     * newFileName
     */
    private String newFileName;

}
