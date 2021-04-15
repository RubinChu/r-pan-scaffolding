package com.rubin.rpan.modules.share.dao;

import com.rubin.rpan.modules.share.entity.RPanShareFile;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 分享文件数据层
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@Repository(value = "rPanShareFileMapper")
public interface RPanShareFileMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(RPanShareFile record);

    int insertSelective(RPanShareFile record);

    RPanShareFile selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RPanShareFile record);

    int updateByPrimaryKey(RPanShareFile record);

    int insertBatch(@Param("rPanShareFileList") List<RPanShareFile> rPanShareFileList);

    int deleteByShareIdList(@Param("shareIdList") List<String> shareIdList);

    List<String> selectFileIdsByShareId(@Param("shareId") String shareId);

    List<String> selectShareIdByFileIds(@Param("fileIds") List<String> fileIds);

}