package com.rubin.rpan.modules.user.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * 用户信息实体类
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
public class RPanUser implements Serializable {

    private static final long serialVersionUID = -3128274535492573630L;

    public RPanUser() {
    }

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 盐值
     */
    private String salt;

    /**
     * 密保问题
     */
    private String question;

    /**
     * 密保答案
     */
    private String answer;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RPanUser rPanUser = (RPanUser) o;
        return Objects.equals(userId, rPanUser.userId) &&
                Objects.equals(username, rPanUser.username) &&
                Objects.equals(password, rPanUser.password) &&
                Objects.equals(salt, rPanUser.salt) &&
                Objects.equals(question, rPanUser.question) &&
                Objects.equals(answer, rPanUser.answer) &&
                Objects.equals(createTime, rPanUser.createTime) &&
                Objects.equals(updateTime, rPanUser.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, password, salt, question, answer, createTime, updateTime);
    }

    @Override
    public String toString() {
        return "RPanUser{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }

}