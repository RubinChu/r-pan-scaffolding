package com.rubin.rpan.controller.view;

import com.rubin.rpan.common.constants.Constants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by rubin on 2019/9/7.
 * 项目视图界面返回
 */

@Controller
public class PanViewController {

    /**
     * 首页
     *
     * @return
     */
    @RequestMapping("/")
    public String index() {
        return "index";
    }

    /**
     * 视频播放页面
     *
     * @return
     */
    @RequestMapping("/video")
    public String video() {
        return "video";
    }

    /**
     * 音频播放页面
     *
     * @return
     */
    @RequestMapping("/music")
    public String music() {
        return "music";
    }

    /**
     * 登录页面
     *
     * @return
     */
    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * 注册页面
     *
     * @return
     */
    @RequestMapping("/register")
    public String register() {
        return "register";
    }

    /**
     * office pdf预览页面返回
     *
     * @param fileShowPath
     * @param model
     * @return
     */
    @RequestMapping("/iframe")
    public String iframe(String fileShowPath, Model model) {
        model.addAttribute(Constants.FILE_SHOW_PATH_STR, fileShowPath);
        return "iframe";
    }

    /**
     * 代码预览返回
     *
     * @return
     */
    @RequestMapping("/code")
    public String iframe() {
        return "code";
    }
}
