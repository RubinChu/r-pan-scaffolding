package com.rubin.rpan.service;

import com.rubin.rpan.common.response.ServerResponse;
import com.rubin.rpan.vo.BreadcrumbVO;
import com.rubin.rpan.vo.DirectoryTreeNode;
import com.rubin.rpan.vo.RPanFileVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Description
 * @auther chuqian
 * @create 2019-09-20 9:47
 */
public interface IFileService {

    ServerResponse<List<RPanFileVO>> list(String parentId, String fileTypes, Integer userId);

    ServerResponse<List<RPanFileVO>> createDirectory(String parentId, String directoryName, Integer userId);

    ServerResponse<List<RPanFileVO>> updateFileName(String fileId, String newFileName, Integer userId);

    ServerResponse<List<RPanFileVO>> delete(String parentId, String fileIds, Integer userId);

    ServerResponse<List<RPanFileVO>> upload(MultipartFile file, String parentId, Integer userId);

    void download(String fileId, Integer userId, HttpServletResponse response);

    ServerResponse<List<DirectoryTreeNode>> getDirectoryTree(Integer userId);

    ServerResponse<List<RPanFileVO>> transfer(String fileIds, String parentId, String targetParentId, Integer userId);

    ServerResponse<List<RPanFileVO>> copy(String fileIds, String parentId, String targetParentId, Integer userId);

    ServerResponse<List<RPanFileVO>> search(String keyword, Integer userId);

    ServerResponse<RPanFileVO> detail(String fileId, Integer userId);

    void downloadZip(String fileIds, HttpServletResponse response, Integer userId);

    ServerResponse<List<BreadcrumbVO>> getBreadcrumbs(String fileId, Integer userId);
}
