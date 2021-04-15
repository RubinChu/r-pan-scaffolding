package com.rubin.rpan.modules.user.service.impl;

import com.rubin.rpan.common.constant.CommonConstant;
import com.rubin.rpan.common.exception.RPanException;
import com.rubin.rpan.modules.user.dao.RPanUserSearchHistoryMapper;
import com.rubin.rpan.modules.user.entity.RPanUserSearchHistory;
import com.rubin.rpan.modules.user.service.IUserSearchHistoryService;
import com.rubin.rpan.modules.user.vo.RPanUserSearchHistoryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 用户搜索历史业务处理实现类
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@Service(value = "userSearchHistoryService")
@Slf4j
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
    public List<RPanUserSearchHistoryVO> list(String userId) {
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
    public List<RPanUserSearchHistoryVO> save(String searchContent, String userId) {
        if (checkForDuplication(searchContent, userId)) {
            stickSearchContent(searchContent, userId);
        } else {
            saveSearchContent(searchContent, userId);
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
    private RPanUserSearchHistory assembleRPanUserSearchHistoryEntity(String searchContent, String userId) {
        RPanUserSearchHistory rPanUserSearchHistory = new RPanUserSearchHistory();
        rPanUserSearchHistory.setSearchContent(searchContent)
                .setUserId(userId)
                .setCreateTime(new Date())
                .setUpdateTime(new Date());
        return rPanUserSearchHistory;
    }

    /**
     * 检查是否有重复数据
     *
     * @param searchContent
     * @param userId
     * @return
     */
    private boolean checkForDuplication(String searchContent, String userId) {
        return !Objects.isNull(rPanUserSearchHistoryMapper.selectBySearchContentAndUserId(searchContent, userId));
    }

    /**
     * 置顶搜索历史
     *
     * @param searchContent
     * @param userId
     * @return
     */
    private void stickSearchContent(String searchContent, String userId) {
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
    private void saveSearchContent(String searchContent, String userId) {
        if (rPanUserSearchHistoryMapper.insertSelective(assembleRPanUserSearchHistoryEntity(searchContent, userId)) != CommonConstant.ONE_INT) {
            throw new RPanException("保存搜索历史信息失败");
        }
    }

}
