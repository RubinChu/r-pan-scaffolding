package com.rubin.rpan.dao;

import com.rubin.rpan.entity.RPanUser;
import com.rubin.rpan.po.CheckAnswerPO;
import com.rubin.rpan.vo.RPanUserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * user mapper
 */
@Mapper
public interface RPanUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RPanUser record);

    int insertSelective(RPanUser record);

    RPanUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RPanUser record);

    int updateByPrimaryKey(RPanUser record);

    RPanUser selectByUserName(@Param("username") String username);

    RPanUserVO selectUserInfoByPrimaryKey(@Param("userId") Integer userId);

    int selectCountByUsernameAndQuestionAndAnswer(CheckAnswerPO checkAnswerPO);
}