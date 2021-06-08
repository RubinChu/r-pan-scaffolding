package com.rubin.rpan.modules.share;

import com.rubin.rpan.RPanApplication;
import com.rubin.rpan.common.constant.CommonConstant;
import com.rubin.rpan.common.util.IdGenerator;
import com.rubin.rpan.common.util.StringListUtil;
import com.rubin.rpan.modules.file.service.IUserFileService;
import com.rubin.rpan.modules.file.vo.RPanUserFileVO;
import com.rubin.rpan.modules.share.constant.ShareConstant;
import com.rubin.rpan.modules.share.service.IShareFileService;
import com.rubin.rpan.modules.share.service.IShareService;
import com.rubin.rpan.modules.share.vo.RPanUserShareUrlVO;
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
 * 分享列表业务测试类
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@SpringBootTest(classes = RPanApplication.class)
@RunWith(SpringRunner.class)
@Transactional
public class ShareFileServiceTest {

    @Autowired
    @Qualifier(value = "shareFileService")
    private IShareFileService iShareFileService;

    @Autowired
    @Qualifier(value = "shareService")
    private IShareService iShareService;

    @Autowired
    @Qualifier(value = "userFileService")
    private IUserFileService iUserFileService;

    @Autowired
    @Qualifier(value = "userService")
    private IUserService iUserService;

    @Autowired
    @Qualifier(value = "idGenerator")
    private IdGenerator idGenerator;

    /**
     * 测试批量保存分享文件信息成功
     */
    @Test
    @Rollback
    public void saveBatchSuccessTest() {
        iShareFileService.saveBatch(idGenerator.nextId(), "1__,__2", idGenerator.nextId());
    }

    /**
     * 测试批量取消分享文件信息成功
     */
    @Test
    @Rollback
    public void cancelShareFilesSuccessTest() {
        Long shareId = idGenerator.nextId();
        iShareFileService.saveBatch(shareId, "1__,__2", idGenerator.nextId());
        iShareFileService.cancelShareFiles(StringListUtil.longListToString(shareId));
    }

    /**
     * 测试获取分享的文件列表成功
     */
    @Test
    @Rollback
    public void getShareFileInfosSuccessTest() {
        Long userId = register();
        List<RPanUserFileVO> rPanUserFileVOList = createFolder(userId);
        String fileIds = StringUtils.join(rPanUserFileVOList.stream().map(RPanUserFileVO::getFileId).collect(Collectors.toList()), CommonConstant.COMMON_SEPARATOR);
        RPanUserShareUrlVO rPanUserShareUrlVO = iShareService.create("test-share-name",
                ShareConstant.ShareType.NEED_SHARE_CODE.getCode(),
                ShareConstant.ShareDayType.PERMANENT_VALIDITY.getCode(),
                fileIds,
                userId);
        Assert.assertNotNull(rPanUserShareUrlVO);
        rPanUserFileVOList = iShareFileService.getShareFileInfos(rPanUserShareUrlVO.getShareId());
        Assert.assertEquals(1, rPanUserFileVOList.size());
    }

    /**
     * 测试通过文件id查找对应的分享id成功
     */
    @Test
    @Rollback
    public void getShareIdByFileIdsSuccessTest() {
        Long userId = register();
        List<RPanUserFileVO> rPanUserFileVOList = createFolder(userId);
        String fileIds = StringUtils.join(rPanUserFileVOList.stream().map(RPanUserFileVO::getFileId).collect(Collectors.toList()), CommonConstant.COMMON_SEPARATOR);
        RPanUserShareUrlVO rPanUserShareUrlVO = iShareService.create("test-share-name",
                ShareConstant.ShareType.NEED_SHARE_CODE.getCode(),
                ShareConstant.ShareDayType.PERMANENT_VALIDITY.getCode(),
                fileIds,
                userId);
        Assert.assertNotNull(rPanUserShareUrlVO);
        List<Long> shareIdList = iShareFileService.getShareIdByFileIds(fileIds);
        Assert.assertEquals(1, shareIdList.size());
    }

    /**
     * 测试获取该分享的所有文件列表成功
     */
    @Test
    @Rollback
    public void getAllShareFileInfosSuccessTest() {
        Long userId = register();
        List<RPanUserFileVO> rPanUserFileVOList = createFolder(userId);
        String fileIds = StringUtils.join(rPanUserFileVOList.stream().map(RPanUserFileVO::getFileId).collect(Collectors.toList()), CommonConstant.COMMON_SEPARATOR);
        RPanUserShareUrlVO rPanUserShareUrlVO = iShareService.create("test-share-name",
                ShareConstant.ShareType.NEED_SHARE_CODE.getCode(),
                ShareConstant.ShareDayType.PERMANENT_VALIDITY.getCode(),
                fileIds,
                userId);
        Assert.assertNotNull(rPanUserShareUrlVO);
        rPanUserFileVOList = iShareFileService.getAllShareFileInfos(rPanUserShareUrlVO.getShareId());
        Assert.assertEquals(1, rPanUserFileVOList.size());
    }

    /**
     * 测试获取分享的文件列表成功
     */
    @Test
    @Rollback
    public void getShareFileIdsSuccessTest() {
        Long userId = register();
        List<RPanUserFileVO> rPanUserFileVOList = createFolder(userId);
        String fileIds = StringUtils.join(rPanUserFileVOList.stream().map(RPanUserFileVO::getFileId).collect(Collectors.toList()), CommonConstant.COMMON_SEPARATOR);
        RPanUserShareUrlVO rPanUserShareUrlVO = iShareService.create("test-share-name",
                ShareConstant.ShareType.NEED_SHARE_CODE.getCode(),
                ShareConstant.ShareDayType.PERMANENT_VALIDITY.getCode(),
                fileIds,
                userId);
        Assert.assertNotNull(rPanUserShareUrlVO);
        List<Long> fileIdsByShareId = iShareFileService.getFileIdsByShareId(rPanUserShareUrlVO.getShareId());
        Assert.assertEquals(1, fileIdsByShareId.size());
    }

    /********************************************************************************私有********************************************************************************/

    /**
     * 注册用户
     *
     * @return
     */
    private Long register() {
        return register("test-username");
    }

    /**
     * 注册用户
     *
     * @param username
     * @return
     */
    private Long register(String username) {
        String userId = iUserService.register(username, "12345678", "test-question", "test-answer");
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
     * 创建文件夹
     *
     * @param userId
     * @return
     */
    private List<RPanUserFileVO> createFolder(Long userId) {
        RPanUserVO rPanUserVO = info(userId);
        List<RPanUserFileVO> rPanUserFileVOList = iUserFileService.createFolder(rPanUserVO.getRootFileId(), "test-folder-1", userId);
        Assert.assertEquals(1, rPanUserFileVOList.size());
        return rPanUserFileVOList;
    }

}
