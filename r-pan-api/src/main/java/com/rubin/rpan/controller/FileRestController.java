package com.rubin.rpan.controller;

import com.rubin.rpan.common.response.ServerResponse;
import com.rubin.rpan.common.util.JwtUtil;
import com.rubin.rpan.common.util.RPanUtil;
import com.rubin.rpan.po.*;
import com.rubin.rpan.service.IFileService;
import com.rubin.rpan.vo.BreadcrumbVO;
import com.rubin.rpan.vo.DirectoryTreeNode;
import com.rubin.rpan.vo.RPanFileVO;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Project rest interface return
 * Created by rubin on 2019/9/7.
 */

@RestController
@RequestMapping("file")
@AllArgsConstructor
public class FileRestController {

    private IFileService iFileService;

    private RPanUtil rPanUtil;

    /**
     * get file list
     *
     * @param parentId
     * @param fileTypes
     * @param request
     * @return
     */
    @GetMapping("list")
    public ServerResponse<List<RPanFileVO>> list(@RequestParam(name = "parentId", required = false, defaultValue = "-1") String parentId,
                                                 @RequestParam(name = "fileTypes", required = false, defaultValue = "-1") String fileTypes,
                                                 HttpServletRequest request) {
        return iFileService.list(parentId, fileTypes, rPanUtil.getUserId(request));
    }

    /**
     * new folder
     *
     * @param createDirectoryPO
     * @param request
     * @return
     */
    @PostMapping("directory/create")
    public ServerResponse<List<RPanFileVO>> createDirectory(@RequestBody CreateDirectoryPO createDirectoryPO,
                                                            HttpServletRequest request) {
        return iFileService.createDirectory(createDirectoryPO.getParentId(), createDirectoryPO.getDirectoryName(), rPanUtil.getUserId(request));
    }

    /**
     * file rename
     *
     * @param updateFileNamePO
     * @param request
     * @return
     */
    @PutMapping("update")
    public ServerResponse<List<RPanFileVO>> updateFileName(@RequestBody UpdateFileNamePO updateFileNamePO,
                                                           HttpServletRequest request) {
        return iFileService.updateFileName(updateFileNamePO.getFileId(), updateFileNamePO.getNewFileName(), rPanUtil.getUserId(request));
    }

    /**
     * delete files (batch)
     *
     * @param deletePO
     * @param request
     * @return
     */
    @DeleteMapping("delete")
    public ServerResponse<List<RPanFileVO>> delete(@RequestBody DeletePO deletePO,
                                                   HttpServletRequest request) {
        return iFileService.delete(deletePO.getParentId(), deletePO.getFileIds(), rPanUtil.getUserId(request));
    }

    /**
     * upload file
     *
     * @param file
     * @param parentId
     * @param request
     * @return
     */
    @PostMapping("upload")
    public ServerResponse<List<RPanFileVO>> upload(MultipartFile file,
                                                   String parentId,
                                                   HttpServletRequest request) {
        return iFileService.upload(file, parentId, rPanUtil.getUserId(request));
    }

    /**
     * download file (only supports single download)
     *
     * @param fileId
     * @param token
     * @param response
     */
    @GetMapping("download")
    public void download(@RequestParam("fileId") String fileId,
                         @RequestParam("token") String token,
                         HttpServletResponse response) {
        iFileService.download(fileId, JwtUtil.analyzeToken(token), response);
    }

    /**
     * bulk package download
     *
     * @param fileIds
     * @param token
     * @param response
     */
    @GetMapping("zip/download")
    public void downloadZip(@RequestParam("fileIds") String fileIds,
                            @RequestParam("token") String token,
                            HttpServletResponse response) {
        iFileService.downloadZip(fileIds, response, JwtUtil.analyzeToken(token));
    }

    /**
     * Get folder tree
     *
     * @param request
     * @return
     */
    @GetMapping("directory/tree")
    public ServerResponse<List<DirectoryTreeNode>> getDirectoryTree(HttpServletRequest request) {
        return iFileService.getDirectoryTree(rPanUtil.getUserId(request));
    }

    /**
     * transfer files (batch)
     *
     * @param transferPO
     * @param request
     * @return
     */
    @PostMapping("transfer")
    public ServerResponse<List<RPanFileVO>> transfer(@RequestBody TransferPO transferPO,
                                                     HttpServletRequest request) {
        return iFileService.transfer(transferPO.getFileIds(), transferPO.getParentId(), transferPO.getTargetParentId(), rPanUtil.getUserId(request));
    }

    /**
     * copy files (batch)
     *
     * @param copyPO
     * @param request
     * @return
     */
    @PostMapping("copy")
    public ServerResponse<List<RPanFileVO>> copy(@RequestBody CopyPO copyPO,
                                                 HttpServletRequest request) {
        return iFileService.copy(copyPO.getFileIds(), copyPO.getParentId(), copyPO.getTargetParentId(), rPanUtil.getUserId(request));
    }

    /**
     * search file list by file name
     *
     * @param keyword
     * @param request
     * @return
     */
    @GetMapping("search")
    public ServerResponse<List<RPanFileVO>> search(@RequestParam("keyword") String keyword,
                                                   HttpServletRequest request) {
        return iFileService.search(keyword, rPanUtil.getUserId(request));
    }

    /**
     * check file details
     *
     * @param fileId
     * @param request
     * @return
     */
    @GetMapping("detail")
    public ServerResponse<RPanFileVO> detail(@RequestParam("fileId") String fileId,
                                             HttpServletRequest request) {
        return iFileService.detail(fileId, rPanUtil.getUserId(request));
    }

    /**
     * get a list of breadcrumbs
     *
     * @return
     */
    @GetMapping("breadcrumbs")
    public ServerResponse<List<BreadcrumbVO>> getBreadcrumbs(@RequestParam("fileId") String fileId,
                                                             HttpServletRequest request) {
        return iFileService.getBreadcrumbs(fileId, rPanUtil.getUserId(request));
    }

}
