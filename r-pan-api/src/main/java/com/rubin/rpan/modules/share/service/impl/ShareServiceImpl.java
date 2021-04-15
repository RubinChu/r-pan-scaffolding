package com.rubin.rpan.modules.share.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.rubin.rpan.common.config.props.FileShareConfig;
import com.rubin.rpan.common.constant.CommonConstant;
import com.rubin.rpan.common.exception.RPanException;
import com.rubin.rpan.common.response.ResponseCode;
import com.rubin.rpan.common.util.*;
import com.rubin.rpan.modules.file.service.IUserFileService;
import com.rubin.rpan.modules.file.vo.RPanUserFileVO;
import com.rubin.rpan.modules.share.constant.ShareConstant;
import com.rubin.rpan.modules.share.dao.RPanShareMapper;
import com.rubin.rpan.modules.share.entity.RPanShare;
import com.rubin.rpan.modules.share.service.IShareFileService;
import com.rubin.rpan.modules.share.service.IShareService;
import com.rubin.rpan.modules.share.vo.RPanUserShareDetailVO;
import com.rubin.rpan.modules.share.vo.RPanUserShareSimpleDetailVO;
import com.rubin.rpan.modules.share.vo.RPanUserShareUrlVO;
import com.rubin.rpan.modules.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 项目分享业务处理类
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@Service(value = "shareService")
@Transactional
@Slf4j
public class ShareServiceImpl implements IShareService {

    @Autowired
    @Qualifier(value = "rPanShareMapper")
    private RPanShareMapper rPanShareMapper;

    @Autowired
    @Qualifier(value = "shareFileService")
    private IShareFileService iShareFileService;

    @Autowired
    @Qualifier(value = "userFileService")
    private IUserFileService iUserFileService;

    @Autowired
    @Qualifier(value = "userService")
    private IUserService iUserService;

    @Autowired
    @Qualifier(value = "fileShareConfig")
    private FileShareConfig fileShareConfig;

    @Autowired
    @Qualifier(value = "redisUtil")
    private RedisUtil redisUtil;

    /**
     * 创建分享链接
     *
     * @param shareName
     * @param shareType
     * @param shareDayType
     * @param shareFileIds
     * @param userId
     * @return
     */
    @Override
    public RPanUserShareUrlVO create(String shareName, Integer shareType, Integer shareDayType, String shareFileIds, String userId) {
        RPanShare rPanShare = saveShare(shareName, shareType, shareDayType, userId);
        saveShareFile(rPanShare.getShareId(), shareFileIds, userId);
        return assembleShareUrlVO(rPanShare);
    }

    /**
     * 查询分享列表
     *
     * @param userId
     * @return
     */
    @Override
    public List<RPanUserShareUrlVO> list(String userId) {
        return rPanShareMapper.selectRPanUserShareUrlVOListByUserId(userId);
    }

    /**
     * 取消分享链接（支持批量）
     *
     * @param shareIds
     * @param userId
     * @return
     */
    @Override
    public void cancel(String shareIds, String userId) {
        cancelShares(shareIds, userId);
        cancelShareFiles(shareIds);
    }

    /**
     * 获取分享详情
     *
     * @param shareId
     * @return
     */
    @Override
    public RPanUserShareDetailVO detail(String shareId) {
        RPanShare rPanShare = checkShareStatus(shareId);
        return assembleShareDetailVO(rPanShare);
    }

    /**
     * 获取简单分享详情
     *
     * @param shareId
     * @return
     */
    @Override
    public RPanUserShareSimpleDetailVO simpleDetail(String shareId) {
        RPanShare rPanShare = checkShareStatus(shareId);
        RPanUserShareSimpleDetailVO rPanUserShareSimpleDetailVO = new RPanUserShareSimpleDetailVO(rPanShare);
        rPanUserShareSimpleDetailVO.setShareUserInfoVO(iUserService.getShareUserInfo(rPanShare.getCreateUser()));
        return rPanUserShareSimpleDetailVO;
    }

    /**
     * 校验分享码
     *
     * @param shareId
     * @param shareCode
     * @return
     */
    @Override
    public String checkShareCode(String shareId, String shareCode) {
        RPanShare rPanShare = checkShareStatus(shareId);
        if (!Objects.equals(rPanShare.getShareCode(), shareCode)) {
            throw new RPanException("分享码错误");
        }
        String token = JwtUtil.generateToken(UUIDUtil.getUUID(), CommonConstant.SHARE_ID, shareId, CommonConstant.ONE_HOUR_LONG);
        redisUtil.setString(CommonConstant.SHARE_CODE_PREFIX + shareId, token, CommonConstant.ONE_HOUR_LONG);
        return token;
    }

    /**
     * 通过文件id修改相关的分享状态
     *
     * @param fileIds
     * @param shareStatus
     */
    @Override
    public void changeShareStatus(String fileIds, ShareConstant.ShareStatus shareStatus) {
        List<String> shareIds = iShareFileService.getShareIdByFileIds(fileIds);
        if (CollectionUtils.isEmpty(shareIds)) {
            return;
        }
        Set<String> shareIdSet = new HashSet<>(shareIds);
        if (rPanShareMapper.changeShareStatusByShareId(shareIdSet, shareStatus.getCode()) != shareIdSet.size()) {
            throw new RPanException("更新分享状态失败");
        }
    }

    /**
     * 获取下一级的文件列表
     *
     * @param shareId
     * @param parentId
     * @return
     */
    @Override
    public List<RPanUserFileVO> fileList(String shareId, String parentId) {
        checkShareStatus(shareId);
        List<RPanUserFileVO> rPanUserFileVOList = checkFileIsOnShareStatusAndGetAllShareUserFiles(shareId, parentId);
        rPanUserFileVOList = rPanUserFileVOList.stream().collect(Collectors.groupingBy(RPanUserFileVO::getParentId)).get(parentId);
        if (CollectionUtils.isEmpty(rPanUserFileVOList)) {
            return Lists.newArrayList();
        }
        return rPanUserFileVOList;
    }

    /**
     * 保存至我的网盘
     *
     * @param fileIds
     * @param targetParentId
     * @param userId
     * @return
     */
    @Override
    public void save(String shareId, String fileIds, String targetParentId, String userId) {
        checkShareStatus(shareId);
        checkFileIsOnShareStatus(shareId, fileIds);
        iUserFileService.saveBatch(fileIds, targetParentId, userId);
    }

    /**
     * 分享文件下载
     *
     * @param shareId
     * @param fileId
     * @param response
     */
    @Override
    public void download(String shareId, String fileId, HttpServletResponse response) {
        checkShareStatus(shareId);
        checkFileIsOnShareStatus(shareId, fileId);
        iUserFileService.download(fileId, response);
    }

    /******************************************************私有****************************************************/

    /**
     * 保存分享信息
     *
     * @param shareName
     * @param shareType
     * @param shareDayType
     * @param userId
     * @return
     */
    private RPanShare saveShare(String shareName, Integer shareType, Integer shareDayType, String userId) {
        RPanShare rPanShare = assembleShareEntity(shareName, shareType, shareDayType, userId);
        if (rPanShareMapper.insertSelective(rPanShare) != CommonConstant.ONE_INT) {
            throw new RPanException("创建分享链接失败");
        }
        return rPanShare;
    }

    /**
     * 保存分享文件列表
     *
     * @param shareId
     * @param shareFileIds
     * @param userId
     */
    private void saveShareFile(String shareId, String shareFileIds, String userId) {
        iShareFileService.saveBatch(shareId, shareFileIds, userId);
    }

    /**
     * 拼装分享主体信息
     *
     * @param shareName
     * @param shareType
     * @param shareDayType
     * @param userId
     * @return
     */
    private RPanShare assembleShareEntity(String shareName, Integer shareType, Integer shareDayType, String userId) {
        RPanShare rPanShare = new RPanShare();
        Integer shareDay = ShareConstant.ShareDayType.getDaysByCode(shareDayType);
        if (Objects.equals(CommonConstant.MINUS_ONE_INT, shareDay)) {
            throw new RPanException("分享天数获取失败");
        }
        String shareId = UUIDUtil.getUUID();
        rPanShare.setShareId(shareId)
                .setShareName(shareName)
                .setShareType(shareType)
                .setShareDayType(shareDayType)
                .setShareDay(shareDay)
                .setShareEndTime(DateUtil.afterDays(shareDay))
                .setShareUrl(createShareUrl(shareId))
                .setShareCode(ShareCodeUtil.get())
                .setShareStatus(ShareConstant.ShareStatus.NORMAL.getCode())
                .setCreateUser(userId)
                .setCreateTime(new Date());
        return rPanShare;
    }

    /**
     * 创建分享链接
     *
     * @param shareId
     * @return
     */
    private String createShareUrl(String shareId) {
        return fileShareConfig.getPrefix() + shareId;
    }

    /**
     * 拼装分享链接返回实体
     *
     * @param rPanShare
     * @return
     */
    private RPanUserShareUrlVO assembleShareUrlVO(RPanShare rPanShare) {
        RPanUserShareUrlVO rPanUserShareUrlVO = new RPanUserShareUrlVO();
        rPanUserShareUrlVO.setShareId(rPanShare.getShareId())
                .setShareName(rPanShare.getShareName())
                .setShareUrl(rPanShare.getShareUrl())
                .setShareCode(rPanShare.getShareCode())
                .setShareStatus(rPanShare.getShareStatus());
        return rPanUserShareUrlVO;
    }

    /**
     * 取消分享链接
     *
     * @param shareIds
     * @param userId
     */
    private void cancelShares(String shareIds, String userId) {
        List<String> shareIdList = Splitter.on(CommonConstant.COMMON_SEPARATOR).splitToList(shareIds);
        if (rPanShareMapper.deleteByShareIdListAndUserId(shareIdList, userId) != shareIdList.size()) {
            throw new RPanException("取消分享失败");
        }
    }

    /**
     * 取消分享文件列表
     *
     * @param shareIds
     */
    private void cancelShareFiles(String shareIds) {
        iShareFileService.cancelShareFiles(shareIds);
    }

    /**
     * 拼装分享详情返回实体
     *
     * @param rPanShare
     * @return
     */
    private RPanUserShareDetailVO assembleShareDetailVO(RPanShare rPanShare) {
        RPanUserShareDetailVO rPanUserShareDetailVO = new RPanUserShareDetailVO(rPanShare);
        rPanUserShareDetailVO.setRPanUserFileVOList(iShareFileService.getShareFileInfos(rPanShare.getShareId()));
        rPanUserShareDetailVO.setShareUserInfoVO(iUserService.getShareUserInfo(rPanShare.getCreateUser()));
        return rPanUserShareDetailVO;
    }

    /**
     * 校验分享状态
     *
     * @param shareId
     */
    private RPanShare checkShareStatus(String shareId) {
        RPanShare rPanShare = rPanShareMapper.selectByShareId(shareId);
        if (Objects.isNull(rPanShare)) {
            throw new RPanException(ResponseCode.SHARE_CANCELLED);
        }
        if (Objects.equals(ShareConstant.ShareStatus.FILE_DELETED.getCode(), rPanShare.getShareStatus())) {
            throw new RPanException(ResponseCode.SHARE_FILE_MISS);
        }
        if (Objects.equals(ShareConstant.ShareDayType.PERMANENT_VALIDITY.getCode(), rPanShare.getShareDayType())) {
            return rPanShare;
        }
        if (rPanShare.getShareEndTime().before(new Date())) {
            throw new RPanException(ResponseCode.SHARE_EXPIRE);
        }
        return rPanShare;
    }

    /**
     * 校验该文件是否在分享状态并且返回该用户分享涉及的全部文件列表
     *
     * @param shareId
     * @param fileIds
     * @return
     */
    private List<RPanUserFileVO> checkFileIsOnShareStatusAndGetAllShareUserFiles(String shareId, String fileIds) {
        List<RPanUserFileVO> rPanUserFileVOList = iShareFileService.getAllShareFileInfos(shareId);
        if (CollectionUtils.isEmpty(rPanUserFileVOList)) {
            throw new RPanException("分享信息不可用");
        }
        Set<String> shareFileIdSet = rPanUserFileVOList.stream().map(RPanUserFileVO::getFileId).collect(Collectors.toSet());
        int originSize = shareFileIdSet.size();
        shareFileIdSet.addAll(Splitter.on(CommonConstant.COMMON_SEPARATOR).splitToList(fileIds));
        if (originSize != shareFileIdSet.size()) {
            throw new RPanException(ResponseCode.ERROR_PARAM);
        }
        return rPanUserFileVOList;
    }

    /**
     * 校验该文件列表是否处于分享状态
     *
     * @param shareId
     * @param fileIds
     */
    private void checkFileIsOnShareStatus(String shareId, String fileIds) {
        checkFileIsOnShareStatusAndGetAllShareUserFiles(shareId, fileIds);
    }

}
