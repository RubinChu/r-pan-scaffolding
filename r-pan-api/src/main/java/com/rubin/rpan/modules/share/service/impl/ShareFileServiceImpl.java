package com.rubin.rpan.modules.share.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.rubin.rpan.common.constant.CommonConstant;
import com.rubin.rpan.common.exception.RPanException;
import com.rubin.rpan.common.util.StringListUtil;
import com.rubin.rpan.modules.file.service.IUserFileService;
import com.rubin.rpan.modules.file.vo.RPanUserFileVO;
import com.rubin.rpan.modules.share.dao.RPanShareFileMapper;
import com.rubin.rpan.modules.share.entity.RPanShareFile;
import com.rubin.rpan.modules.share.service.IShareFileService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 项目分享文件业务处理类
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@Service(value = "shareFileService")
@Transactional
public class ShareFileServiceImpl implements IShareFileService {

    private static final Logger log = LoggerFactory.getLogger(ShareFileServiceImpl.class);

    @Autowired
    @Qualifier(value = "rPanShareFileMapper")
    private RPanShareFileMapper rPanShareFileMapper;

    @Autowired
    @Qualifier(value = "userFileService")
    private IUserFileService iUserFileService;

    /**
     * 批量保存分享文件列表
     *
     * @param shareId
     * @param shareFileIds
     * @param userId
     * @return
     */
    @Override
    public void saveBatch(Long shareId, String shareFileIds, Long userId) {
        List<RPanShareFile> rPanShareFileList = assembleRPanShareFileList(shareId, shareFileIds, userId);
        if (rPanShareFileMapper.insertBatch(rPanShareFileList) != rPanShareFileList.size()) {
            throw new RPanException("保存分享文件列表失败");
        }
    }

    /**
     * 取消分享列表
     *
     * @param shareIds
     * @return
     */
    @Override
    public void cancelShareFiles(String shareIds) {
        if (rPanShareFileMapper.deleteByShareIdList(StringListUtil.string2LongList(shareIds)) <= CommonConstant.ZERO_INT) {
            throw new RPanException("取消分享文件失败");
        }
    }

    /**
     * 通过分享id，查询文件列表信息
     *
     * @param shareId
     * @return
     */
    @Override
    public List<RPanUserFileVO> getShareFileInfos(Long shareId) {
        List<Long> fileIds = rPanShareFileMapper.selectFileIdsByShareId(shareId);
        return iUserFileService.list(StringListUtil.longListToString(fileIds));
    }

    /**
     * 通过文件id查找对应的分享id
     *
     * @param fileIds
     * @return
     */
    @Override
    public List<Long> getShareIdByFileIds(String fileIds) {
        return rPanShareFileMapper.selectShareIdByFileIds(StringListUtil.string2LongList(fileIds));
    }

    /**
     * 获取该分享的所有文件列表
     *
     * @param shareId
     * @return
     */
    @Override
    public List<RPanUserFileVO> getAllShareFileInfos(Long shareId) {
        List<Long> fileIds = rPanShareFileMapper.selectFileIdsByShareId(shareId);
        if (CollectionUtils.isEmpty(fileIds)) {
            return Lists.newArrayList();
        }
        return iUserFileService.allList(StringListUtil.longListToString(fileIds));
    }

    /**
     * 获取分享的所有关联的文件ID
     * @param shareId
     * @return
     */
    @Override
    public List<Long> getFileIdsByShareId(Long shareId) {
        return rPanShareFileMapper.selectFileIdsByShareId(shareId);
    }

    /******************************************************私有****************************************************/

    /**
     * 生成分享文件列表
     *
     * @param shareId
     * @param shareFileIds
     * @param userId
     * @return
     */
    private List<RPanShareFile> assembleRPanShareFileList(Long shareId, String shareFileIds, Long userId) {
        final List<RPanShareFile> rPanShareFileList = Lists.newArrayList();
        StringListUtil.string2LongList(shareFileIds).stream().forEach(shareFileId -> {
            RPanShareFile rPanShareFile = new RPanShareFile();
            rPanShareFile.setShareId(shareId);
            rPanShareFile.setFileId(shareFileId);
            rPanShareFile.setCreateUser(userId);
            rPanShareFile.setCreateTime(new Date());
            rPanShareFileList.add(rPanShareFile);
        });

        return rPanShareFileList;
    }

}
