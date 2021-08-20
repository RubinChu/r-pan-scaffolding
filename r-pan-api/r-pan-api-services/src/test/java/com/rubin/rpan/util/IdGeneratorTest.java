package com.rubin.rpan.util;

import com.rubin.rpan.RPanApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * id生成器测试类
 */
@SpringBootTest(classes = RPanApplication.class)
@RunWith(SpringRunner.class)
public class IdGeneratorTest {

    @Test
    public void getIdTest() {
        for (int i = 0; i < 10; i++) {
            System.out.println(IdGenerator.nextId());
        }
    }

}
