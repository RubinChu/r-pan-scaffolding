package com.rubin.rpan.po;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * check username PO
 * Created by RubinChu on 2020/6/5 15:03
 */
@Data
@Accessors(chain = true)
public class CheckUsernamePO implements Serializable {

    private static final long serialVersionUID = -6341217428770821922L;

    /**
     * username
     */
    private String username;

}
