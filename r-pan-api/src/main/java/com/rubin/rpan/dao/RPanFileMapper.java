package com.rubin.rpan.dao;

import com.rubin.rpan.entity.RPanFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * file mapper
 */
@Mapper
public interface RPanFileMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RPanFile record);

    int insertSelective(RPanFile record);

    RPanFile selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RPanFile record);

    int updateByPrimaryKey(RPanFile record);

    RPanFile selectByFileId(@Param("fileId") String fileId);

    List<RPanFile> selectByUserIdAndFileTypeAndParentId(@Param("userId") Integer userId,
                                                        @Param("fileTypes") List<String> fileTypes,
                                                        @Param("parentId") String parentId);

    int deleteBatch(@Param("rPanFiles") List<RPanFile> rPanFiles,
                    @Param("userId") Integer userId);

    List<RPanFile> selectByFileIds(@Param("fileIds") List<String> fileIds);

    List<RPanFile> selectByUserIdAndFileType(@Param("userId") Integer userId,
                                             @Param("fileType") Integer fileType);

    int updateParentIdByIds(@Param("idList") List<String> idList,
                            @Param("parentId") Integer parentId);

    List<RPanFile> selectByUserIdAndFileName(@Param("userId") Integer userId,
                                             @Param("keyword") String keyword);

    RPanFile selectByFileIdAndUserId(@Param("fileId") String fileId,
                                     @Param("userId") Integer userId);

    int selectCountByUserIdAndFileNameAndParentId(@Param("userId") Integer userId,
                                                  @Param("newFileName") String newFileName,
                                                  @Param("parentId") String parentId);

    int updateFileInfoBatch(@Param("rPanFiles") List<RPanFile> rPanFiles);

    int insertBatch(@Param("rPanFiles") List<RPanFile> rPanFiles);
}