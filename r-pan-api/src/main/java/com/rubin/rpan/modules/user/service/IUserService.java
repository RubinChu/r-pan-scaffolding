package com.rubin.rpan.modules.user.service;

import com.rubin.rpan.modules.share.vo.ShareUserInfoVO;
import com.rubin.rpan.modules.user.vo.RPanUserVO;

/**
 * 用户业务处理接口
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
public interface IUserService {

    String register(String username, String password, String question, String answer);

    String login(String username, String password);

    RPanUserVO info(String userId);

    String checkUsername(String username);

    String checkAnswer(String username, String question, String answer);

    void resetPassword(String username, String newPassword, String token);

    void changePassword(String password, String newPassword, String userId);

    void exit(String userId);

    ShareUserInfoVO getShareUserInfo(String userId);

}
