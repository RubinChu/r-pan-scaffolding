package com.rubin.rpan.modules.user.service;

import com.rubin.rpan.modules.user.vo.RPanUserSearchHistoryVO;

import java.util.List;

/**
 * 用户搜索历史业务处理接口
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
public interface IUserSearchHistoryService {

    List<RPanUserSearchHistoryVO> list(Long userId);

    List<RPanUserSearchHistoryVO> save(String searchContent, Long userId);

}
