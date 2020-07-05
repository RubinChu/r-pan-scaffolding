package com.rubin.rpan.po;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * check forgot password answer PO
 * Created by RubinChu on 2020/6/5 15:11
 */
@Data
@Accessors(chain = true)
public class CheckAnswerPO implements Serializable {

    private static final long serialVersionUID = -1966217684837591319L;

    /**
     * username
     */
    private String username;

    /**
     * question
     */
    private String question;

    /**
     * answer
     */
    private String answer;

}
