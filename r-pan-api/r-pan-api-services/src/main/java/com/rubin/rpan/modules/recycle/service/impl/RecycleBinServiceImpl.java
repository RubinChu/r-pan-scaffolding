package com.rubin.rpan.modules.recycle.service.impl;

import com.rubin.rpan.modules.file.constant.FileConstant;
import com.rubin.rpan.modules.file.service.IUserFileService;
import com.rubin.rpan.modules.file.vo.RPanUserFileVO;
import com.rubin.rpan.modules.recycle.service.IRecycleBinService;
import com.rubin.rpan.modules.share.constant.ShareConstant;
import com.rubin.rpan.modules.share.service.IShareService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Transactional(rollbackFor = Exception.class)
public class RecycleBinServiceImpl implements IRecycleBinService {

    private static final Logger log = LoggerFactory.getLogger(RecycleBinServiceImpl.class);

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
    public List<RPanUserFileVO> list(Long userId) {
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
    public List<RPanUserFileVO> restore(String fileIds, Long userId) {
        iUserFileService.restoreUserFiles(fileIds, userId);
        iShareService.refreshShareStatus(iUserFileService.getAllAvailableFileIdByFileIds(fileIds));
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
    public List<RPanUserFileVO> delete(String fileIds, Long userId) {
        iUserFileService.physicalDeleteUserFiles(fileIds, userId);
        return list(userId);
    }

}
