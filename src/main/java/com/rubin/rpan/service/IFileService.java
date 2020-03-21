package com.rubin.rpan.service;

import com.rubin.rpan.common.response.ServerResponse;
import com.rubin.rpan.entity.FileInfo;
import com.rubin.rpan.entity.FolderTreeNode;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 文件管理service接口
 *
 * @Description
 * @auther chuqian
 * @create 2019-09-20 9:47
 */
public interface IFileService {

    ServerResponse<List<FileInfo>> getFiles(String filePath, Integer fileType, String fileName, HttpSession session);

    ServerResponse<List<FileInfo>> createDirectory(String filePath, String folderName, HttpSession session);

    ServerResponse<List<FileInfo>> updateFileName(String filePath, String oldName, String newName, HttpSession session);

    ServerResponse<List<FileInfo>> deleteFile(String filePath, String fileNames, HttpSession session);

    ServerResponse<List<FileInfo>> uploadFile(MultipartFile file, String filePath, HttpSession session);

    void downloadFile(String filePath, String fileNames, HttpServletResponse response, HttpSession session);

    ServerResponse<List<FolderTreeNode>> getFolders(HttpSession session);

    ServerResponse<List<FileInfo>> transferFile(String filePath, String fileNames, String targetFolder, HttpSession session);

}
