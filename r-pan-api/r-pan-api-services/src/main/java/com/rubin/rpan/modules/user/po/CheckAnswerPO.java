package com.rubin.rpan.modules.user.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Objects;

/**
 * 检查忘记密码密保答案PO
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@ApiModel(value = "检查忘记密码密保答案PO")
public class CheckAnswerPO implements Serializable {

    private static final long serialVersionUID = -5463583920866083137L;

    /**
     * 用户名称
     */
    @ApiModelProperty(value = "用户名称", required = true)
    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = "^[0-9A-Za-z]{6,16}$", message = "请输入6-16位只包含数字和字母的用户名")
    private String username;

    /**
     * 密保问题
     */
    @ApiModelProperty(value = "密保问题", required = true)
    @NotBlank(message = "密保问题不能为空")
    @Length(max = 100, message = "密保问题不能超过100个字")
    private String question;

    /**
     * 密保答案
     */
    @ApiModelProperty(value = "密保答案", required = true)
    @NotBlank(message = "密保答案不能为空")
    @Length(max = 100, message = "密保答案不能超过100个字")
    private String answer;

    public CheckAnswerPO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CheckAnswerPO that = (CheckAnswerPO) o;
        return Objects.equals(username, that.username) &&
                Objects.equals(question, that.question) &&
                Objects.equals(answer, that.answer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, question, answer);
    }

    @Override
    public String toString() {
        return "CheckAnswerPO{" +
                "username='" + username + '\'' +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }

}
