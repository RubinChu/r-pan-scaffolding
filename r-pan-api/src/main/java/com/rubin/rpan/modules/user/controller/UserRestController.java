package com.rubin.rpan.modules.user.controller;

import com.rubin.rpan.common.annotation.NeedLogin;
import com.rubin.rpan.common.response.R;
import com.rubin.rpan.common.util.UserIdUtil;
import com.rubin.rpan.modules.user.po.*;
import com.rubin.rpan.modules.user.service.IUserService;
import com.rubin.rpan.modules.user.vo.RPanUserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 项目用户相关rest接口返回
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@RestController
@Api(tags = "用户")
public class UserRestController {

    @Autowired
    @Qualifier(value = "userService")
    private IUserService iUserService;

    /**
     * 用户注册
     *
     * @param registerPO
     * @return
     */
    @ApiOperation(
            value = "用户注册",
            notes = "该接口提供了用户注册的功能",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @PostMapping("user/register")
    public R register(@Validated @RequestBody RegisterPO registerPO) {
        iUserService.register(registerPO.getUsername(), registerPO.getPassword(), registerPO.getQuestion(), registerPO.getAnswer());
        return R.success();
    }


    /**
     * 用户登录
     *
     * @param loginPO
     * @return
     */
    @ApiOperation(
            value = "用户登录",
            notes = "该接口提供了用户登录的功能",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @PostMapping("user/login")
    public R<String> login(@Validated @RequestBody LoginPO loginPO) {
        return R.data(iUserService.login(loginPO.getUsername(), loginPO.getPassword()));
    }

    /**
     * 用户退出登录
     *
     * @return
     */
    @ApiOperation(
            value = "用户退出登录",
            notes = "该接口提供了用户退出登录的功能",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @PostMapping("user/exit")
    @NeedLogin
    public R exit() {
        iUserService.exit(UserIdUtil.get());
        return R.success();
    }

    /**
     * 获取用户详情
     *
     * @return
     */
    @ApiOperation(
            value = "获取用户详情",
            notes = "该接口提供了预览获取用户详情的功能",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @GetMapping("user")
    @NeedLogin
    public R<RPanUserVO> info() {
        return R.data(iUserService.info(UserIdUtil.get()));
    }

    /**
     * 忘记密码-校验用户名
     *
     * @param checkUsernamePO
     * @return
     */
    @ApiOperation(
            value = "忘记密码-校验用户名",
            notes = "该接口提供了忘记密码-校验用户名的功能",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @PostMapping("user/username/check")
    public R<String> checkUsername(@Validated @RequestBody CheckUsernamePO checkUsernamePO) {
        return R.data(iUserService.checkUsername(checkUsernamePO.getUsername()));
    }

    /**
     * 忘记密码-校验密保答案
     *
     * @param checkAnswerPO
     * @return
     */
    @ApiOperation(
            value = "忘记密码-校验密保答案",
            notes = "该接口提供了忘记密码-校验密保答案的功能",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @PostMapping("user/answer/check")
    public R<String> checkAnswer(@Validated @RequestBody CheckAnswerPO checkAnswerPO) {
        return R.data(iUserService.checkAnswer(checkAnswerPO.getUsername(), checkAnswerPO.getQuestion(), checkAnswerPO.getAnswer()));
    }

    /**
     * 忘记密码-重置密码
     *
     * @param resetPasswordPO
     * @return
     */
    @ApiOperation(
            value = "忘记密码-重置密码",
            notes = "该接口提供了忘记密码-重置密码的功能",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @PostMapping("user/password/reset")
    public R resetPassword(@Validated @RequestBody ResetPasswordPO resetPasswordPO) {
        iUserService.resetPassword(resetPasswordPO.getUsername(), resetPasswordPO.getNewPassword(), resetPasswordPO.getToken());
        return R.success();
    }

    /**
     * 用户修改密码
     *
     * @param changePasswordPO
     * @return
     */
    @ApiOperation(
            value = "用户修改密码",
            notes = "该接口提供了用户修改密码的功能",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @PostMapping("user/password/change")
    @NeedLogin
    public R changePassword(@Validated @RequestBody ChangePasswordPO changePasswordPO) {
        iUserService.changePassword(changePasswordPO.getPassword(), changePasswordPO.getNewPassword(), UserIdUtil.get());
        return R.success();
    }

}
