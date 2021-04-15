package com.rubin.rpan.modules.share.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.rubin.rpan.common.constant.CommonConstant;
import com.rubin.rpan.common.exception.RPanException;
import com.rubin.rpan.modules.file.service.IUserFileService;
import com.rubin.rpan.modules.file.vo.RPanUserFileVO;
import com.rubin.rpan.modules.share.dao.RPanShareFileMapper;
import com.rubin.rpan.modules.share.entity.RPanShareFile;
import com.rubin.rpan.modules.share.service.IShareFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
@Slf4j
public class ShareFileServiceImpl implements IShareFileService {

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
    public void saveBatch(String shareId, String shareFileIds, String userId) {
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
        if (rPanShareFileMapper.deleteByShareIdList(Splitter.on(CommonConstant.COMMON_SEPARATOR).splitToList(shareIds)) <= CommonConstant.ZERO_INT) {
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
    public List<RPanUserFileVO> getShareFileInfos(String shareId) {
        List<String> fileIds = rPanShareFileMapper.selectFileIdsByShareId(shareId);
        return iUserFileService.list(StringUtils.join(fileIds, CommonConstant.COMMON_SEPARATOR));
    }

    /**
     * 通过文件id查找对应的分享id
     *
     * @param fileIds
     * @return
     */
    @Override
    public List<String> getShareIdByFileIds(String fileIds) {
        return rPanShareFileMapper.selectShareIdByFileIds(Splitter.on(CommonConstant.COMMON_SEPARATOR).splitToList(fileIds));
    }

    /**
     * 获取该分享的所有文件列表
     *
     * @param shareId
     * @return
     */
    @Override
    public List<RPanUserFileVO> getAllShareFileInfos(String shareId) {
        List<String> fileIds = rPanShareFileMapper.selectFileIdsByShareId(shareId);
        if (CollectionUtils.isEmpty(fileIds)) {
            return Lists.newArrayList();
        }
        return iUserFileService.allList(StringUtils.join(fileIds, CommonConstant.COMMON_SEPARATOR));
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
    private List<RPanShareFile> assembleRPanShareFileList(String shareId, String shareFileIds, String userId) {
        final List<RPanShareFile> rPanShareFileList = Lists.newArrayList();
        Splitter.on(CommonConstant.COMMON_SEPARATOR).splitToList(shareFileIds).stream().forEach(shareFileId -> {
            RPanShareFile rPanShareFile = new RPanShareFile();
            rPanShareFile.setShareId(shareId)
                    .setFileId(shareFileId)
                    .setCreateUser(userId)
                    .setCreateTime(new Date());
            rPanShareFileList.add(rPanShareFile);
        });

        return rPanShareFileList;
    }

}
