package com.rubin.rpan.modules.share;

import com.rubin.rpan.RPanApplication;
import com.rubin.rpan.common.constant.CommonConstant;
import com.rubin.rpan.common.exception.RPanException;
import com.rubin.rpan.common.util.IdGenerator;
import com.rubin.rpan.common.util.StringListUtil;
import com.rubin.rpan.modules.file.service.IUserFileService;
import com.rubin.rpan.modules.file.vo.RPanUserFileVO;
import com.rubin.rpan.modules.share.constant.ShareConstant;
import com.rubin.rpan.modules.share.service.IShareService;
import com.rubin.rpan.modules.share.vo.RPanUserShareDetailVO;
import com.rubin.rpan.modules.share.vo.RPanUserShareSimpleDetailVO;
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
 * 分享业务测试类
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@SpringBootTest(classes = RPanApplication.class)
@RunWith(SpringRunner.class)
@Transactional
public class ShareServiceTest {

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
     * 测试创建分享链接成功
     */
    @Test
    @Rollback
    public void createSuccessTest() {
        Long userId = register();
        List<RPanUserFileVO> rPanUserFileVOList = createFolder(userId);
        String fileIds = StringUtils.join(rPanUserFileVOList.stream().map(RPanUserFileVO::getFileId).collect(Collectors.toList()), CommonConstant.COMMON_SEPARATOR);
        RPanUserShareUrlVO rPanUserShareUrlVO = iShareService.create("test-share-name",
                ShareConstant.ShareType.NEED_SHARE_CODE.getCode(),
                ShareConstant.ShareDayType.PERMANENT_VALIDITY.getCode(),
                fileIds,
                userId);
        Assert.assertNotNull(rPanUserShareUrlVO);
    }

    /**
     * 测试查询分享列表成功
     */
    @Test
    @Rollback
    public void listSuccessTest() {
        Long userId = register();
        List<RPanUserFileVO> rPanUserFileVOList = createFolder(userId);
        String fileIds = StringUtils.join(rPanUserFileVOList.stream().map(RPanUserFileVO::getFileId).collect(Collectors.toList()), CommonConstant.COMMON_SEPARATOR);
        RPanUserShareUrlVO rPanUserShareUrlVO = iShareService.create("test-share-name",
                ShareConstant.ShareType.NEED_SHARE_CODE.getCode(),
                ShareConstant.ShareDayType.PERMANENT_VALIDITY.getCode(),
                fileIds,
                userId);
        Assert.assertNotNull(rPanUserShareUrlVO);
        List<RPanUserShareUrlVO> rPanUserShareUrlVOList = iShareService.list(userId);
        Assert.assertEquals(1, rPanUserShareUrlVOList.size());
    }

    /**
     * 测试取消分享成功
     */
    @Test
    @Rollback
    public void cancelSuccessTest() {
        Long userId = register();
        List<RPanUserFileVO> rPanUserFileVOList = createFolder(userId);
        String fileIds = StringUtils.join(rPanUserFileVOList.stream().map(RPanUserFileVO::getFileId).collect(Collectors.toList()), CommonConstant.COMMON_SEPARATOR);
        RPanUserShareUrlVO rPanUserShareUrlVO = iShareService.create("test-share-name",
                ShareConstant.ShareType.NEED_SHARE_CODE.getCode(),
                ShareConstant.ShareDayType.PERMANENT_VALIDITY.getCode(),
                fileIds,
                userId);
        Assert.assertNotNull(rPanUserShareUrlVO);
        iShareService.cancel(StringListUtil.longListToString(rPanUserShareUrlVO.getShareId()), userId);
    }

    /**
     * 测试查询分享链接详情成功
     */
    @Test
    @Rollback
    public void detailSuccessTest() {
        Long userId = register();
        List<RPanUserFileVO> rPanUserFileVOList = createFolder(userId);
        String fileIds = StringUtils.join(rPanUserFileVOList.stream().map(RPanUserFileVO::getFileId).collect(Collectors.toList()), CommonConstant.COMMON_SEPARATOR);
        RPanUserShareUrlVO rPanUserShareUrlVO = iShareService.create("test-share-name",
                ShareConstant.ShareType.NEED_SHARE_CODE.getCode(),
                ShareConstant.ShareDayType.PERMANENT_VALIDITY.getCode(),
                fileIds,
                userId);
        Assert.assertNotNull(rPanUserShareUrlVO);
        RPanUserShareDetailVO rPanUserShareDetailVO = iShareService.detail(rPanUserShareUrlVO.getShareId());
        Assert.assertNotNull(rPanUserShareDetailVO);
    }

    /**
     * 测试查询简单分享链接详情成功
     */
    @Test
    @Rollback
    public void simpleDetailSuccessTest() {
        Long userId = register();
        List<RPanUserFileVO> rPanUserFileVOList = createFolder(userId);
        String fileIds = StringUtils.join(rPanUserFileVOList.stream().map(RPanUserFileVO::getFileId).collect(Collectors.toList()), CommonConstant.COMMON_SEPARATOR);
        RPanUserShareUrlVO rPanUserShareUrlVO = iShareService.create("test-share-name",
                ShareConstant.ShareType.NEED_SHARE_CODE.getCode(),
                ShareConstant.ShareDayType.PERMANENT_VALIDITY.getCode(),
                fileIds,
                userId);
        Assert.assertNotNull(rPanUserShareUrlVO);
        RPanUserShareSimpleDetailVO rPanUserShareSimpleDetailVO = iShareService.simpleDetail(rPanUserShareUrlVO.getShareId());
        Assert.assertNotNull(rPanUserShareSimpleDetailVO);
    }

    /**
     * 测试校验分享码成功
     */
    @Test
    @Rollback
    public void checkShareCodeSuccessTest() {
        Long userId = register();
        List<RPanUserFileVO> rPanUserFileVOList = createFolder(userId);
        String fileIds = StringUtils.join(rPanUserFileVOList.stream().map(RPanUserFileVO::getFileId).collect(Collectors.toList()), CommonConstant.COMMON_SEPARATOR);
        RPanUserShareUrlVO rPanUserShareUrlVO = iShareService.create("test-share-name",
                ShareConstant.ShareType.NEED_SHARE_CODE.getCode(),
                ShareConstant.ShareDayType.PERMANENT_VALIDITY.getCode(),
                fileIds,
                userId);
        Assert.assertNotNull(rPanUserShareUrlVO);
        String token = iShareService.checkShareCode(rPanUserShareUrlVO.getShareId(), rPanUserShareUrlVO.getShareCode());
        Assert.assertNotNull(token);
    }

    /**
     * 测试校验分享码成功
     */
    @Test(expected = RPanException.class)
    @Rollback
    public void checkShareCodeFailTest() {
        Long userId = register();
        List<RPanUserFileVO> rPanUserFileVOList = createFolder(userId);
        String fileIds = StringUtils.join(rPanUserFileVOList.stream().map(RPanUserFileVO::getFileId).collect(Collectors.toList()), CommonConstant.COMMON_SEPARATOR);
        RPanUserShareUrlVO rPanUserShareUrlVO = iShareService.create("test-share-name",
                ShareConstant.ShareType.NEED_SHARE_CODE.getCode(),
                ShareConstant.ShareDayType.PERMANENT_VALIDITY.getCode(),
                fileIds,
                userId);
        Assert.assertNotNull(rPanUserShareUrlVO);
        String token = iShareService.checkShareCode(rPanUserShareUrlVO.getShareId(), StringUtils.reverse(rPanUserShareUrlVO.getShareCode()));
        Assert.assertNotNull(token);
    }

    /**
     * 测试通过文件id修改相关的分享状态成功
     */
    @Test
    @Rollback
    public void changeShareStatusSuccessTest() {
        Long userId = register();
        List<RPanUserFileVO> rPanUserFileVOList = createFolder(userId);
        String fileIds = StringUtils.join(rPanUserFileVOList.stream().map(RPanUserFileVO::getFileId).collect(Collectors.toList()), CommonConstant.COMMON_SEPARATOR);
        RPanUserShareUrlVO rPanUserShareUrlVO = iShareService.create("test-share-name",
                ShareConstant.ShareType.NEED_SHARE_CODE.getCode(),
                ShareConstant.ShareDayType.PERMANENT_VALIDITY.getCode(),
                fileIds,
                userId);
        Assert.assertNotNull(rPanUserShareUrlVO);
        iShareService.refreshShareStatus(fileIds);
    }

    /**
     * 测试获取下一级的文件列表成功
     */
    @Test
    @Rollback
    public void fileListSuccessTest() {
        Long userId = register();
        List<RPanUserFileVO> rPanUserFileVOList = createFolder(userId);
        String fileIds = StringUtils.join(rPanUserFileVOList.stream().map(RPanUserFileVO::getFileId).collect(Collectors.toList()), CommonConstant.COMMON_SEPARATOR);
        RPanUserFileVO parentFolder = rPanUserFileVOList.stream().findFirst().get();
        RPanUserShareUrlVO rPanUserShareUrlVO = iShareService.create("test-share-name",
                ShareConstant.ShareType.NEED_SHARE_CODE.getCode(),
                ShareConstant.ShareDayType.PERMANENT_VALIDITY.getCode(),
                fileIds,
                userId);
        Assert.assertNotNull(rPanUserShareUrlVO);
        rPanUserFileVOList = iShareService.fileList(rPanUserShareUrlVO.getShareId(), parentFolder.getFileId());
        Assert.assertEquals(0, rPanUserFileVOList.size());
    }

    /**
     * 测试获取下一级的文件列表-分享状态异常失败
     */
    @Test(expected = RPanException.class)
    @Rollback
    public void fileListShareStatusErrorFailTest() {
        Long userId = register();
        List<RPanUserFileVO> rPanUserFileVOList = createFolder(userId);
        String fileIds = StringUtils.join(rPanUserFileVOList.stream().map(RPanUserFileVO::getFileId).collect(Collectors.toList()), CommonConstant.COMMON_SEPARATOR);
        RPanUserFileVO parentFolder = rPanUserFileVOList.stream().findFirst().get();
        RPanUserShareUrlVO rPanUserShareUrlVO = iShareService.create("test-share-name",
                ShareConstant.ShareType.NEED_SHARE_CODE.getCode(),
                ShareConstant.ShareDayType.PERMANENT_VALIDITY.getCode(),
                fileIds,
                userId);
        Assert.assertNotNull(rPanUserShareUrlVO);
        iUserFileService.delete(info(userId).getRootFileId(), fileIds, userId);
        rPanUserFileVOList = iShareService.fileList(rPanUserShareUrlVO.getShareId(), parentFolder.getFileId());
        Assert.assertEquals(0, rPanUserFileVOList.size());
    }

    /**
     * 测试保存分享的文文件到自己文件夹成功
     */
    @Test
    @Rollback
    public void saveSuccessTest() {
        Long userId = register();
        List<RPanUserFileVO> rPanUserFileVOList = createFolder(userId);
        String fileIds = StringUtils.join(rPanUserFileVOList.stream().map(RPanUserFileVO::getFileId).collect(Collectors.toList()), CommonConstant.COMMON_SEPARATOR);

        RPanUserShareUrlVO rPanUserShareUrlVO = iShareService.create("test-share-name",
                ShareConstant.ShareType.NEED_SHARE_CODE.getCode(),
                ShareConstant.ShareDayType.PERMANENT_VALIDITY.getCode(),
                fileIds,
                userId);
        Assert.assertNotNull(rPanUserShareUrlVO);
        Long saveUserId = register("save-username");
        RPanUserFileVO parentFolder = createFolder(saveUserId).stream().findFirst().get();
        iShareService.save(rPanUserShareUrlVO.getShareId(), fileIds, parentFolder.getFileId(), saveUserId);
    }

    /**
     * 测试保存分享的文文件到自己文件夹失败
     */
    @Test(expected = RPanException.class)
    @Rollback
    public void saveShareStatusErrorFailTest() {
        Long userId = register();
        List<RPanUserFileVO> rPanUserFileVOList = createFolder(userId);
        String fileIds = StringUtils.join(rPanUserFileVOList.stream().map(RPanUserFileVO::getFileId).collect(Collectors.toList()), CommonConstant.COMMON_SEPARATOR);
        RPanUserShareUrlVO rPanUserShareUrlVO = iShareService.create("test-share-name",
                ShareConstant.ShareType.NEED_SHARE_CODE.getCode(),
                ShareConstant.ShareDayType.PERMANENT_VALIDITY.getCode(),
                fileIds,
                userId);
        Assert.assertNotNull(rPanUserShareUrlVO);
        iUserFileService.delete(info(userId).getRootFileId(), fileIds, userId);
        Long saveUserId = register("save-username");
        RPanUserFileVO parentFolder = createFolder(saveUserId).stream().findFirst().get();
        iShareService.save(rPanUserShareUrlVO.getShareId(), fileIds, parentFolder.getFileId(), saveUserId);
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
