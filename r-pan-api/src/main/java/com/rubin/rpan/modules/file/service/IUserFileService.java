package com.rubin.rpan.modules.file.service;

import com.rubin.rpan.modules.file.vo.BreadcrumbVO;
import com.rubin.rpan.modules.file.vo.FolderTreeNode;
import com.rubin.rpan.modules.file.vo.RPanUserFileVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用户文件业务处理接口
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
public interface IUserFileService {

    List<RPanUserFileVO> list(String parentId, String fileTypes, String userId);

    List<RPanUserFileVO> list(String parentId, String fileTypes, String userId, Integer delFlag);

    List<RPanUserFileVO> list(String fileIds);

    List<RPanUserFileVO> createFolder(String parentId, String folderName, String userId);

    List<RPanUserFileVO> updateFilename(String fileId, String newFilename, String userId);

    List<RPanUserFileVO> delete(String parentId, String fileIds, String userId);

    List<RPanUserFileVO> upload(MultipartFile file, String parentId, String userId);

    void download(String fileId, HttpServletResponse response, String userId);

    void download(String fileId, HttpServletResponse response);

    List<FolderTreeNode> getFolderTree(String userId);

    List<RPanUserFileVO> transfer(String fileIds, String parentId, String targetParentId, String userId);

    List<RPanUserFileVO> copy(String fileIds, String parentId, String targetParentId, String userId);

    List<RPanUserFileVO> search(String keyword, String fileTypes, String userId);

    RPanUserFileVO detail(String fileId, String userId);

    List<BreadcrumbVO> getBreadcrumbs(String fileId, String userId);

    void preview(String fileId, HttpServletResponse response, String userId);

    void restoreUserFiles(String fileIds, String userId);

    void physicalDeleteUserFiles(String fileIds, String userId);

    void saveBatch(String fileIds, String targetParentId, String userId);

    List<RPanUserFileVO> allList(String fileIds);

}
