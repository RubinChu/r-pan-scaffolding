package com.rubin.rpan.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * user returns entity
 * Created by rubin on 2020/5/31.
 */

@Data
@Accessors(chain = true)
public class RPanUserVO implements Serializable {

    private static final long serialVersionUID = 6738182044196028202L;

    /**
     * id
     */
    private Integer id;

    /**
     * username
     */
    private String username;

    /**
     * rootFileId
     */
    private String rootFileId;

    /**
     * rootFileName
     */
    private String rootFileName;

}
