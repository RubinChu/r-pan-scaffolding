package com.rubin.rpan.service;

import com.rubin.rpan.common.response.ServerResponse;
import com.rubin.rpan.entity.User;

import javax.servlet.http.HttpSession;

/**
 * 用户管理接口service
 *
 * @Description
 * @auther chuqian
 * @create 2019-09-20 9:55
 */
public interface IUserService {

    ServerResponse<String> register(User user);

    ServerResponse<User> login(User user, HttpSession session);

    ServerResponse<String> exit(HttpSession session);

    ServerResponse<String> getUsername(HttpSession session);
}
