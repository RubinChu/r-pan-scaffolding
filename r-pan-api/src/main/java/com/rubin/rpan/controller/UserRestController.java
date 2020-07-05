package com.rubin.rpan.controller;

import com.rubin.rpan.common.response.ServerResponse;
import com.rubin.rpan.po.*;
import com.rubin.rpan.service.IUserService;
import com.rubin.rpan.common.util.RPanUtil;
import com.rubin.rpan.vo.RPanUserVO;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Project rest interface return
 * Created by rubin on 2019/9/7.
 */

@RestController
@RequestMapping("user")
@AllArgsConstructor
public class UserRestController {

    private IUserService iUserService;

    private RPanUtil rPanUtil;

    /**
     * user registration
     *
     * @param registerPO
     * @return
     */
    @PostMapping("register")
    public ServerResponse<String> register(@RequestBody RegisterPO registerPO) {
        return iUserService.register(registerPO);
    }

    /**
     * user login
     *
     * @param loginPO
     * @return
     */
    @PostMapping("login")
    public ServerResponse<String> login(@RequestBody LoginPO loginPO) {
        return iUserService.login(loginPO);
    }

    /**
     * user exit
     *
     * @return
     */
    @PostMapping("exit")
    public ServerResponse<String> exit(HttpServletRequest request) {
        return iUserService.exit(rPanUtil.getUserId(request));
    }

    /**
     * get user detail
     *
     * @param request
     * @return
     */
    @GetMapping("info")
    public ServerResponse<RPanUserVO> info(HttpServletRequest request) {
        return iUserService.info(rPanUtil.getUserId(request));
    }

    /**
     * forgot password-verify username
     *
     * @param checkUsernamePO
     * @return
     */
    @PostMapping("username/check")
    public ServerResponse<String> checkUsername(@RequestBody CheckUsernamePO checkUsernamePO) {
        return iUserService.checkUsername(checkUsernamePO);
    }

    /**
     * forgot password-verify secret security answer
     *
     * @param checkAnswerPO
     * @return
     */
    @PostMapping("answer/check")
    public ServerResponse<String> checkAnswer(@RequestBody CheckAnswerPO checkAnswerPO) {
        return iUserService.checkAnswer(checkAnswerPO);
    }

    /**
     * forgot password-reset password
     *
     * @param resetPasswordPO
     * @return
     */
    @PostMapping("password/reset")
    public ServerResponse<String> resetPassword(@RequestBody ResetPasswordPO resetPasswordPO) {
        return iUserService.resetPassword(resetPasswordPO);
    }

    /**
     * user change password
     *
     * @param changePasswordPO
     * @param request
     * @return
     */
    @PostMapping("password/change")
    public ServerResponse<String> changePassword(@RequestBody ChangePasswordPO changePasswordPO, HttpServletRequest request) {
        return iUserService.changePassword(changePasswordPO, rPanUtil.getUserId(request));
    }

}
