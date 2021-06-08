package com.rubin.rpan.modules.share.service;

import com.rubin.rpan.modules.file.vo.RPanUserFileVO;
import com.rubin.rpan.modules.share.constant.ShareConstant;
import com.rubin.rpan.modules.share.vo.RPanUserShareDetailVO;
import com.rubin.rpan.modules.share.vo.RPanUserShareSimpleDetailVO;
import com.rubin.rpan.modules.share.vo.RPanUserShareUrlVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 项目分享业务处理接口
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
public interface IShareService {

    RPanUserShareUrlVO create(String shareName, Integer shareType, Integer shareDayType, String shareFileIds, Long userId);

    List<RPanUserShareUrlVO> list(Long userId);

    void cancel(String shareIds, Long userId);

    RPanUserShareDetailVO detail(Long shareId);

    RPanUserShareSimpleDetailVO simpleDetail(Long shareId);

    String checkShareCode(Long shareId, String shareCode);

    void refreshShareStatus(String fileIds);

    List<RPanUserFileVO> fileList(Long shareId, Long parentId);

    void save(Long shareId, String fileIds, Long targetParentId, Long userId);

    void download(Long shareId, Long fileId, HttpServletResponse response);

}
