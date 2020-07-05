package com.rubin.rpan.po;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * user loginPO
 * Created by RubinChu on 2020/6/5 13:59
 */
@Data
@Accessors(chain = true)
public class LoginPO implements Serializable {

    private static final long serialVersionUID = 3731378791193144067L;

    /**
     * username
     */
    private String username;

    /**
     * password
     */
    private String password;

}
