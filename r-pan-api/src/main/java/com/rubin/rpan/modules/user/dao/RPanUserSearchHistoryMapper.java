package com.rubin.rpan.modules.user.dao;

import com.rubin.rpan.modules.user.entity.RPanUserSearchHistory;
import com.rubin.rpan.modules.user.vo.RPanUserSearchHistoryVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户搜索历史数据层
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@Repository(value = "rPanUserSearchHistoryMapper")
public interface RPanUserSearchHistoryMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(RPanUserSearchHistory record);

    int insertSelective(RPanUserSearchHistory record);

    RPanUserSearchHistory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RPanUserSearchHistory record);

    int updateByPrimaryKey(RPanUserSearchHistory record);

    List<RPanUserSearchHistoryVO> selectRPanUserSearchHistoryVOListByUserId(@Param("userId") String userId);

    RPanUserSearchHistory selectBySearchContentAndUserId(@Param("searchContent") String searchContent, @Param("userId") String userId);

    int updateUpdateTimeBySearchContentAndUserId(@Param("searchContent") String searchContent, @Param("userId") String userId);

}