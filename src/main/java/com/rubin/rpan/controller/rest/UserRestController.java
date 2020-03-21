package com.rubin.rpan.controller.rest;

import com.rubin.rpan.common.response.ServerResponse;
import com.rubin.rpan.entity.User;
import com.rubin.rpan.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * Created by rubin on 2019/9/7.
 * 项目rest接口返回
 */

@RestController
@RequestMapping("/user")
public class UserRestController {

    @Autowired
    private IUserService iUserService;

    /**
     * 用户注册
     *
     * @param user
     * @return
     */
    @PostMapping("/register")
    public ServerResponse<String> register(User user) {
        return iUserService.register(user);
    }

    /**
     * 用户登录
     *
     * @param user
     * @param session
     * @return
     */
    @PostMapping("/login")
    public ServerResponse<User> login(User user, HttpSession session) {
        return iUserService.login(user, session);
    }

    /**
     * 用户退出登录
     *
     * @param session
     * @return
     */
    @PostMapping("/exit")
    public ServerResponse<String> exit(HttpSession session) {
        return iUserService.exit(session);
    }

    /**
     * 获取登录的用户名
     *
     * @param session
     * @return
     */
    @GetMapping("/getUsername")
    public ServerResponse<String> getUsername(HttpSession session) {
        return iUserService.getUsername(session);
    }

}
