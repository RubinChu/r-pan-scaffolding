package com.rubin.rpan.controller.rest;

import com.rubin.rpan.common.response.ServerResponse;
import com.rubin.rpan.entity.FileInfo;
import com.rubin.rpan.entity.FolderTreeNode;
import com.rubin.rpan.service.IFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by rubin on 2019/9/7.
 * 项目rest接口返回
 */

@RestController
@RequestMapping("/file/")
public class FileRestController {

    @Autowired
    private IFileService iFileService;

    /**
     * 获取文件列表
     *
     * @param filePath
     * @param fileType
     * @param fileName
     * @param session
     * @return
     */
    @GetMapping("/getFiles")
    public ServerResponse<List<FileInfo>> getFiles(@RequestParam(name = "filePath") String filePath,
                                                   @RequestParam(name = "fileType", required = false, defaultValue = "1") Integer fileType,
                                                   @RequestParam(name = "fileName", required = false, defaultValue = "") String fileName,
                                                   HttpSession session) {
        return iFileService.getFiles(filePath, fileType, fileName, session);
    }

    /**
     * 新建文件夹
     *
     * @param filePath
     * @param folderName
     * @param session
     * @return
     */
    @PostMapping("/createDirectory")
    public ServerResponse<List<FileInfo>> createDirectory(String filePath, String folderName, HttpSession session) {
        return iFileService.createDirectory(filePath, folderName, session);
    }

    /**
     * 文件重命名
     *
     * @param filePath
     * @param oldName
     * @param newName
     * @param session
     * @return
     */
    @PostMapping("/updateFileName")
    public ServerResponse<List<FileInfo>> updateFileName(String filePath, String oldName, String newName, HttpSession session) {
        return iFileService.updateFileName(filePath, oldName, newName, session);
    }

    /**
     * 删除文件(批量)
     *
     * @param filePath
     * @param fileNames
     * @param session
     * @return
     */
    @PostMapping("/deleteFile")
    public ServerResponse<List<FileInfo>> deleteFile(String filePath, String fileNames, HttpSession session) {
        return iFileService.deleteFile(filePath, fileNames, session);
    }

    /**
     * 上传文件
     *
     * @param file
     * @param filePath
     * @param session
     * @return
     */
    @PostMapping("/uploadFile")
    public ServerResponse<List<FileInfo>> uploadFile(@RequestParam("file") MultipartFile file,
                                                     @RequestParam("filePath") String filePath,
                                                     HttpSession session) {
        return iFileService.uploadFile(file, filePath, session);
    }

    /**
     * 下载文件(批量打包下载)
     *
     * @param filePath
     * @param fileNames
     * @param response
     * @param session
     */
    @PostMapping("/downloadFile")
    public void downloadFile(String filePath, String fileNames, HttpServletResponse response, HttpSession session) {
        iFileService.downloadFile(filePath, fileNames, response, session);
    }

    /**
     * 获取文件夹树
     *
     * @param session
     * @return
     */
    @GetMapping("/getFolders")
    public ServerResponse<List<FolderTreeNode>> getFolders(HttpSession session) {
        return iFileService.getFolders(session);
    }

    /**
     * 转移文件(批量)
     *
     * @param filePath
     * @param fileNames
     * @param targetFolder
     * @param session
     * @return
     */
    @PostMapping("/transferFile")
    public ServerResponse<List<FileInfo>> transferFile(String filePath, String fileNames, String targetFolder, HttpSession session) {
        return iFileService.transferFile(filePath, fileNames, targetFolder, session);
    }

}
