package com.rubin.rpan.po;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * user reset password PO
 * Created by RubinChu on 2020/6/5 15:23
 */
@Data
@Accessors(chain = true)
public class ResetPasswordPO implements Serializable {

    private static final long serialVersionUID = -9059847965492071453L;

    /**
     * username
     */
    private String username;

    /**
     * newPassword
     */
    private String newPassword;

    /**
     * token
     */
    private String token;

}
