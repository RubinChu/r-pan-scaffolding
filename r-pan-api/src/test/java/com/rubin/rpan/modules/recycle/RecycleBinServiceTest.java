package com.rubin.rpan.modules.recycle;

import com.rubin.rpan.RPanApplication;
import com.rubin.rpan.common.constant.CommonConstant;
import com.rubin.rpan.common.exception.RPanException;
import com.rubin.rpan.common.util.StringListUtil;
import com.rubin.rpan.modules.file.service.IUserFileService;
import com.rubin.rpan.modules.file.vo.RPanUserFileVO;
import com.rubin.rpan.modules.recycle.service.IRecycleBinService;
import com.rubin.rpan.modules.user.service.IUserService;
import com.rubin.rpan.modules.user.vo.RPanUserVO;
import org.apache.commons.lang3.StringUtils;
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
import java.util.stream.Collectors;

/**
 * 回收站业务测试类
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@SpringBootTest(classes = RPanApplication.class)
@RunWith(SpringRunner.class)
@Transactional
public class RecycleBinServiceTest {

    @Autowired
    @Qualifier(value = "recycleBinService")
    private IRecycleBinService iRecycleBinService;

    @Autowired
    @Qualifier(value = "userFileService")
    private IUserFileService iUserFileService;

    @Autowired
    @Qualifier(value = "userService")
    private IUserService iUserService;

    /**
     * 测试回收站列表成功
     */
    @Test
    @Rollback
    public void listSuccessTest() {
        Long userId = deleteFile();
        List<RPanUserFileVO> rPanUserFileVOList = iRecycleBinService.list(userId);
        Assert.assertEquals(1, rPanUserFileVOList.size());
    }

    /**
     * 测试回收站还原文件成功
     */
    @Test
    @Rollback
    public void restoreSuccessTest() {
        Long userId = deleteFile();
        List<RPanUserFileVO> rPanUserFileVOList = iRecycleBinService.list(userId);
        Assert.assertEquals(1, rPanUserFileVOList.size());
        List<Long> fileIdList = rPanUserFileVOList.stream().map(RPanUserFileVO::getFileId).collect(Collectors.toList());
        String fileIds = StringListUtil.longListToString(fileIdList);
        rPanUserFileVOList = iRecycleBinService.restore(fileIds, userId);
        Assert.assertEquals(0, rPanUserFileVOList.size());
    }

    /**
     * 测试回收站还原文件失败：还原文件中存在名称相同并且父目录相同的被删除文件
     */
    @Test(expected = RPanException.class)
    @Rollback
    public void restoreSameNameFileFailTest() {
        Long userId = deleteSameNameFile();
        List<RPanUserFileVO> rPanUserFileVOList = iRecycleBinService.list(userId);
        Assert.assertEquals(2, rPanUserFileVOList.size());
        List<Long> fileIdList = rPanUserFileVOList.stream().map(RPanUserFileVO::getFileId).collect(Collectors.toList());
        String fileIds = StringListUtil.longListToString(fileIdList);
        iRecycleBinService.restore(fileIds, userId);
    }

    /**
     * 测试回收站还原文件失败：还原文件中存在名称相同与父目录相同的未被删除的文件
     */
    @Test(expected = RPanException.class)
    @Rollback
    public void restoreSameNameWithExistFileFailTest() {
        Long userId = deleteAndCreateSameNameFile();
        List<RPanUserFileVO> rPanUserFileVOList = iRecycleBinService.list(userId);
        Assert.assertEquals(1, rPanUserFileVOList.size());
        List<Long> fileIdList = rPanUserFileVOList.stream().map(RPanUserFileVO::getFileId).collect(Collectors.toList());
        String fileIds = StringListUtil.longListToString(fileIdList);
        iRecycleBinService.restore(fileIds, userId);
    }

    /**
     * 测试回收站删除文件成功
     */
    @Test
    @Rollback
    public void deleteSuccessTest() {
        Long userId = deleteFile();
        List<RPanUserFileVO> rPanUserFileVOList = iRecycleBinService.list(userId);
        Assert.assertEquals(1, rPanUserFileVOList.size());
        List<Long> fileIdList = rPanUserFileVOList.stream().map(RPanUserFileVO::getFileId).collect(Collectors.toList());
        String fileIds = StringListUtil.longListToString(fileIdList);
        rPanUserFileVOList = iRecycleBinService.delete(fileIds, userId);
        Assert.assertEquals(0, rPanUserFileVOList.size());
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
     * 查询用户信息
     *
     * @param userId
     * @return
     */
    private RPanUserVO info(Long userId) {
        RPanUserVO rPanUserVO = iUserService.info(userId);
        Assert.assertNotNull(rPanUserVO);
        return rPanUserVO;
    }

    /**
     * 删除文件
     *
     * @return
     */
    private Long deleteFile() {
        Long userId = register();
        RPanUserVO rPanUserVO = info(userId);
        RPanUserFileVO folder = createFolder(rPanUserVO.getRootFileId(), userId);
        deleteFile(rPanUserVO.getRootFileId(), userId, folder.getFileId());
        return userId;
    }

    /**
     * 创建一个文件夹
     *
     * @param parentId
     * @param userId
     * @return
     */
    private RPanUserFileVO createFolder(Long parentId, Long userId) {
        String folderName = "test-folder-1";
        List<RPanUserFileVO> rPanUserFileVOList = iUserFileService.createFolder(parentId, folderName, userId);
        Assert.assertTrue(rPanUserFileVOList.size() > 0);
        return rPanUserFileVOList.stream().filter(rPanUserFileVO -> folderName.equals(rPanUserFileVO.getFilename())).findFirst().get();
    }

    /**
     * 删除文件
     *
     * @param parentId
     * @param userId
     * @param fileId
     */
    private void deleteFile(Long parentId, Long userId, Long fileId) {
        iUserFileService.delete(parentId, StringListUtil.longListToString(fileId), userId);
    }

    /**
     * 删除两个相同名称的文件
     *
     * @return
     */
    private Long deleteSameNameFile() {
        Long userId = register();
        RPanUserVO rPanUserVO = info(userId);
        RPanUserFileVO folder = createFolder(rPanUserVO.getRootFileId(), userId);
        deleteFile(rPanUserVO.getRootFileId(), userId, folder.getFileId());
        folder = createFolder(rPanUserVO.getRootFileId(), userId);
        deleteFile(rPanUserVO.getRootFileId(), userId, folder.getFileId());
        return userId;
    }

    /**
     * 删除一个文件夹 并创建一个相同的文件夹
     *
     * @return
     */
    private Long deleteAndCreateSameNameFile() {
        Long userId = register();
        RPanUserVO rPanUserVO = info(userId);
        RPanUserFileVO folder = createFolder(rPanUserVO.getRootFileId(), userId);
        deleteFile(rPanUserVO.getRootFileId(), userId, folder.getFileId());
        createFolder(rPanUserVO.getRootFileId(), userId);
        return userId;
    }

}
