package com.rubin.rpan.modules.share.dao;

import com.rubin.rpan.modules.share.entity.RPanShare;
import com.rubin.rpan.modules.share.vo.RPanUserShareUrlVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * 分享数据层
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@Repository(value = "rPanShareMapper")
public interface RPanShareMapper {

    int deleteByPrimaryKey(Long shareId);

    int insert(RPanShare record);

    int insertSelective(RPanShare record);

    RPanShare selectByPrimaryKey(Long shareId);

    int updateByPrimaryKeySelective(RPanShare record);

    int updateByPrimaryKey(RPanShare record);

    List<RPanUserShareUrlVO> selectRPanUserShareUrlVOListByUserId(@Param("userId") Long userId);

    int deleteByShareIdListAndUserId(@Param("shareIdList") List<Long> shareIdList, @Param("userId") Long userId);

    RPanShare selectByShareId(@Param("shareId") String shareId);

    int changeShareStatusByShareId(@Param("shareId") Long shareId, @Param("shareStatus") Integer shareStatus);

}