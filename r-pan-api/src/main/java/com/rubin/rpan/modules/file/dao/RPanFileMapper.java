package com.rubin.rpan.modules.file.dao;

import com.rubin.rpan.modules.file.entity.RPanFile;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 物理文件数据层
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@Repository(value = "rPanFileMapper")
public interface RPanFileMapper {

    int deleteByPrimaryKey(Long fileId);

    int insert(RPanFile record);

    int insertSelective(RPanFile record);

    RPanFile selectByPrimaryKey(Long fileId);

    int updateByPrimaryKeySelective(RPanFile record);

    int updateByPrimaryKey(RPanFile record);

    List<RPanFile> selectByFileIdList(@Param("fileIdList") List<String> fileIdList);

    int deleteBatch(@Param("fileIdList") List<String> fileIdList);

}