package com.rubin.rpan.modules.user;

import com.rubin.rpan.RPanApplication;
import com.rubin.rpan.common.constant.CommonConstant;
import com.rubin.rpan.common.exception.RPanException;
import com.rubin.rpan.common.util.JwtUtil;
import com.rubin.rpan.modules.share.vo.ShareUserInfoVO;
import com.rubin.rpan.modules.user.service.IUserService;
import com.rubin.rpan.modules.user.vo.RPanUserVO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户业务测试类
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@SpringBootTest(classes = RPanApplication.class)
@RunWith(SpringRunner.class)
@Transactional
public class UserServiceTest {

    @Autowired
    @Qualifier(value = "userService")
    private IUserService iUserService;

    /**
     * 测试注册成功
     */
    @Test
    @Rollback
    public void registerSuccessTest() {
        String userId = iUserService.register("test-user", "12345678", "test-question", "test-answer");
        Assert.assertNotNull(userId);
    }

    /**
     * 测试注册-用户名已存在
     */
    @Test(expected = RPanException.class)
    @Rollback
    public void registerDuplicateUsernameTest() {
        String userId = iUserService.register("test-user", "12345678", "test-question", "test-answer");
        Assert.assertNotNull(userId);
        iUserService.register("test-user", "12345678", "test-question", "test-answer");
    }

    /**
     * 测试登录成功
     */
    @Test
    @Rollback
    public void loginSuccessTest() {
        String username = "test-user",
                password = "12345678";
        String userId = iUserService.register(username, password, "test-question", "test-answer");
        Assert.assertNotNull(userId);
        String token = iUserService.login(username, password);
        Assert.assertNotNull(token);
    }

    /**
     * 测试登录-用户名错误
     */
    @Test(expected = RPanException.class)
    @Rollback
    public void loginUsernameWrongTest() {
        String username = "test-user",
                password = "12345678";
        String userId = iUserService.register(username, password, "test-question", "test-answer");
        Assert.assertNotNull(userId);
        iUserService.login(username + "1", password);
    }

    /**
     * 测试登录-密码错误
     */
    @Test(expected = RPanException.class)
    @Rollback
    public void loginPasswordWrongTest() {
        String username = "test-user",
                password = "12345678";
        String userId = iUserService.register(username, password, "test-question", "test-answer");
        Assert.assertNotNull(userId);
        iUserService.login(username, password + "1");
    }

    /**
     * 测试查询用户信息成功
     */
    @Test
    @Rollback
    public void getUserInfoSuccessTest() {
        String userId = iUserService.register("test-user", "12345678", "test-question", "test-answer");
        Assert.assertNotNull(userId);
        RPanUserVO rPanUserVO = iUserService.info(Long.valueOf(userId));
        Assert.assertNotNull(rPanUserVO);
    }

    /**
     * 测试忘记密码-校验用户名成功
     */
    @Test
    @Rollback
    public void checkUsernameSuccessTest() {
        String username = "test-user";
        String userId = iUserService.register(username, "12345678", "test-question", "test-answer");
        Assert.assertNotNull(userId);
        String question = iUserService.checkUsername(username);
        Assert.assertNotNull(question);
    }

    /**
     * 测试忘记密码-校验用户名错误失败
     */
    @Test(expected = RPanException.class)
    @Rollback
    public void checkUsernameFailTest() {
        String username = "test-user";
        String userId = iUserService.register(username, "12345678", "test-question", "test-answer");
        Assert.assertNotNull(userId);
        String question = iUserService.checkUsername(username + "1");
        Assert.assertNotNull(question);
    }

    /**
     * 测试忘记密码-校验密保答案成功
     */
    @Test
    @Rollback
    public void checkAnswerSuccessTest() {
        String username = "test-user",
                answer = "test-answer";
        String userId = iUserService.register(username, "12345678", "test-question", answer);
        Assert.assertNotNull(userId);
        String question = iUserService.checkUsername(username);
        Assert.assertNotNull(question);
        String token = iUserService.checkAnswer(username, question, answer);
        Assert.assertNotNull(token);
    }

    /**
     * 测试忘记密码-校验密保答案错误失败
     */
    @Test(expected = RPanException.class)
    @Rollback
    public void checkAnswerFailTest() {
        String username = "test-user",
                answer = "test-answer";
        String userId = iUserService.register(username, "12345678", "test-question", answer);
        Assert.assertNotNull(userId);
        String question = iUserService.checkUsername(username);
        Assert.assertNotNull(question);
        iUserService.checkAnswer(username, question, answer + "1");
    }

    /**
     * 测试忘记密码-重置密码成功
     */
    @Test
    @Rollback
    public void resetPasswordSuccessTest() {
        String username = "test-user",
                answer = "test-answer";
        String userId = iUserService.register(username, "12345678", "test-question", answer);
        Assert.assertNotNull(userId);
        String question = iUserService.checkUsername(username);
        Assert.assertNotNull(question);
        String token = iUserService.checkAnswer(username, question, answer);
        Assert.assertNotNull(token);
        iUserService.resetPassword(username, "87654321", token);
    }

    /**
     * 测试忘记密码-重置密码token错误失败
     */
    @Test(expected = RPanException.class)
    @Rollback
    public void resetPasswordFailTest() {
        String username = "test-user",
                answer = "test-answer";
        String userId = iUserService.register(username, "12345678", "test-question", answer);
        Assert.assertNotNull(userId);
        String question = iUserService.checkUsername(username);
        Assert.assertNotNull(question);
        String token = iUserService.checkAnswer(username, question, answer);
        Assert.assertNotNull(token);
        iUserService.resetPassword(username, "87654321", token + "1");
    }

    /**
     * 测试用户修改密码成功
     */
    @Test
    @Rollback
    public void changePasswordSuccessTest() {
        String username = "test-user",
                password = "12345678";
        String userId = iUserService.register(username, password, "test-question", "test-answer");

        iUserService.changePassword(password, "87654321", Long.valueOf(userId));
    }

    /**
     * 测试用户修改密码-旧密码错误失败
     */
    @Test(expected = RPanException.class)
    @Rollback
    public void changePasswordFailTest() {
        String username = "test-user",
                password = "12345678";
        String userId = iUserService.register(username, password, "test-question", "test-answer");

        iUserService.changePassword(password + "1", "87654321", Long.valueOf(userId));
    }

    /**
     * 测试退出登陆成功
     */
    @Test
    @Rollback
    public void exitTest() {
        String username = "test-user",
                password = "12345678";
        String userId = iUserService.register(username, password, "test-question", "test-answer");
        Assert.assertNotNull(userId);
        String token = iUserService.login(username, password);
        Assert.assertNotNull(token);
        iUserService.exit(Long.valueOf(String.valueOf(JwtUtil.analyzeToken(token, CommonConstant.LOGIN_USER_ID))));
    }

    /**
     * 测试获取分享的用户信息成功
     */
    @Test
    @Rollback
    public void getShareUserInfoTest() {
        String username = "test-user",
                password = "12345678";
        String userId = iUserService.register(username, password, "test-question", "test-answer");
        Assert.assertNotNull(userId);
        ShareUserInfoVO shareUserInfoVO = iUserService.getShareUserInfo(Long.valueOf(userId));
        Assert.assertNotNull(shareUserInfoVO);
    }

}
