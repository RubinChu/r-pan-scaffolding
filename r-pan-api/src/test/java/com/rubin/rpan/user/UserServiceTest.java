package com.rubin.rpan.user;

import com.rubin.rpan.RPanApplication;
import com.rubin.rpan.common.response.ServerResponse;
import com.rubin.rpan.po.*;
import com.rubin.rpan.service.IUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

/**
 * Created by rubin on 2020/5/30.
 */

@SpringBootTest(classes = RPanApplication.class)
@RunWith(SpringRunner.class)
public class UserServiceTest {

    @Autowired
    private IUserService iUserService;

    @Test
    public void registerTest() {
        RegisterPO registerPO = new RegisterPO();
        registerPO.setUsername("RubinChu");
        registerPO.setPassword("12345678");
        registerPO.setQuestion("test question");
        registerPO.setAnswer("test answer");
        assertServerResponse(iUserService.register(registerPO));
    }

    @Test
    public void loginTest() {
        LoginPO loginPO = new LoginPO();
        loginPO.setUsername("RubinChu");
        loginPO.setPassword("12345678");
        assertServerResponse(iUserService.login(loginPO));
    }

    @Test
    public void infoTest() {
        assertServerResponse(iUserService.info(1));
    }

    @Test
    public void checkUsernameTest() {
        CheckUsernamePO checkUsernamePO = new CheckUsernamePO();
        checkUsernamePO.setUsername("RubinChu");
        assertServerResponse(iUserService.checkUsername(checkUsernamePO));
    }

    @Test
    public void checkAnswerTest() {
        CheckAnswerPO checkAnswerPO = new CheckAnswerPO();
        checkAnswerPO.setUsername("RubinChu");
        checkAnswerPO.setQuestion("test question");
        checkAnswerPO.setAnswer("test answer");
        assertServerResponse(iUserService.checkAnswer(checkAnswerPO));
    }

    @Test
    public void resetPasswordTest() {
        CheckAnswerPO checkAnswerPO = new CheckAnswerPO();
        checkAnswerPO.setUsername("RubinChu");
        checkAnswerPO.setQuestion("test question");
        checkAnswerPO.setAnswer("test answer");

        ResetPasswordPO resetPasswordPO = new ResetPasswordPO();
        resetPasswordPO.setUsername("RubinChu");
        resetPasswordPO.setNewPassword("87654321");
        resetPasswordPO.setToken(iUserService.checkAnswer(checkAnswerPO).getData());
        assertServerResponse(iUserService.resetPassword(resetPasswordPO));
    }

    @Test
    public void changePasswordTest() {
        ChangePasswordPO changePasswordPO = new ChangePasswordPO();
        changePasswordPO.setPassword("87654321");
        changePasswordPO.setNewPassword("12345678");
        assertServerResponse(iUserService.changePassword(changePasswordPO, 1));
    }

    @Test
    public void exitTest() {
        assertServerResponse(iUserService.exit(1));
    }

    private void assertServerResponse(ServerResponse serverResponse) {
        System.out.println(serverResponse.getData());
        Assert.isTrue(serverResponse.isSuccess(), serverResponse.getMessage());
    }

}
