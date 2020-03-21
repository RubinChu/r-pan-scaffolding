package com.rubin.rpan;

import com.rubin.rpan.dao.UserMapper;
import com.rubin.rpan.entity.User;
import com.rubin.rpan.service.IUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * 用户相关单元测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTests {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private UserMapper userMapper;

    @Test
    public void insertTest() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("123456");
        user.setSalt("123456");
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        try {
            userMapper.insertSelective(user);
        } catch (DuplicateKeyException e) {
            System.out.println("用户名已存在");
        }
    }

    @Test
    public void updateTest() {
        User user = new User();
        user.setId(1);
        user.setUsername("test");
        user.setUpdateTime(new Date());
        try {
            userMapper.updateByPrimaryKeySelective(user);
        } catch (DuplicateKeyException e) {
            System.out.println("用户名已存在");
        }
    }

    @Test
    public void selectTest() {
        User user = userMapper.selectByPrimaryKey(1);
        if (user == null) {
            System.out.println("null");
            return;
        }
        System.out.println(user.toString());
    }

    @Test
    public void deleteTest() {
        int num = userMapper.deleteByPrimaryKey(1);
        System.out.println(num);
    }

    @Test
    public void registerTest() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("123456");
        System.out.println(iUserService.register(user).getMessage());
    }

}
