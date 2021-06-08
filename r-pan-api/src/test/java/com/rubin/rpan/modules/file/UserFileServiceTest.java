package com.rubin.rpan.modules.file;

import com.rubin.rpan.RPanApplication;
import com.rubin.rpan.common.constant.CommonConstant;
import com.rubin.rpan.common.exception.RPanException;
import com.rubin.rpan.common.util.StringListUtil;
import com.rubin.rpan.modules.file.constant.FileConstant;
import com.rubin.rpan.modules.file.entity.RPanUserFile;
import com.rubin.rpan.modules.file.service.IUserFileService;
import com.rubin.rpan.modules.file.vo.BreadcrumbVO;
import com.rubin.rpan.modules.file.vo.FolderTreeNode;
import com.rubin.rpan.modules.file.vo.RPanUserFileVO;
import com.rubin.rpan.modules.user.service.IUserService;
import com.rubin.rpan.modules.user.vo.RPanUserVO;
import org.apache.commons.lang3.StringUtils;
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

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 用户文件业务测试类
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@SpringBootTest(classes = RPanApplication.class)
@RunWith(SpringRunner.class)
@Transactional
public class UserFileServiceTest {

    @Autowired
    @Qualifier(value = "userFileService")
    private IUserFileService iUserFileService;

    @Autowired
    @Qualifier(value = "userService")
    private IUserService iUserService;

    /**
     * 测试查询文件列表成功
     */
    @Test
    @Rollback
    public void listSuccessTest() {
        Long userId = register();
        RPanUserVO rPanUserVO = info(userId);
        List<RPanUserFileVO> rPanUserFileVOList = iUserFileService.list(rPanUserVO.getRootFileId(), FileConstant.ALL_FILE_TYPE, userId);
        Assert.assertEquals(0, rPanUserFileVOList.size());
        rPanUserFileVOList = iUserFileService.list(rPanUserVO.getRootFileId(), FileConstant.ALL_FILE_TYPE, userId, FileConstant.DelFlagEnum.NO.getCode());
        Assert.assertEquals(0, rPanUserFileVOList.size());
        rPanUserFileVOList = iUserFileService.list(StringListUtil.longListToString(rPanUserVO.getRootFileId()));
        Assert.assertEquals(1, rPanUserFileVOList.size());
    }

    /**
     * 测试创建文件夹成功
     */
    @Test
    @Rollback
    public void createFolderSuccessTest() {
        Long userId = register();
        RPanUserVO rPanUserVO = info(userId);
        List<RPanUserFileVO> rPanUserFileVOList = iUserFileService.createFolder(rPanUserVO.getRootFileId(), "test-folder", userId);
        Assert.assertEquals(1, rPanUserFileVOList.size());
    }

    /**
     * 测试创建文件夹重复自动重命名成功
     */
    @Test
    @Rollback
    public void createFolderHandleSameNameSuccessTest() {
        Long userId = register();
        RPanUserVO rPanUserVO = info(userId);
        List<RPanUserFileVO> rPanUserFileVOList = iUserFileService.createFolder(rPanUserVO.getRootFileId(), "test-folder", userId);
        Assert.assertEquals(1, rPanUserFileVOList.size());
        rPanUserFileVOList = iUserFileService.createFolder(rPanUserVO.getRootFileId(), "test-folder", userId);
        Assert.assertEquals(2, rPanUserFileVOList.size());
    }

    /**
     * 测试修改文件名称成功
     */
    @Test
    @Rollback
    public void updateFilenameSuccessTest() {
        Long userId = register();
        RPanUserVO rPanUserVO = info(userId);
        List<RPanUserFileVO> rPanUserFileVOList = iUserFileService.createFolder(rPanUserVO.getRootFileId(), "test-folder", userId);
        RPanUserFileVO rPanUserFileVO = rPanUserFileVOList.stream().findFirst().get();
        rPanUserFileVOList = iUserFileService.updateFilename(rPanUserFileVO.getFileId(), rPanUserFileVO.getFilename() + "_update", userId);
        Assert.assertEquals(1, rPanUserFileVOList.size());
    }

    /**
     * 测试修改文件名称-没有此文件失败
     */
    @Test(expected = RPanException.class)
    @Rollback
    public void updateFilenameNoThatFileFailTest() {
        Long userId = register();
        RPanUserVO rPanUserVO = info(userId);
        List<RPanUserFileVO> rPanUserFileVOList = iUserFileService.createFolder(rPanUserVO.getRootFileId(), "test-folder", userId);
        Assert.assertEquals(1, rPanUserFileVOList.size());
        RPanUserFileVO rPanUserFileVO = rPanUserFileVOList.stream().findFirst().get();
        rPanUserFileVOList = iUserFileService.updateFilename(0L, rPanUserFileVO.getFilename() + "_update", userId);
        Assert.assertEquals(1, rPanUserFileVOList.size());
    }

    /**
     * 测试修改文件名称-未更新新文件名失败
     */
    @Test(expected = RPanException.class)
    @Rollback
    public void updateFilenameNoNewFilenameFailTest() {
        Long userId = register();
        RPanUserVO rPanUserVO = info(userId);
        List<RPanUserFileVO> rPanUserFileVOList = iUserFileService.createFolder(rPanUserVO.getRootFileId(), "test-folder", userId);
        Assert.assertEquals(1, rPanUserFileVOList.size());
        RPanUserFileVO rPanUserFileVO = rPanUserFileVOList.stream().findFirst().get();
        rPanUserFileVOList = iUserFileService.updateFilename(rPanUserFileVO.getFileId(), rPanUserFileVO.getFilename(), userId);
        Assert.assertEquals(1, rPanUserFileVOList.size());
    }

    /**
     * 测试修改文件名称-新文件名已存在失败
     */
    @Test(expected = RPanException.class)
    @Rollback
    public void updateFilenameNewFilenameExitFailTest() {
        Long userId = register();
        RPanUserVO rPanUserVO = info(userId);
        List<RPanUserFileVO> rPanUserFileVOList = iUserFileService.createFolder(rPanUserVO.getRootFileId(), "test-folder-1", userId);
        Assert.assertEquals(1, rPanUserFileVOList.size());
        rPanUserFileVOList = iUserFileService.createFolder(rPanUserVO.getRootFileId(), "test-folder-2", userId);
        Assert.assertEquals(2, rPanUserFileVOList.size());
        RPanUserFileVO rPanUserFileVO = rPanUserFileVOList.stream().filter(rPanUserFileVO1 -> Objects.equals("test-folder-1", rPanUserFileVO1.getFilename())).findFirst().get();
        rPanUserFileVOList = iUserFileService.updateFilename(rPanUserFileVO.getFileId(), "test-folder-2", userId);
        Assert.assertEquals(2, rPanUserFileVOList.size());
    }

    /**
     * 测试删除文件成功
     */
    @Test
    @Rollback
    public void deleteSuccessTest() {
        Long userId = register();
        RPanUserVO rPanUserVO = info(userId);
        List<RPanUserFileVO> rPanUserFileVOList = iUserFileService.createFolder(rPanUserVO.getRootFileId(), "test-folder-1", userId);
        Assert.assertEquals(1, rPanUserFileVOList.size());
        RPanUserFileVO rPanUserFileVO = rPanUserFileVOList.stream().findFirst().get();
        rPanUserFileVOList = iUserFileService.delete(rPanUserFileVO.getParentId(), StringListUtil.longListToString(rPanUserFileVO.getFileId()), userId);
        Assert.assertEquals(0, rPanUserFileVOList.size());
    }

    /**
     * 测试上传文件成功
     */
    @Test
    @Rollback
    public void uploadSuccessTest() {
        Long userId = register();
        RPanUserVO rPanUserVO = info(userId);
        List<RPanUserFileVO> rPanUserFileVOList = iUserFileService.upload(generateMultipartFile(), rPanUserVO.getRootFileId(), userId);
        Assert.assertEquals(1, rPanUserFileVOList.size());
    }

    /**
     * 测试获取文件夹树成功
     */
    @Test
    @Rollback
    public void getFolderTreeSuccessTest() {
        Long userId = register();
        List<FolderTreeNode> folderTreeNodeList = iUserFileService.getFolderTree(userId);
        Assert.assertEquals(1, folderTreeNodeList.size());
    }

    /**
     * 测试转移文件(批量)成功
     */
    @Test
    @Rollback
    public void transferSuccessTest() {
        Long userId = register();
        RPanUserVO rPanUserVO = info(userId);
        List<RPanUserFileVO> rPanUserFileVOList = iUserFileService.createFolder(rPanUserVO.getRootFileId(), "test-folder-1", userId);
        Assert.assertEquals(1, rPanUserFileVOList.size());
        rPanUserFileVOList = iUserFileService.createFolder(rPanUserVO.getRootFileId(), "test-folder-2", userId);
        Assert.assertEquals(2, rPanUserFileVOList.size());
        RPanUserFileVO targetFolder = rPanUserFileVOList.stream().filter(rPanUserFileVO1 -> Objects.equals("test-folder-1", rPanUserFileVO1.getFilename())).findFirst().get();
        RPanUserFileVO toBeTransferFile = rPanUserFileVOList.stream().filter(rPanUserFileVO1 -> Objects.equals("test-folder-2", rPanUserFileVO1.getFilename())).findFirst().get();
        rPanUserFileVOList = iUserFileService.transfer(StringListUtil.longListToString(toBeTransferFile.getFileId()), toBeTransferFile.getParentId(), targetFolder.getFileId(), userId);
        Assert.assertEquals(1, rPanUserFileVOList.size());
    }

    /**
     * 测试转移文件(批量)目标文件不是文件夹失败
     */
    @Test(expected = RPanException.class)
    @Rollback
    public void transferTargetFileIsNotFolderFailTest() {
        Long userId = register();
        RPanUserVO rPanUserVO = info(userId);
        List<RPanUserFileVO> rPanUserFileVOList = iUserFileService.createFolder(rPanUserVO.getRootFileId(), "test-folder-1", userId);
        Assert.assertEquals(1, rPanUserFileVOList.size());
        rPanUserFileVOList = iUserFileService.upload(generateMultipartFile(), rPanUserVO.getRootFileId(), userId);
        Assert.assertEquals(2, rPanUserFileVOList.size());
        RPanUserFileVO toBeTransferFile = rPanUserFileVOList.stream().filter(rPanUserFileVO1 -> Objects.equals("test-folder-1", rPanUserFileVO1.getFilename())).findFirst().get();
        RPanUserFileVO targetFolderFile = rPanUserFileVOList.stream().filter(rPanUserFileVO1 -> Objects.equals("test.txt", rPanUserFileVO1.getFilename())).findFirst().get();
        iUserFileService.transfer(StringListUtil.longListToString(toBeTransferFile.getFileId()), toBeTransferFile.getParentId(), targetFolderFile.getFileId(), userId);
    }

    /**
     * 测试批量复制文件成功
     */
    @Test
    @Rollback
    public void copySuccessTest() {
        Long userId = register();
        RPanUserVO rPanUserVO = info(userId);
        List<RPanUserFileVO> rPanUserFileVOList = iUserFileService.createFolder(rPanUserVO.getRootFileId(), "test-folder-1", userId);
        Assert.assertEquals(1, rPanUserFileVOList.size());
        RPanUserFileVO toBeCopiedFile = rPanUserFileVOList.stream().filter(rPanUserFileVO1 -> Objects.equals("test-folder-1", rPanUserFileVO1.getFilename())).findFirst().get();
        rPanUserFileVOList = iUserFileService.copy(StringListUtil.longListToString(toBeCopiedFile.getFileId()), toBeCopiedFile.getParentId(), toBeCopiedFile.getParentId(), userId);
        Assert.assertEquals(2, rPanUserFileVOList.size());
    }

    /**
     * 测试批量复制文件-要复制的文件中包含选中的目标文件夹失败
     */
    @Test(expected = RPanException.class)
    @Rollback
    public void copyFileIdsContainTargetParentIdFailTest() {
        Long userId = register();
        RPanUserVO rPanUserVO = info(userId);
        List<RPanUserFileVO> rPanUserFileVOList = iUserFileService.createFolder(rPanUserVO.getRootFileId(), "test-folder-1", userId);
        Assert.assertEquals(1, rPanUserFileVOList.size());
        RPanUserFileVO toBeCopiedFile = rPanUserFileVOList.stream().filter(rPanUserFileVO1 -> Objects.equals("test-folder-1", rPanUserFileVO1.getFilename())).findFirst().get();
        iUserFileService.copy(StringListUtil.longListToString(toBeCopiedFile.getFileId()), toBeCopiedFile.getParentId(), toBeCopiedFile.getFileId(), userId);
    }

    /**
     * 测试批量复制文件-目标文件不是文件夹失败
     */
    @Test(expected = RPanException.class)
    @Rollback
    public void copyTargetFileIsNotFolderFailTest() {
        Long userId = register();
        RPanUserVO rPanUserVO = info(userId);
        List<RPanUserFileVO> rPanUserFileVOList = iUserFileService.createFolder(rPanUserVO.getRootFileId(), "test-folder-1", userId);
        Assert.assertEquals(1, rPanUserFileVOList.size());
        rPanUserFileVOList = iUserFileService.upload(generateMultipartFile(), rPanUserVO.getRootFileId(), userId);
        Assert.assertEquals(2, rPanUserFileVOList.size());
        RPanUserFileVO toBeCopiedFile = rPanUserFileVOList.stream().filter(rPanUserFileVO1 -> Objects.equals("test-folder-1", rPanUserFileVO1.getFilename())).findFirst().get();
        RPanUserFileVO targetFolderFile = rPanUserFileVOList.stream().filter(rPanUserFileVO1 -> Objects.equals("test.txt", rPanUserFileVO1.getFilename())).findFirst().get();
        iUserFileService.copy(StringListUtil.longListToString(toBeCopiedFile.getFileId()), toBeCopiedFile.getParentId(), targetFolderFile.getFileId(), userId);
    }

    /**
     * 测试通过文件名搜索文件成功
     */
    @Test
    @Rollback
    public void searchSuccessTest() {
        Long userId = register();
        String searchContent = "test-folder-1";
        RPanUserVO rPanUserVO = info(userId);
        List<RPanUserFileVO> rPanUserFileVOList = iUserFileService.createFolder(rPanUserVO.getRootFileId(), searchContent, userId);
        Assert.assertEquals(1, rPanUserFileVOList.size());
        rPanUserFileVOList = iUserFileService.search(searchContent, FileConstant.ALL_FILE_TYPE, userId);
        Assert.assertEquals(1, rPanUserFileVOList.size());
    }

    /**
     * 测试查询文件详情成功
     */
    @Test
    @Rollback
    public void detailSuccessTest() {
        Long userId = register();
        RPanUserVO rPanUserVO = info(userId);
        List<RPanUserFileVO> rPanUserFileVOList = iUserFileService.createFolder(rPanUserVO.getRootFileId(), "test-folder-1", userId);
        Assert.assertEquals(1, rPanUserFileVOList.size());
        RPanUserFileVO rPanUserFileVO = rPanUserFileVOList.stream().filter(rPanUserFileVO1 -> Objects.equals("test-folder-1", rPanUserFileVO1.getFilename())).findFirst().get();
        Assert.assertEquals(rPanUserFileVO, iUserFileService.detail(rPanUserFileVO.getFileId(), userId));
    }

    /**
     * 测试获取面包屑列表成功
     */
    @Test
    @Rollback
    public void getBreadcrumbsSuccessTest() {
        Long userId = register();
        RPanUserVO rPanUserVO = info(userId);
        List<RPanUserFileVO> rPanUserFileVOList = iUserFileService.createFolder(rPanUserVO.getRootFileId(), "test-folder-1", userId);
        Assert.assertEquals(1, rPanUserFileVOList.size());
        RPanUserFileVO rPanUserFileVO = rPanUserFileVOList.stream().filter(rPanUserFileVO1 -> Objects.equals("test-folder-1", rPanUserFileVO1.getFilename())).findFirst().get();
        List<BreadcrumbVO> breadcrumbVOList = iUserFileService.getBreadcrumbs(rPanUserFileVO.getFileId(), userId);
        Assert.assertEquals(2, breadcrumbVOList.size());
    }

    /**
     * 测试批量还原用户文件的删除状态成功
     */
    @Test
    @Rollback
    public void restoreUserFilesSuccessTest() {
        Long userId = register();
        RPanUserVO rPanUserVO = info(userId);
        List<RPanUserFileVO> rPanUserFileVOList = iUserFileService.createFolder(rPanUserVO.getRootFileId(), "test-folder-1", userId);
        Assert.assertEquals(1, rPanUserFileVOList.size());
        RPanUserFileVO rPanUserFileVO = rPanUserFileVOList.stream().findFirst().get();
        rPanUserFileVOList = iUserFileService.delete(rPanUserFileVO.getParentId(), StringListUtil.longListToString(rPanUserFileVO.getFileId()), userId);
        Assert.assertEquals(0, rPanUserFileVOList.size());
        iUserFileService.restoreUserFiles(StringListUtil.longListToString(rPanUserFileVO.getFileId()), userId);
        rPanUserFileVOList = iUserFileService.list(rPanUserFileVO.getParentId(), FileConstant.ALL_FILE_TYPE, userId);
        Assert.assertEquals(1, rPanUserFileVOList.size());
    }

    /**
     * 测试批量还原用户文件的删除状态-参数错误失败
     */
    @Test(expected = RPanException.class)
    @Rollback
    public void restoreErrorParamFailTest() {
        iUserFileService.restoreUserFiles(null, null);
    }

    /**
     * 测试物理删除用户文件成功
     */
    @Test
    @Rollback
    public void physicalDeleteUserFilesSuccessTest() {
        Long userId = register();
        RPanUserVO rPanUserVO = info(userId);
        List<RPanUserFileVO> rPanUserFileVOList = iUserFileService.createFolder(rPanUserVO.getRootFileId(), "test-folder-1", userId);
        Assert.assertEquals(1, rPanUserFileVOList.size());
        rPanUserFileVOList = iUserFileService.upload(generateMultipartFile(), rPanUserVO.getRootFileId(), userId);
        Assert.assertEquals(2, rPanUserFileVOList.size());
        List<Long> fileIds = rPanUserFileVOList.stream().map(RPanUserFileVO::getFileId).collect(Collectors.toList());
        iUserFileService.physicalDeleteUserFiles(StringListUtil.longListToString(fileIds), userId);
        rPanUserFileVOList = iUserFileService.list(rPanUserVO.getRootFileId(), FileConstant.ALL_FILE_TYPE, userId);
        Assert.assertEquals(0, rPanUserFileVOList.size());
    }

    /**
     * 测试批量保存文件成功
     */
    @Test
    @Rollback
    public void saveBatchSuccessTest() {
        Long userId = register();
        RPanUserVO rPanUserVO = info(userId);
        List<RPanUserFileVO> rPanUserFileVOList = iUserFileService.createFolder(rPanUserVO.getRootFileId(), "test-folder-1", userId);
        Assert.assertEquals(1, rPanUserFileVOList.size());
        RPanUserFileVO toBeCopiedFile = rPanUserFileVOList.stream().filter(rPanUserFileVO1 -> Objects.equals("test-folder-1", rPanUserFileVO1.getFilename())).findFirst().get();
        iUserFileService.saveBatch(StringListUtil.longListToString(toBeCopiedFile.getFileId()), toBeCopiedFile.getParentId(), userId);
        rPanUserFileVOList = iUserFileService.list(toBeCopiedFile.getParentId(), FileConstant.ALL_FILE_TYPE, userId);
        Assert.assertEquals(2, rPanUserFileVOList.size());
    }

    /**
     * 测试获取对应文件列表的所有文件以及自文件信息成功
     */
    @Test
    @Rollback
    public void allListSuccessTest() {
        Long userId = register();
        RPanUserVO rPanUserVO = info(userId);
        List<RPanUserFileVO> rPanUserFileVOList = iUserFileService.createFolder(rPanUserVO.getRootFileId(), "test-folder-1", userId);
        Assert.assertEquals(1, rPanUserFileVOList.size());
        RPanUserFileVO rPanUserFileVO = rPanUserFileVOList.stream().findFirst().get();
        rPanUserFileVOList = iUserFileService.upload(generateMultipartFile(), rPanUserFileVO.getFileId(), userId);
        Assert.assertEquals(1, rPanUserFileVOList.size());
        rPanUserFileVOList = iUserFileService.allList(StringListUtil.longListToString(rPanUserFileVO.getFileId()));
        Assert.assertEquals(2, rPanUserFileVOList.size());
    }

    /**
     * 测试获取对应文件列表的所有文件以及自文件信息成功
     */
    @Test
    @Rollback
    public void getUserTopFileInfoSuccessTest() {
        Long userId = register();
        RPanUserFile userTopFileInfo = iUserFileService.getUserTopFileInfo(userId);
        Assert.assertNotNull(userTopFileInfo);
    }

    /**
     * 测试获取对应文件列表的所有文件以及自文件信息成功
     */
    @Test
    @Rollback
    public void getAllAvailableFileIdByFileIdsSuccessTest() {
        Long userId = register();
        RPanUserVO rPanUserVO = info(userId);
        List<RPanUserFileVO> rPanUserFileVOList = iUserFileService.createFolder(rPanUserVO.getRootFileId(), "test-folder-1", userId);
        Assert.assertEquals(1, rPanUserFileVOList.size());
        RPanUserFileVO rPanUserFileVO = rPanUserFileVOList.stream().findFirst().get();
        rPanUserFileVOList = iUserFileService.upload(generateMultipartFile(), rPanUserFileVO.getFileId(), userId);
        Assert.assertEquals(1, rPanUserFileVOList.size());
        String allAvailableFileIdByFileIds = iUserFileService.getAllAvailableFileIdByFileIds(StringListUtil.longListToString(rPanUserFileVO.getFileId()));
        Assert.assertEquals(2, StringListUtil.string2LongList(allAvailableFileIdByFileIds).size());
    }

    /**
     * 测试获取对应文件列表的所有文件以及自文件信息成功
     */
    @Test
    @Rollback
    public void checkAllUpFileAvailableSuccessTest() {
        Long userId = register();
        RPanUserVO rPanUserVO = info(userId);
        List<RPanUserFileVO> rPanUserFileVOList = iUserFileService.createFolder(rPanUserVO.getRootFileId(), "test-folder-1", userId);
        Assert.assertEquals(1, rPanUserFileVOList.size());
        RPanUserFileVO rPanUserFileVO = rPanUserFileVOList.stream().findFirst().get();
        rPanUserFileVOList = iUserFileService.upload(generateMultipartFile(), rPanUserFileVO.getFileId(), userId);
        Assert.assertEquals(1, rPanUserFileVOList.size());
        boolean available = iUserFileService.checkAllUpFileAvailable(Arrays.asList(rPanUserFileVO.getFileId()));
        Assert.assertTrue(available);
        iUserFileService.delete(rPanUserVO.getRootFileId(), rPanUserFileVO.getFileId().toString(), userId);
        available = iUserFileService.checkAllUpFileAvailable(Arrays.asList(rPanUserFileVO.getFileId()));
        Assert.assertFalse(available);
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
