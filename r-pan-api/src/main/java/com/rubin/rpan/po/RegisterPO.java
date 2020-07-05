package com.rubin.rpan.po;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * user registration PO
 * Created by RubinChu on 2020/6/5 13:53
 */
@Data
@Accessors(chain = true)
public class RegisterPO implements Serializable {

    private static final long serialVersionUID = 2409461130959403921L;

    /**
     * username
     */
    private String username;

    /**
     * password
     */
    private String password;

    /**
     * question
     */
    private String question;

    /**
     * answer
     */
    private String answer;

}
