package com.rubin.rpan.modules.user.controller;

import com.rubin.rpan.common.annotation.NeedLogin;
import com.rubin.rpan.common.response.R;
import com.rubin.rpan.common.util.UserIdUtil;
import com.rubin.rpan.modules.user.service.IUserSearchHistoryService;
import com.rubin.rpan.modules.user.vo.RPanUserSearchHistoryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 项目用户搜索历史相关rest接口返回
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@RestController
@Api(tags = "用户搜索历史")
public class UserSearchHistoryRestController {

    @Autowired
    @Qualifier(value = "userSearchHistoryService")
    private IUserSearchHistoryService iUserSearchHistoryService;

    /**
     * 获取用户最新的搜索历史列表，默认十条
     *
     * @return
     */
    @ApiOperation(
            value = "获取用户最新的搜索历史列表，默认十条",
            notes = "该接口提供了获取用户最新的搜索历史列表，默认十条的功能",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @GetMapping("user/search/histories")
    @NeedLogin
    public R<List<RPanUserSearchHistoryVO>> list() {
        return R.data(iUserSearchHistoryService.list(UserIdUtil.get()));
    }

}
