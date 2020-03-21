package com.rubin.rpan.service.impl;

import com.google.common.base.Strings;
import com.rubin.rpan.common.base.BaseService;
import com.rubin.rpan.common.constants.Constants;
import com.rubin.rpan.common.response.ServerResponse;
import com.rubin.rpan.dao.UserMapper;
import com.rubin.rpan.entity.User;
import com.rubin.rpan.service.IUserService;
import com.rubin.rpan.util.MD5Util;
import com.rubin.rpan.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.Date;

/**
 * @Description 用户相关逻辑处理
 * @auther chuqian
 * @create 2019-09-20 9:56
 */

@Service
public class UserServiceImpl extends BaseService implements IUserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 用户注册
     *
     * @param user
     * @return
     */
    @Override
    public ServerResponse<String> register(User user) {
        ServerResponse serverResponse = checkUserParam(user);
        if (!serverResponse.isSuccess()) {
            return serverResponse;
        }
        user.setSalt(MD5Util.getMD5(UUIDUtil.getUUID()));
        user.setPassword(MD5Util.getMD5(MD5Util.getMD5(user.getPassword()) + user.getSalt()));
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        int num;
        try {
            num = userMapper.insertSelective(user);
        } catch (DuplicateKeyException e) {
            return ServerResponse.createByErrorMessage("用户名已存在");
        }
        if (num != 1) {
            return ServerResponse.createByErrorMessage("注册失败");
        }
        File folder = new File(getFileRealPathStr(Constants.EMPTY_STR, user.getUsername()));
        folder.mkdirs();
        return ServerResponse.createBySuccessMessage("注册成功");
    }

    /**
     * 用户登录
     *
     * @param user
     * @return
     */
    @Override
    public ServerResponse<User> login(User user, HttpSession session) {
        ServerResponse serverResponse = checkUserParam(user);
        if (!serverResponse.isSuccess()) {
            return serverResponse;
        }
        String inputUserName = user.getUsername(),
                inputPassword = user.getPassword();
        user = userMapper.selectByUserName(inputUserName);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户名错误");
        }
        String salt = user.getSalt(), dbPassword = user.getPassword();
        if (!MD5Util.getMD5(MD5Util.getMD5(inputPassword) + salt).equals(dbPassword)) {
            return ServerResponse.createByErrorMessage("密码错误");
        }
        user.setSalt(Constants.EMPTY_STR);
        user.setPassword(Constants.EMPTY_STR);

        session.setAttribute(Constants.LOGIN_SESSION_KEY, inputUserName);
        session.setMaxInactiveInterval(60 * 60 * 24);

        return ServerResponse.createBySuccess(user);
    }

    /**
     * 用户退出登录
     *
     * @param session
     * @return
     */
    @Override
    public ServerResponse<String> exit(HttpSession session) {
        session.removeAttribute(Constants.LOGIN_SESSION_KEY);
        return ServerResponse.createBySuccess("退出登录成功");
    }

    /**
     * 获取登录的用户名
     *
     * @param session
     * @return
     */
    @Override
    public ServerResponse<String> getUsername(HttpSession session) {
        return ServerResponse.createBySuccess(session.getAttribute(Constants.LOGIN_SESSION_KEY).toString());
    }

    /******************************************************私有****************************************************/

    /**
     * 检查用户名密码规则
     *
     * @param user
     * @return
     */
    private ServerResponse checkUserParam(User user) {
        String regex = "^[0-9A-Za-z]{6,16}$";
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户名或密码不能为空");
        }
        if (Strings.isNullOrEmpty(user.getUsername())) {
            return ServerResponse.createByErrorMessage("用户名不能为空");
        }
        if (Strings.isNullOrEmpty(user.getPassword())) {
            return ServerResponse.createByErrorMessage("密码不能为空");
        }
        if (!user.getUsername().matches(regex)) {
            return ServerResponse.createByErrorMessage("请输入6-16位只包含数字和字母的用户名");
        }
        if (user.getPassword().length() < 8 || user.getPassword().length() > 16) {
            return ServerResponse.createByErrorMessage("请输入8-16位的密码");
        }
        return ServerResponse.createBySuccess();
    }
}
