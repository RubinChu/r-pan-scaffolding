package com.rubin.rpan.modules.file.service;

import com.rubin.rpan.modules.file.entity.RPanUserFile;
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

    List<RPanUserFileVO> list(Long parentId, String fileTypes, Long userId);

    List<RPanUserFileVO> list(Long parentId, String fileTypes, Long userId, Integer delFlag);

    List<RPanUserFileVO> list(String fileIds);

    List<RPanUserFileVO> createFolder(Long parentId, String folderName, Long userId);

    List<RPanUserFileVO> updateFilename(Long fileId, String newFilename, Long userId);

    List<RPanUserFileVO> delete(Long parentId, String fileIds, Long userId);

    List<RPanUserFileVO> upload(MultipartFile file, Long parentId, Long userId);

    void download(Long fileId, HttpServletResponse response, Long userId);

    void download(Long fileId, HttpServletResponse response);

    List<FolderTreeNode> getFolderTree(Long userId);

    List<RPanUserFileVO> transfer(String fileIds, Long parentId, Long targetParentId, Long userId);

    List<RPanUserFileVO> copy(String fileIds, Long parentId, Long targetParentId, Long userId);

    List<RPanUserFileVO> search(String keyword, String fileTypes, Long userId);

    RPanUserFileVO detail(Long fileId, Long userId);

    List<BreadcrumbVO> getBreadcrumbs(Long fileId, Long userId);

    void preview(Long fileId, HttpServletResponse response, Long userId);

    void restoreUserFiles(String fileIds, Long userId);

    void physicalDeleteUserFiles(String fileIds, Long userId);

    void saveBatch(String fileIds, Long targetParentId, Long userId);

    List<RPanUserFileVO> allList(String fileIds);

    RPanUserFile getUserTopFileInfo(Long userId);

    String getAllAvailableFileIdByFileIds(String fileIds);

    boolean checkAllUpFileAvailable(List<Long> fileIds);

}
