package com.rubin.rpan.modules.file.dao;

import com.rubin.rpan.modules.file.bo.RPanFileDownloadBO;
import com.rubin.rpan.modules.file.bo.RPanFilePreviewBO;
import com.rubin.rpan.modules.file.entity.RPanUserFile;
import com.rubin.rpan.modules.file.vo.RPanUserFileVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户文件数据层
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@Repository(value = "rPanUserFileMapper")
public interface RPanUserFileMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(RPanUserFile record);

    int insertSelective(RPanUserFile record);

    RPanUserFile selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RPanUserFile record);

    int updateByPrimaryKey(RPanUserFile record);

    List<RPanUserFileVO> selectByUserIdAndFileTypeAndParentIdAndDelFlag(@Param("userId") String userId,
                                                                        @Param("fileTypeArray") List<String> fileTypeArray,
                                                                        @Param("parentId") String parentId,
                                                                        @Param("delFlag") Integer delFlag);

    RPanUserFile selectByFileId(@Param("fileId") String fileId);

    RPanUserFile selectByFileIdAndUserId(@Param("fileId")String fileId, @Param("userId") String userId);

    int selectCountByUserIdAndFilenameAndParentId(@Param("userId") String userId,
                                                  @Param("filename") String filename,
                                                  @Param("parentId") String parentId);

    int deleteBatch(@Param("idList") List<String> idList, @Param("userId") String userId);

    RPanFileDownloadBO selectRPanFileDownloadBOByFileId(@Param("fileId") String fileId);

    List<RPanUserFile> selectFolderListByUserId(@Param("userId") String userId);

    List<RPanUserFile> selectListByFileIdList(@Param("idList") List<String> idList);

    int insertBatch(@Param("recordList") List<RPanUserFile> recordList);

    List<RPanUserFileVO> selectRPanUserFileVOListByUserIdAndFilenameAndFileTypes(@Param("userId") String userId, @Param("keyword") String keyword, @Param("fileTypeArray") List<String> fileTypeArray);

    RPanUserFileVO selectRPanUserFileVOByFileIdAndUserId(@Param("fileId") String fileId, @Param("userId") String userId);

    RPanFilePreviewBO selectRPanFilePreviewBOByFileId(@Param("fileId") String fileId);

    List<RPanUserFile> selectAllListByParentId(@Param("parentId") String parentId);

    List<RPanUserFile> selectAvailableListByParentId(@Param("parentId") String parentId);

    List<RPanUserFileVO> selectAvailableRPanUserFileVOListByParentId(@Param("parentId") String parentId);

    int updateUserFileDelFlagByFileIdListAndUserId(@Param("fileIdList") List<String> fileIdList, @Param("userId") String userId);

    int physicalDeleteBatch(@Param("fileIdList") List<String> fileIdList, @Param("userId") String userId);

    int selectCountByRealFileId(@Param("realFileId") String realFileId);

    List<RPanUserFileVO> selectRPanUserFileVOListByFileIdList(@Param("fileIdList") List<String> fileIdList);

}