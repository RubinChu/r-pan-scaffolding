package com.rubin.rpan.modules.file.controller;

import com.rubin.rpan.common.annotation.LogIgnore;
import com.rubin.rpan.common.annotation.NeedLogin;
import com.rubin.rpan.common.response.R;
import com.rubin.rpan.common.util.UserIdUtil;
import com.rubin.rpan.modules.file.po.*;
import com.rubin.rpan.modules.file.service.IUserFileService;
import com.rubin.rpan.modules.file.vo.BreadcrumbVO;
import com.rubin.rpan.modules.file.vo.FolderTreeNode;
import com.rubin.rpan.modules.file.vo.RPanUserFileVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 项目文件相关rest接口返回
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@RestController
@Validated
@Api(tags = "文件接口")
public class FileRestController {

    @Autowired
    @Qualifier(value = "userFileService")
    private IUserFileService iUserFileService;

    /**
     * 获取文件列表
     *
     * @param parentId
     * @param fileTypes
     * @return
     */
    @ApiOperation(
            value = "获取文件列表",
            notes = "该接口提供了获取文件列表的功能",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @GetMapping("files")
    @NeedLogin
    public R<List<RPanUserFileVO>> list(@NotNull(message = "父id不能为空") @RequestParam(value = "parentId", required = false) Long parentId,
                                        @RequestParam(name = "fileTypes", required = false, defaultValue = "-1") String fileTypes) {
        return R.data(iUserFileService.list(parentId, fileTypes, UserIdUtil.get()));
    }

    /**
     * 新建文件夹
     *
     * @param createFolderPO
     * @return
     */
    @ApiOperation(
            value = "新建文件夹",
            notes = "该接口提供了新建文件夹的功能",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @PostMapping("file/folder")
    @NeedLogin
    public R<List<RPanUserFileVO>> createFolder(@Validated @RequestBody CreateFolderPO createFolderPO) {
        return R.data(iUserFileService.createFolder(createFolderPO.getParentId(), createFolderPO.getFolderName(), UserIdUtil.get()));
    }

    /**
     * 文件重命名
     *
     * @param updateFileNamePO
     * @return
     */
    @ApiOperation(
            value = "文件重命名",
            notes = "该接口提供了文件重命名的功能",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @PutMapping("file")
    @NeedLogin
    public R<List<RPanUserFileVO>> updateFilename(@Validated @RequestBody UpdateFileNamePO updateFileNamePO) {
        return R.data(iUserFileService.updateFilename(updateFileNamePO.getFileId(), updateFileNamePO.getNewFilename(), UserIdUtil.get()));
    }

    /**
     * 删除文件(批量)
     *
     * @param deletePO
     * @return
     */
    @ApiOperation(
            value = "删除文件(批量)",
            notes = "该接口提供了删除文件(批量)的功能",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @DeleteMapping("file")
    @NeedLogin
    public R<List<RPanUserFileVO>> delete(@Validated @RequestBody DeletePO deletePO) {
        return R.data(iUserFileService.delete(deletePO.getParentId(), deletePO.getFileIds(), UserIdUtil.get()));
    }

    /**
     * 上传文件
     *
     * @param file
     * @param parentId
     * @return
     */
    @ApiOperation(
            value = "上传文件",
            notes = "该接口提供了上传文件的功能",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @PostMapping("file/upload")
    @NeedLogin
    @LogIgnore
    public R<List<RPanUserFileVO>> upload(@NotNull(message = "上传文件不能为空") @RequestParam(value = "file", required = false) MultipartFile file,
                                          @NotNull(message = "父id不能为空") @RequestParam(value = "parentId", required = false) Long parentId) {
        return R.data(iUserFileService.upload(file, parentId, UserIdUtil.get()));
    }

    /**
     * 下载文件(只支持单个下载)
     *
     * @param fileId
     * @param response
     */
    @ApiOperation(
            value = "下载文件(只支持单个下载)",
            notes = "该接口提供了下载文件(只支持单个下载)的功能",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @GetMapping("file/download")
    @NeedLogin
    @LogIgnore
    public void download(@NotNull(message = "请选择要下载的文件") @RequestParam(value = "fileId", required = false) Long fileId,
                         HttpServletResponse response) {
        iUserFileService.download(fileId, response, UserIdUtil.get());
    }

    /**
     * 获取文件夹树
     *
     * @return
     */
    @ApiOperation(
            value = "获取文件夹树",
            notes = "该接口提供了获取文件夹树的功能",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @GetMapping("file/folder/tree")
    @NeedLogin
    public R<List<FolderTreeNode>> getFolderTree() {
        return R.data(iUserFileService.getFolderTree(UserIdUtil.get()));
    }

    /**
     * 转移文件(批量)
     *
     * @param transferPO
     * @return
     */
    @ApiOperation(
            value = "转移文件(批量)",
            notes = "该接口提供了转移文件(批量)的功能",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @PostMapping("file/transfer")
    @NeedLogin
    public R<List<RPanUserFileVO>> transfer(@Validated @RequestBody TransferPO transferPO) {
        return R.data(iUserFileService.transfer(transferPO.getFileIds(), transferPO.getParentId(), transferPO.getTargetParentId(), UserIdUtil.get()));
    }

    /**
     * 复制文件(批量)
     *
     * @param copyPO
     * @return
     */
    @ApiOperation(
            value = "复制文件(批量)",
            notes = "该接口提供了复制文件(批量)的功能",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @PostMapping("file/copy")
    @NeedLogin
    public R<List<RPanUserFileVO>> copy(@Validated @RequestBody CopyPO copyPO) {
        return R.data(iUserFileService.copy(copyPO.getFileIds(), copyPO.getParentId(), copyPO.getTargetParentId(), UserIdUtil.get()));
    }

    /**
     * 通过文件名搜索文件列表
     *
     * @param keyword
     * @param fileTypes
     * @return
     */
    @ApiOperation(
            value = "通过文件名搜索文件列表",
            notes = "该接口提供了通过文件名搜索文件列表的功能",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @GetMapping("file/search")
    @NeedLogin
    public R<List<RPanUserFileVO>> search(@NotBlank(message = "关键字不能为空") @RequestParam(value = "keyword", required = false) String keyword,
                                          @RequestParam(name = "fileTypes", required = false, defaultValue = "-1") String fileTypes) {
        return R.data(iUserFileService.search(keyword, fileTypes, UserIdUtil.get()));
    }

    /**
     * 查询文件详情
     *
     * @param fileId
     * @return
     */
    @ApiOperation(
            value = "查询文件详情",
            notes = "该接口提供了查询文件详情的功能",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @GetMapping("file")
    @NeedLogin
    public R<RPanUserFileVO> detail(@NotNull(message = "文件id不能为空") @RequestParam(value = "fileId", required = false) Long fileId) {
        return R.data(iUserFileService.detail(fileId, UserIdUtil.get()));
    }

    /**
     * 获取面包屑列表
     *
     * @return
     */
    @ApiOperation(
            value = "获取面包屑列表",
            notes = "该接口提供了获取面包屑列表的功能",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @GetMapping("file/breadcrumbs")
    @NeedLogin
    public R<List<BreadcrumbVO>> getBreadcrumbs(@NotNull(message = "文件id不能为空") @RequestParam(value = "fileId", required = false) Long fileId) {
        return R.data(iUserFileService.getBreadcrumbs(fileId, UserIdUtil.get()));
    }

    /**
     * 预览单个文件
     *
     * @param fileId
     * @return
     */
    @ApiOperation(
            value = "预览单个文件",
            notes = "该接口提供了预览单个文件的功能",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @GetMapping("preview")
    @NeedLogin
    @LogIgnore
    public void preview(@NotNull(message = "文件id不能为空") @RequestParam(value = "fileId", required = false) Long fileId,
                        HttpServletResponse response) {
        iUserFileService.preview(fileId, response, UserIdUtil.get());
    }

}
