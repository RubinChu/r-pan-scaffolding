package com.rubin.rpan.service.impl;

import com.rubin.rpan.common.constants.Constants;
import com.rubin.rpan.common.response.ResponseCode;
import com.rubin.rpan.common.response.ServerResponse;
import com.rubin.rpan.dao.RPanUserMapper;
import com.rubin.rpan.entity.RPanUser;
import com.rubin.rpan.common.exception.RPanException;
import com.rubin.rpan.po.*;
import com.rubin.rpan.service.IFileService;
import com.rubin.rpan.service.IUserService;
import com.rubin.rpan.common.util.*;
import com.rubin.rpan.vo.RPanUserVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;

/**
 * @Description user related logic processing
 * @auther chuqian
 * @create 2019-09-20 9:56
 */

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements IUserService {

    private RPanUserMapper rPanUserMapper;

    private IFileService iFileService;

    private RPanUtil rPanUtil;

    private RedisUtil redisUtil;

    /**
     * user registration
     *
     * @param registerPO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse<String> register(RegisterPO registerPO) {
        ServerResponse serverResponse = checkRegisterParam(registerPO);
        if (!serverResponse.isSuccess()) {
            return serverResponse;
        }
        RPanUser rPanUser = assembleRPanUser(registerPO);
        try {
            if (rPanUserMapper.insertSelective(rPanUser) != Constants.ONE_INT) {
                return ServerResponse.createByErrorMessage("注册失败");
            }
        } catch (DuplicateKeyException e) {
            return ServerResponse.createByErrorMessage("用户名已存在");
        }
        if (iFileService.createDirectory(Constants.TOP_STR, String.valueOf(rPanUser.getId()), rPanUser.getId()).isSuccess()) {
            return ServerResponse.createBySuccess("注册成功");
        }
        throw new RPanException("注册失败");
    }

    /**
     * user login
     *
     * @param loginPO
     * @return
     */
    @Override
    public ServerResponse<String> login(LoginPO loginPO) {
        ServerResponse serverResponse = checkLoginParam(loginPO);
        if (!serverResponse.isSuccess()) {
            return serverResponse;
        }
        String inputUserName = loginPO.getUsername(),
                inputPassword = loginPO.getPassword();
        RPanUser rPanUser = rPanUserMapper.selectByUserName(inputUserName);
        if (Objects.isNull(rPanUser)) {
            return ServerResponse.createByErrorMessage("用户名错误");
        }
        String salt = rPanUser.getSalt(), dbPassword = rPanUser.getPassword();
        if (!Objects.equals(dbPassword, rPanUtil.encryptPassword(salt, inputPassword))) {
            return ServerResponse.createByErrorMessage("密码错误");
        }
        long oneDayLongMill = 1000L * 60L * 60L * 24L;
        String token = JwtUtil.generateToken(rPanUser, oneDayLongMill);
        redisUtil.setString(Constants.USER_LOGIN_PREFIX + rPanUser.getId(), token, oneDayLongMill);
        return ServerResponse.createBySuccess(token);
    }

    /**
     * get user info
     *
     * @param userId
     * @return
     */
    @Override
    public ServerResponse<RPanUserVO> info(Integer userId) {
        if (Objects.isNull(userId)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        RPanUserVO rPanUserVO = rPanUserMapper.selectUserInfoByPrimaryKey(userId);
        if (Objects.isNull(rPanUserVO)) {
            return ServerResponse.createByErrorMessage("无此用户");
        }
        return ServerResponse.createBySuccess(rPanUserVO);
    }

    /**
     * forgot password-verify username
     *
     * @param checkUsernamePO
     * @return
     */
    @Override
    public ServerResponse<String> checkUsername(CheckUsernamePO checkUsernamePO) {
        if (Objects.isNull(checkUsernamePO) || StringUtils.isEmpty(checkUsernamePO.getUsername())) {
            return ServerResponse.createByErrorMessage("请输入用户名");
        }
        RPanUser rPanUser = rPanUserMapper.selectByUserName(checkUsernamePO.getUsername());
        if (Objects.isNull(rPanUser)) {
            return ServerResponse.createByErrorMessage("没有此用户");
        }
        return ServerResponse.createBySuccess(rPanUser.getQuestion());
    }

    /**
     * forgot password-verify secret security answer
     *
     * @param checkAnswerPO
     * @return
     */
    @Override
    public ServerResponse<String> checkAnswer(CheckAnswerPO checkAnswerPO) {
        if (Objects.isNull(checkAnswerPO) ||
                StringUtils.isEmpty(checkAnswerPO.getUsername()) ||
                StringUtils.isEmpty(checkAnswerPO.getQuestion()) ||
                StringUtils.isEmpty(checkAnswerPO.getAnswer())) {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        if (rPanUserMapper.selectCountByUsernameAndQuestionAndAnswer(checkAnswerPO) > Constants.ZERO_INT) {
            String token = UUIDUtil.getUUID();
            LocalCacheUtil.setKey(checkAnswerPO.getUsername(), token);
            return ServerResponse.createBySuccess(token);
        }
        return ServerResponse.createByErrorMessage("密保答案错误");
    }

    /**
     * forgot password-reset password
     *
     * @param resetPasswordPO
     * @return
     */
    @Override
    public ServerResponse<String> resetPassword(ResetPasswordPO resetPasswordPO) {
        if (Objects.isNull(resetPasswordPO) ||
                StringUtils.isEmpty(resetPasswordPO.getUsername()) ||
                StringUtils.isEmpty(resetPasswordPO.getNewPassword()) ||
                StringUtils.isEmpty(resetPasswordPO.getToken())) {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        String newPassword = resetPasswordPO.getNewPassword(),
                username = resetPasswordPO.getUsername();
        if (newPassword.length() < 8 || newPassword.length() > 16) {
            return ServerResponse.createByErrorMessage("请输入8-16位的密码");
        }
        RPanUser rPanUser = rPanUserMapper.selectByUserName(username);
        if (Objects.isNull(rPanUser)) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        String token = LocalCacheUtil.getKey(username);
        if (Objects.isNull(token)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.TOKEN_EXPIRE.getCode(), ResponseCode.TOKEN_EXPIRE.getDesc());
        }
        if (!Objects.equals(token, resetPasswordPO.getToken())) {
            return ServerResponse.createByErrorMessage("token错误");
        }
        rPanUser.setPassword(rPanUtil.encryptPassword(rPanUser.getSalt(), newPassword))
                .setUpdateTime(new Date());
        if (rPanUserMapper.updateByPrimaryKeySelective(rPanUser) != Constants.ONE_INT) {
            return ServerResponse.createByErrorMessage("重置密码失败");
        }
        return ServerResponse.createBySuccess("重置密码成功");
    }

    /**
     * user change password
     *
     * @param changePasswordPO
     * @param userId
     * @return
     */
    @Override
    public ServerResponse<String> changePassword(ChangePasswordPO changePasswordPO, Integer userId) {
        if (Objects.isNull(userId)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        ServerResponse checkResult = checkChangePasswordParam(changePasswordPO);
        if (!checkResult.isSuccess()) {
            return checkResult;
        }
        RPanUser rPanUser = rPanUserMapper.selectByPrimaryKey(userId);
        String salt = rPanUser.getSalt();
        if (!Objects.equals(rPanUser.getPassword(), rPanUtil.encryptPassword(salt, changePasswordPO.getPassword()))) {
            return ServerResponse.createByErrorMessage("旧密码不正确");
        }
        rPanUser.setPassword(rPanUtil.encryptPassword(salt, changePasswordPO.getNewPassword()))
                .setUpdateTime(new Date());
        if (rPanUserMapper.updateByPrimaryKeySelective(rPanUser) != Constants.ONE_INT) {
            return ServerResponse.createByErrorMessage("修改密码失败");
        }
        return ServerResponse.createBySuccess("修改密码成功");
    }

    /**
     * 用户退出登录
     *
     * @param userId
     * @return
     */
    @Override
    public ServerResponse<String> exit(Integer userId) {
        if (redisUtil.del(Constants.USER_LOGIN_PREFIX + userId)) {
            return ServerResponse.createBySuccess("登出成功");
        }
        return ServerResponse.createByErrorMessage("登出失败");
    }

    /******************************************************私有****************************************************/

    /**
     * check user registration parameter rules
     *
     * @param registerPO
     * @return
     */
    private ServerResponse checkRegisterParam(RegisterPO registerPO) {
        if (Objects.isNull(registerPO)) {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        if (StringUtils.isEmpty(registerPO.getQuestion())) {
            return ServerResponse.createByErrorMessage("密保问题不能为空");
        }
        if (StringUtils.isEmpty(registerPO.getAnswer())) {
            return ServerResponse.createByErrorMessage("密保答案不能为空");
        }
        return checkUsernameAndPassword(registerPO.getUsername(), registerPO.getPassword());
    }

    /**
     * check login param
     *
     * @param loginPO
     * @return
     */
    private ServerResponse checkLoginParam(LoginPO loginPO) {
        if (Objects.isNull(loginPO)) {
            return ServerResponse.createByErrorMessage("用户名或密码不能为空");
        }
        return checkUsernameAndPassword(loginPO.getUsername(), loginPO.getPassword());
    }

    /**
     * check username and password
     *
     * @param username
     * @param password
     * @return
     */
    private ServerResponse checkUsernameAndPassword(String username, String password) {
        String regex = "^[0-9A-Za-z]{6,16}$";
        if (StringUtils.isEmpty(username)) {
            return ServerResponse.createByErrorMessage("用户名不能为空");
        }
        if (StringUtils.isEmpty(password)) {
            return ServerResponse.createByErrorMessage("密码不能为空");
        }
        if (!username.matches(regex)) {
            return ServerResponse.createByErrorMessage("请输入6-16位只包含数字和字母的用户名");
        }
        if (password.length() < 8 || password.length() > 16) {
            return ServerResponse.createByErrorMessage("请输入8-16位的密码");
        }
        return ServerResponse.createBySuccess();
    }

    /**
     * assemble user info
     *
     * @param registerPO
     * @return
     */
    private RPanUser assembleRPanUser(RegisterPO registerPO) {
        RPanUser rPanUser = new RPanUser();
        String salt = rPanUtil.getSalt(),
                dbPassword = rPanUtil.encryptPassword(salt, registerPO.getPassword());
        rPanUser.setSalt(salt)
                .setUsername(registerPO.getUsername())
                .setPassword(dbPassword)
                .setQuestion(registerPO.getQuestion())
                .setAnswer(registerPO.getAnswer())
                .setCreateTime(new Date())
                .setUpdateTime(new Date());
        return rPanUser;
    }

    /**
     * verify and change password parameters
     *
     * @param changePasswordPO
     * @return
     */
    private ServerResponse checkChangePasswordParam(ChangePasswordPO changePasswordPO) {
        if (Objects.isNull(changePasswordPO)) {
            return ServerResponse.createBySuccess("参数错误");
        }
        String password = changePasswordPO.getPassword(),
                newPassword = changePasswordPO.getNewPassword();
        if (StringUtils.isEmpty(password)) {
            return ServerResponse.createByErrorMessage("旧密码不能为空");
        }
        if (password.length() < 8 || password.length() > 16) {
            return ServerResponse.createByErrorMessage("请输入8-16位的旧密码");
        }
        if (StringUtils.isEmpty(newPassword)) {
            return ServerResponse.createByErrorMessage("新密码不能为空");
        }
        if (newPassword.length() < 8 || newPassword.length() > 16) {
            return ServerResponse.createByErrorMessage("请输入8-16位的新密码");
        }
        return ServerResponse.createBySuccess();
    }
}
