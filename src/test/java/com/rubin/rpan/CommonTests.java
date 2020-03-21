package com.rubin.rpan;

import com.rubin.rpan.util.MD5Util;
import com.rubin.rpan.util.UUIDUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 公共方法相关单元测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CommonTests {

    @Test
    public void UUIDTest() {
        System.out.println(UUIDUtil.getUUID());
    }

    @Test
    public void MD5Test() {
        System.out.println(MD5Util.getMD5("29C9D60EC3F647DA8BA16BED06F15E04"));
    }

}
