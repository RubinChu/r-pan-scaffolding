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

    RPanUserShareUrlVO create(String shareName, Integer shareType, Integer shareDayType, String shareFileIds, String userId);

    List<RPanUserShareUrlVO> list(String userId);

    void cancel(String shareIds, String userId);

    RPanUserShareDetailVO detail(String shareId);

    RPanUserShareSimpleDetailVO simpleDetail(String shareId);

    String checkShareCode(String shareId, String shareCode);

    void changeShareStatus(String fileIds, ShareConstant.ShareStatus shareStatus);

    List<RPanUserFileVO> fileList(String shareId, String parentId);

    void save(String shareId, String fileIds, String targetParentId, String userId);

    void download(String shareId, String fileId, HttpServletResponse response);

}
