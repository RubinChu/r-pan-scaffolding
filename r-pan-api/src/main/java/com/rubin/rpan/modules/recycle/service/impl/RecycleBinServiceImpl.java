package com.rubin.rpan.modules.recycle.service.impl;

import com.rubin.rpan.modules.file.constant.FileConstant;
import com.rubin.rpan.modules.file.service.IUserFileService;
import com.rubin.rpan.modules.file.vo.RPanUserFileVO;
import com.rubin.rpan.modules.recycle.service.IRecycleBinService;
import com.rubin.rpan.modules.share.constant.ShareConstant;
import com.rubin.rpan.modules.share.service.IShareService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 回收站业务处理实现
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@Service(value = "recycleBinService")
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class RecycleBinServiceImpl implements IRecycleBinService {

    @Autowired
    @Qualifier(value = "userFileService")
    private IUserFileService iUserFileService;

    @Autowired
    @Qualifier(value = "shareService")
    private IShareService iShareService;

    /**
     * 获取回收站的文件列表
     *
     * @param userId
     * @return
     */
    @Override
    public List<RPanUserFileVO> list(String userId) {
        return iUserFileService.list(null, FileConstant.ALL_FILE_TYPE, userId, FileConstant.DelFlagEnum.YES.getCode());
    }

    /**
     * 还原文件
     *
     * @param fileIds
     * @param userId
     * @return
     */
    @Override
    public List<RPanUserFileVO> restore(String fileIds, String userId) {
        iUserFileService.restoreUserFiles(fileIds, userId);
        iShareService.changeShareStatus(fileIds, ShareConstant.ShareStatus.NORMAL);
        return list(userId);
    }

    /**
     * 回收站删除文件
     *
     * @param fileIds
     * @param userId
     * @return
     */
    @Override
    public List<RPanUserFileVO> delete(String fileIds, String userId) {
        iUserFileService.physicalDeleteUserFiles(fileIds, userId);
        return list(userId);
    }

}
