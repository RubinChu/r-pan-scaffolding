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

    int deleteByPrimaryKey(Long id);

    int insert(RPanShareFile record);

    int insertSelective(RPanShareFile record);

    RPanShareFile selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RPanShareFile record);

    int updateByPrimaryKey(RPanShareFile record);

    int insertBatch(@Param("rPanShareFileList") List<RPanShareFile> rPanShareFileList);

    int deleteByShareIdList(@Param("shareIdList") List<Long> shareIdList);

    List<Long> selectFileIdsByShareId(@Param("shareId") Long shareId);

    List<Long> selectShareIdByFileIds(@Param("fileIds") List<Long> fileIds);

}