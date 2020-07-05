package com.rubin.rpan.po;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * change password PO
 * Created by RubinChu on 2020/6/5 15:39
 */
@Data
@Accessors(chain = true)
public class ChangePasswordPO implements Serializable {

    private static final long serialVersionUID = 3354263303988623277L;

    /**
     * old password
     */
    private String password;

    /**
     * new password
     */
    private String newPassword;

}
