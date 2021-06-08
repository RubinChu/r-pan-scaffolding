package com.rubin.rpan.modules.user.service.impl;

import com.rubin.rpan.common.constant.CommonConstant;
import com.rubin.rpan.common.exception.RPanException;
import com.rubin.rpan.common.response.ResponseCode;
import com.rubin.rpan.common.util.*;
import com.rubin.rpan.modules.file.constant.FileConstant;
import com.rubin.rpan.modules.file.entity.RPanUserFile;
import com.rubin.rpan.modules.file.service.IUserFileService;
import com.rubin.rpan.modules.share.vo.ShareUserInfoVO;
import com.rubin.rpan.modules.user.constant.UserConstant;
import com.rubin.rpan.modules.user.dao.RPanUserMapper;
import com.rubin.rpan.modules.user.entity.RPanUser;
import com.rubin.rpan.modules.user.service.IUserService;
import com.rubin.rpan.modules.user.vo.RPanUserVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;

/**
 * 用户业务处理实现
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@Service(value = "userService")
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements IUserService {

    @Autowired
    @Qualifier(value = "rPanUserMapper")
    private RPanUserMapper rPanUserMapper;

    @Autowired
    @Qualifier(value = "userFileService")
    private IUserFileService iUserFileService;

    @Autowired
    @Qualifier(value = "redisUtil")
    private RedisUtil redisUtil;

    @Autowired
    @Qualifier(value = "idGenerator")
    private IdGenerator idGenerator;

    /**
     * 用户注册
     *
     * @param username
     * @param password
     * @param question
     * @param answer
     * @return
     */
    @Override
    public String register(String username, String password, String question, String answer) {
        RPanUser rPanUser = assembleRPanUser(username, password, question, answer);
        saveUserInfo(rPanUser);
        createUserRootFolder(rPanUser);
        return rPanUser.getUserId().toString();
    }

    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    public String login(String username, String password) {
        RPanUser rPanUser = checkUsernameAndPassword(username, password);
        String token = generateAndSaveToken(rPanUser);
        return token;
    }

    /**
     * 获取用户详情
     *
     * @param userId
     * @return
     */
    @Override
    public RPanUserVO info(Long userId) {
        RPanUser rPanUser = rPanUserMapper.selectByPrimaryKey(userId);
        RPanUserFile rPanUserFile = iUserFileService.getUserTopFileInfo(userId);
        return assembleRPanUserVO(rPanUser, rPanUserFile);
    }

    /**
     * 忘记密码-校验用户名
     *
     * @param username
     * @return
     */
    @Override
    public String checkUsername(String username) {
        String question = rPanUserMapper.selectQuestionByUsername(username);
        if (StringUtils.isBlank(question)) {
            throw new RPanException("没有此用户");
        }
        return question;
    }

    /**
     * 忘记密码-校验密保答案
     *
     * @param username
     * @param question
     * @param answer
     * @return
     */
    @Override
    public String checkAnswer(String username, String question, String answer) {
        if (rPanUserMapper.selectCountByUsernameAndQuestionAndAnswer(username, question, answer) > CommonConstant.ZERO_INT) {
            return generateAndSaveCheckAnswerToken(username);
        }
        throw new RPanException("密保答案错误");
    }

    /**
     * 忘记密码-重置密码
     *
     * @param username
     * @param newPassword
     * @param token
     */
    @Override
    public void resetPassword(String username, String newPassword, String token) {
        RPanUser rPanUser = checkUsernameExistAndGet(username);
        checkResetPasswordToken(username, token);
        doResetPassword(rPanUser, newPassword);
    }

    /**
     * 用户修改密码
     *
     * @param password
     * @param newPassword
     * @param userId
     */
    @Override
    public void changePassword(String password, String newPassword, Long userId) {
        RPanUser rPanUser = checkOldPasswordAndGet(userId, password);
        doChangePassword(rPanUser, newPassword);
    }

    /**
     * 用户退出登录
     *
     * @param userId
     * @return
     */
    @Override
    public void exit(Long userId) {
        if (redisUtil.del(CommonConstant.USER_LOGIN_PREFIX + userId)) {
            return;
        }
        throw new RPanException("登出失败");
    }

    /**
     * 获取分享的用户信息
     *
     * @param userId
     * @return
     */
    @Override
    public ShareUserInfoVO getShareUserInfo(Long userId) {
        ShareUserInfoVO shareUserInfoVO = rPanUserMapper.selectShareUserInfoVOByUserId(userId);
        encryptUsername(shareUserInfoVO);
        return shareUserInfoVO;
    }

    /***********************************************************私有***********************************************************/

    /**
     * 拼装用户信息
     *
     * @return
     */
    private RPanUser assembleRPanUser(String username, String password, String question, String answer) {
        RPanUser rPanUser = new RPanUser();
        String salt = PasswordUtil.getSalt(),
                dbPassword = PasswordUtil.encryptPassword(salt, password);
        rPanUser.setUserId(idGenerator.nextId());
        rPanUser.setSalt(salt);
        rPanUser.setUsername(username);
        rPanUser.setPassword(dbPassword);
        rPanUser.setQuestion(question);
        rPanUser.setAnswer(answer);
        rPanUser.setCreateTime(new Date());
        rPanUser.setUpdateTime(new Date());
        return rPanUser;
    }

    /**
     * 保存用户信息
     *
     * @param rPanUser
     */
    private void saveUserInfo(RPanUser rPanUser) {
        try {
            if (rPanUserMapper.insertSelective(rPanUser) != CommonConstant.ONE_INT) {
                throw new RPanException("注册失败");
            }
        } catch (DuplicateKeyException e) {
            throw new RPanException("用户名已存在");
        }
    }

    /**
     * 创建用户根目录信息
     *
     * @param rPanUser
     */
    private void createUserRootFolder(RPanUser rPanUser) {
        iUserFileService.createFolder(CommonConstant.ZERO_LONG, FileConstant.ALL_FILE_CN_STR, rPanUser.getUserId());
    }

    /**
     * 校验用户名密码
     *
     * @param username
     * @param password
     * @return
     */
    private RPanUser checkUsernameAndPassword(String username, String password) {
        RPanUser rPanUser = rPanUserMapper.selectByUsername(username);
        if (Objects.isNull(rPanUser)) {
            throw new RPanException("用户名错误");
        }
        String salt = rPanUser.getSalt(), dbPassword = rPanUser.getPassword();
        if (!Objects.equals(dbPassword, PasswordUtil.encryptPassword(salt, password))) {
            throw new RPanException("密码错误");
        }
        return rPanUser;
    }

    /**
     * 生成登陆token并存在Redis
     *
     * @param rPanUser
     * @return
     */
    private String generateAndSaveToken(RPanUser rPanUser) {
        String token = JwtUtil.generateToken(rPanUser.getUsername(), CommonConstant.LOGIN_USER_ID, rPanUser.getUserId(), CommonConstant.ONE_DAY_LONG);
        redisUtil.set(CommonConstant.USER_LOGIN_PREFIX + rPanUser.getUserId(), token, CommonConstant.ONE_DAY_LONG);
        return token;
    }

    /**
     * 拼装用户信息返回实体
     *
     * @param rPanUser
     * @param rPanUserFile
     * @return
     */
    private RPanUserVO assembleRPanUserVO(RPanUser rPanUser, RPanUserFile rPanUserFile) {
        RPanUserVO rPanUserVO = new RPanUserVO();
        rPanUserVO.setUsername(rPanUser.getUsername());
        rPanUserVO.setRootFileId(rPanUserFile.getFileId());
        rPanUserVO.setRootFilename(rPanUserFile.getFilename());
        return rPanUserVO;
    }

    /**
     * 生成并保存忘记密码-校验完密保密码之后的token
     *
     * @param username
     * @return
     */
    private String generateAndSaveCheckAnswerToken(String username) {
        String token = UUIDUtil.getUUID();
        redisUtil.set(UserConstant.USER_FORGET_PREFIX + username, token, CommonConstant.FIVE_MINUTES_LONG);
        return token;
    }

    /**
     * 检查用户名是否存在并返回用户实体
     *
     * @param username
     * @return
     */
    private RPanUser checkUsernameExistAndGet(String username) {
        RPanUser rPanUser = rPanUserMapper.selectByUsername(username);
        if (Objects.isNull(rPanUser)) {
            throw new RPanException("用户名不存在");
        }
        return rPanUser;
    }

    /**
     * 校验重置密码token是否正确
     *
     * @param username
     * @param inputToken
     * @return
     */
    private void checkResetPasswordToken(String username, String inputToken) {
        Object token = redisUtil.get(UserConstant.USER_FORGET_PREFIX + username);
        if (Objects.isNull(token)) {
            throw new RPanException(ResponseCode.TOKEN_EXPIRE.getCode(), ResponseCode.TOKEN_EXPIRE.getDesc());
        }
        if (!Objects.equals(token, inputToken)) {
            throw new RPanException("token错误");
        }
    }

    /**
     * 重置密码
     *
     * @param rPanUser
     * @param newPassword
     */
    private void doResetPassword(RPanUser rPanUser, String newPassword) {
        rPanUser.setPassword(PasswordUtil.encryptPassword(rPanUser.getSalt(), newPassword));
        rPanUser.setUpdateTime(new Date());
        if (rPanUserMapper.updateByPrimaryKeySelective(rPanUser) != CommonConstant.ONE_INT) {
            throw new RPanException("重置密码失败");
        }
    }

    /**
     * 校验旧密码并返回用户信息
     *
     * @param userId
     * @param password
     * @return
     */
    private RPanUser checkOldPasswordAndGet(Long userId, String password) {
        RPanUser rPanUser = rPanUserMapper.selectByPrimaryKey(userId);
        if (!Objects.equals(rPanUser.getPassword(), PasswordUtil.encryptPassword(rPanUser.getSalt(), password))) {
            throw new RPanException("旧密码不正确");
        }
        return rPanUser;
    }

    /**
     * 修改用户密码
     *
     * @param rPanUser
     * @param newPassword
     */
    private void doChangePassword(RPanUser rPanUser, String newPassword) {
        rPanUser.setPassword(PasswordUtil.encryptPassword(rPanUser.getSalt(), newPassword));
        rPanUser.setUpdateTime(new Date());
        if (rPanUserMapper.updateByPrimaryKeySelective(rPanUser) != CommonConstant.ONE_INT) {
            throw new RPanException("修改密码失败");
        }
    }

    /**
     * 加密用户名
     *
     * @param shareUserInfoVO
     */
    private void encryptUsername(ShareUserInfoVO shareUserInfoVO) {
        String username = shareUserInfoVO.getUsername();
        StringBuffer stringBuffer = new StringBuffer(username);
        stringBuffer.replace(CommonConstant.TWO_INT, username.length() - CommonConstant.TWO_INT, UserConstant.USERNAME_ENCRYPTION_STR);
        shareUserInfoVO.setUsername(stringBuffer.toString());
    }

}
