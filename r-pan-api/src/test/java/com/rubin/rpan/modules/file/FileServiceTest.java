package com.rubin.rpan.modules.file;

import com.rubin.rpan.RPanApplication;
import com.rubin.rpan.common.constant.CommonConstant;
import com.rubin.rpan.common.exception.RPanException;
import com.rubin.rpan.common.util.StringListUtil;
import com.rubin.rpan.modules.file.entity.RPanFile;
import com.rubin.rpan.modules.file.service.IFileService;
import com.rubin.rpan.modules.user.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Arrays;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;

/**
 * 物理文件业务测试类
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@SpringBootTest(classes = RPanApplication.class)
@RunWith(SpringRunner.class)
@Transactional
public class FileServiceTest {

    @Autowired
    @Qualifier(value = "fileService")
    private IFileService iFileService;

    @Autowired
    @Qualifier(value = "userService")
    private IUserService iUserService;

    /**
     * 测试保存物理文件成功
     */
    @Test
    @Rollback
    public void saveSuccessTest() {
        Long userId = register();
        Assert.assertNotNull(iFileService.save(generateMultipartFile(), userId));
    }

    /**
     * 测试删除物理文件成功
     */
    @Test
    @Rollback
    public void deleteSuccessTest() {
        Long userId = register();
        RPanFile rPanFile = iFileService.save(generateMultipartFile(), userId);
        Assert.assertNotNull(rPanFile);
        iFileService.delete(StringListUtil.longListToString(rPanFile.getFileId()));
    }

    /**
     * 测试删除物理文件-入参错误失败
     */
    @Test(expected = RPanException.class)
    @Rollback
    public void deleteErrorParamFailTest() {
        iFileService.delete(null);
    }

    /********************************************************************************私有********************************************************************************/

    /**
     * 注册用户
     *
     * @return
     */
    private Long register() {
        String userId = iUserService.register("test-user", "12345678", "test-question", "test-answer");
        Assert.assertNotNull(userId);
        return Long.valueOf(userId);
    }

    /**
     * 生成mock的MultipartFile实体
     *
     * @return
     */
    private MultipartFile generateMultipartFile() {
        MultipartFile multipartFile = null;
        try {
            multipartFile = new MockMultipartFile("file", "test.txt", ",multipart/form-data", "test upload content".getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return multipartFile;
    }

}
