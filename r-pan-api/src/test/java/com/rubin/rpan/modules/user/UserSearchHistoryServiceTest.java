package com.rubin.rpan.modules.user;

import com.rubin.rpan.RPanApplication;
import com.rubin.rpan.modules.user.service.IUserSearchHistoryService;
import com.rubin.rpan.modules.user.service.IUserService;
import com.rubin.rpan.modules.user.vo.RPanUserSearchHistoryVO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户搜索历史业务测试类
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@SpringBootTest(classes = RPanApplication.class)
@RunWith(SpringRunner.class)
@Transactional
public class UserSearchHistoryServiceTest {

    @Autowired
    @Qualifier(value = "userService")
    private IUserService iUserService;

    @Autowired
    @Qualifier(value = "userSearchHistoryService")
    private IUserSearchHistoryService iUserSearchHistoryService;

    /**
     * 测试获取用户最新的搜索历史列表，默认十条成功
     */
    @Test
    @Rollback
    public void listSuccessTest() {
        String userId = iUserService.register("test-user", "12345678", "test-question", "test-answer");
        Assert.assertNotNull(userId);
        List<RPanUserSearchHistoryVO> rPanUserSearchHistoryVOList = iUserSearchHistoryService.save("test-search-content", Long.valueOf(userId));
        Assert.assertEquals(1, rPanUserSearchHistoryVOList.size());
        rPanUserSearchHistoryVOList = iUserSearchHistoryService.list(Long.valueOf(userId));
        Assert.assertEquals(1, rPanUserSearchHistoryVOList.size());
    }

    /**
     * 测试保存用户搜索关键字，重复关键字会变为置顶状态成功
     */
    @Test
    @Rollback
    public void saveSuccessTest() {
        String userId = iUserService.register("test-user", "12345678", "test-question", "test-answer");
        Assert.assertNotNull(userId);
        List<RPanUserSearchHistoryVO> rPanUserSearchHistoryVOList = iUserSearchHistoryService.save("test-search-content", Long.valueOf(userId));
        Assert.assertEquals(1, rPanUserSearchHistoryVOList.size());
    }

}
