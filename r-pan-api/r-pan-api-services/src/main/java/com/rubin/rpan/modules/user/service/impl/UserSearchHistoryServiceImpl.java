package com.rubin.rpan.modules.user.service.impl;

import com.rubin.rpan.common.constant.CommonConstant;
import com.rubin.rpan.common.exception.RPanException;
import com.rubin.rpan.modules.user.dao.RPanUserSearchHistoryMapper;
import com.rubin.rpan.modules.user.entity.RPanUserSearchHistory;
import com.rubin.rpan.modules.user.service.IUserSearchHistoryService;
import com.rubin.rpan.modules.user.vo.RPanUserSearchHistoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 用户搜索历史业务处理实现类
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@Service(value = "userSearchHistoryService")
public class UserSearchHistoryServiceImpl implements IUserSearchHistoryService {

    @Autowired
    @Qualifier(value = "rPanUserSearchHistoryMapper")
    private RPanUserSearchHistoryMapper rPanUserSearchHistoryMapper;

    /**
     * 获取用户最新的搜索历史列表，默认十条
     *
     * @param userId
     * @return
     */
    @Override
    public List<RPanUserSearchHistoryVO> list(Long userId) {
        return rPanUserSearchHistoryMapper.selectRPanUserSearchHistoryVOListByUserId(userId);
    }

    /**
     * 保存用户搜索关键字，重复关键字会变为置顶状态
     *
     * @param searchContent
     * @param userId
     * @return
     */
    @Override
    public List<RPanUserSearchHistoryVO> save(String searchContent, Long userId) {
        try {
            saveSearchContent(searchContent, userId);
        } catch (DuplicateKeyException e) {
            stickSearchContent(searchContent, userId);
        }
        return list(userId);
    }

    /*********************************************************自定义*********************************************************/

    /**
     * 拼装实体信息
     *
     * @param searchContent
     * @param userId
     * @return
     */
    private RPanUserSearchHistory assembleRPanUserSearchHistoryEntity(String searchContent, Long userId) {
        RPanUserSearchHistory rPanUserSearchHistory = new RPanUserSearchHistory();
        rPanUserSearchHistory.setSearchContent(searchContent);
        rPanUserSearchHistory.setUserId(userId);
        rPanUserSearchHistory.setCreateTime(new Date());
        rPanUserSearchHistory.setUpdateTime(new Date());
        return rPanUserSearchHistory;
    }

    /**
     * 置顶搜索历史
     *
     * @param searchContent
     * @param userId
     * @return
     */
    private void stickSearchContent(String searchContent, Long userId) {
        if (rPanUserSearchHistoryMapper.updateUpdateTimeBySearchContentAndUserId(searchContent, userId) != CommonConstant.ONE_INT) {
            throw new RPanException("置顶搜索历史信息失败");
        }
    }

    /**
     * 保存搜索历史信息
     *
     * @param searchContent
     * @param userId
     */
    private void saveSearchContent(String searchContent, Long userId) {
        if (rPanUserSearchHistoryMapper.insertSelective(assembleRPanUserSearchHistoryEntity(searchContent, userId)) != CommonConstant.ONE_INT) {
            throw new RPanException("保存搜索历史信息失败");
        }
    }

}
