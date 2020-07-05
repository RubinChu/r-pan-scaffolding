package com.rubin.rpan.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
public class RPanUser implements Serializable {

    private static final long serialVersionUID = 932247274212033391L;

    /**
     * primary
     */
    private Integer id;

    /**
     * username
     */
    private String username;

    /**
     * password
     */
    private String password;

    /**
     * salt
     */
    private String salt;

    /**
     * question
     */
    private String question;

    /**
     * answer
     */
    private String answer;

    /**
     * createTime
     */
    private Date createTime;

    /**
     * updateTime
     */
    private Date updateTime;

}