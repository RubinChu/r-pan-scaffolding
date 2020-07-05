package com.rubin.rpan.service;

import com.rubin.rpan.common.response.ServerResponse;
import com.rubin.rpan.po.*;
import com.rubin.rpan.vo.RPanUserVO;

/**
 * @Description
 * @auther chuqian
 * @create 2019-09-20 9:55
 */
public interface IUserService {

    ServerResponse<String> register(RegisterPO registerPO);

    ServerResponse<String> login(LoginPO loginPO);

    ServerResponse<RPanUserVO> info(Integer userId);

    ServerResponse<String> checkUsername(CheckUsernamePO checkUsernamePO);

    ServerResponse<String> checkAnswer(CheckAnswerPO checkAnswerPO);

    ServerResponse<String> resetPassword(ResetPasswordPO resetPasswordPO);

    ServerResponse<String> changePassword(ChangePasswordPO changePasswordPO, Integer userId);

    ServerResponse<String> exit(Integer userId);
}
