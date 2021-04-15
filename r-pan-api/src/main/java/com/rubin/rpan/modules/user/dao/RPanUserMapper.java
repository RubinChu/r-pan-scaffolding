package com.rubin.rpan.modules.user.dao;

import com.rubin.rpan.modules.share.vo.ShareUserInfoVO;
import com.rubin.rpan.modules.user.entity.RPanUser;
import com.rubin.rpan.modules.user.vo.RPanUserVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 用户信息数据层
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@Repository(value = "rPanUserMapper")
public interface RPanUserMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(RPanUser record);

    int insertSelective(RPanUser record);

    RPanUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RPanUser record);

    int updateByPrimaryKey(RPanUser record);

    RPanUser selectByUserName(@Param("username") String username);

    RPanUserVO selectRPanUserVOByUserId(@Param("userId") String userId);

    int selectCountByUsernameAndQuestionAndAnswer(@Param("username") String username, @Param("question") String question, @Param("answer") String answer);

    RPanUser selectByUserId(@Param("userId") String userId);

    ShareUserInfoVO selectShareUserInfoVOByUserId(@Param("userId") String userId);

}