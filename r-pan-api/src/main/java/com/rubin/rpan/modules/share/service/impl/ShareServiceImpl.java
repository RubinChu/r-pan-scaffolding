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
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class ShareServiceImpl implements IShareService {

    private static final Logger log = LoggerFactory.getLogger(ShareServiceImpl.class);

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
    @Qualifier(value = "idGenerator")
    private IdGenerator idGenerator;

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
    public RPanUserShareUrlVO create(String shareName, Integer shareType, Integer shareDayType, String shareFileIds, Long userId) {
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
    public List<RPanUserShareUrlVO> list(Long userId) {
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
    public void cancel(String shareIds, Long userId) {
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
    public RPanUserShareDetailVO detail(Long shareId) {
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
    public RPanUserShareSimpleDetailVO simpleDetail(Long shareId) {
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
    public String checkShareCode(Long shareId, String shareCode) {
        RPanShare rPanShare = checkShareStatus(shareId);
        if (!Objects.equals(rPanShare.getShareCode(), shareCode)) {
            throw new RPanException("分享码错误");
        }
        String token = JwtUtil.generateToken(UUIDUtil.getUUID(), CommonConstant.SHARE_ID, shareId, CommonConstant.ONE_HOUR_LONG);
        return token;
    }

    /**
     * 通过文件id刷新相关分享的分享状态
     *
     * @param fileIds
     */
    @Override
    public void refreshShareStatus(String fileIds) {
        List<Long> shareIds = iShareFileService.getShareIdByFileIds(fileIds);
        if (CollectionUtils.isEmpty(shareIds)) {
            return;
        }
        Set<Long> shareIdSet = new HashSet<>(shareIds);
        shareIdSet.stream().forEach(this::refreshOneShareStatus);
    }

    /**
     * 获取下一级的文件列表
     *
     * @param shareId
     * @param parentId
     * @return
     */
    @Override
    public List<RPanUserFileVO> fileList(Long shareId, Long parentId) {
        checkShareStatus(shareId);
        List<RPanUserFileVO> rPanUserFileVOList = checkFileIsOnShareStatusAndGetAllShareUserFiles(shareId, StringListUtil.longListToString(parentId));
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
    public void save(Long shareId, String fileIds, Long targetParentId, Long userId) {
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
    public void download(Long shareId, Long fileId, HttpServletResponse response) {
        checkShareStatus(shareId);
        checkFileIsOnShareStatus(shareId, StringListUtil.longListToString(fileId));
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
    private RPanShare saveShare(String shareName, Integer shareType, Integer shareDayType, Long userId) {
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
    private void saveShareFile(Long shareId, String shareFileIds, Long userId) {
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
    private RPanShare assembleShareEntity(String shareName, Integer shareType, Integer shareDayType, Long userId) {
        RPanShare rPanShare = new RPanShare();
        Integer shareDay = ShareConstant.ShareDayType.getDaysByCode(shareDayType);
        if (Objects.equals(CommonConstant.MINUS_ONE_INT, shareDay)) {
            throw new RPanException("分享天数获取失败");
        }
        Long shareId = idGenerator.nextId();
        rPanShare.setShareId(shareId);
        rPanShare.setShareName(shareName);
        rPanShare.setShareType(shareType);
        rPanShare.setShareDayType(shareDayType);
        rPanShare.setShareDay(shareDay);
        rPanShare.setShareEndTime(DateUtil.afterDays(shareDay));
        rPanShare.setShareUrl(createShareUrl(shareId));
        rPanShare.setShareCode(ShareCodeUtil.get());
        rPanShare.setShareStatus(ShareConstant.ShareStatus.NORMAL.getCode());
        rPanShare.setCreateUser(userId);
        rPanShare.setCreateTime(new Date());
        return rPanShare;
    }

    /**
     * 创建分享链接
     *
     * @param shareId
     * @return
     */
    private String createShareUrl(Long shareId) {
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
        rPanUserShareUrlVO.setShareId(rPanShare.getShareId());
        rPanUserShareUrlVO.setShareName(rPanShare.getShareName());
        rPanUserShareUrlVO.setShareUrl(rPanShare.getShareUrl());
        rPanUserShareUrlVO.setShareCode(rPanShare.getShareCode());
        rPanUserShareUrlVO.setShareStatus(rPanShare.getShareStatus());
        return rPanUserShareUrlVO;
    }

    /**
     * 取消分享链接
     *
     * @param shareIds
     * @param userId
     */
    private void cancelShares(String shareIds, Long userId) {
        List<Long> shareIdList = StringListUtil.string2LongList(shareIds);
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
        rPanUserShareDetailVO.setrPanUserFileVOList(iShareFileService.getShareFileInfos(rPanShare.getShareId()));
        rPanUserShareDetailVO.setShareUserInfoVO(iUserService.getShareUserInfo(rPanShare.getCreateUser()));
        return rPanUserShareDetailVO;
    }

    /**
     * 校验分享状态
     *
     * @param shareId
     */
    private RPanShare checkShareStatus(Long shareId) {
        RPanShare rPanShare = rPanShareMapper.selectByPrimaryKey(shareId);
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
    private List<RPanUserFileVO> checkFileIsOnShareStatusAndGetAllShareUserFiles(Long shareId, String fileIds) {
        List<RPanUserFileVO> rPanUserFileVOList = iShareFileService.getAllShareFileInfos(shareId);
        if (CollectionUtils.isEmpty(rPanUserFileVOList)) {
            throw new RPanException("分享信息不可用");
        }
        Set<Long> shareFileIdSet = rPanUserFileVOList.stream().map(RPanUserFileVO::getFileId).collect(Collectors.toSet());
        int originSize = shareFileIdSet.size();
        shareFileIdSet.addAll(StringListUtil.string2LongList(fileIds));
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
    private void checkFileIsOnShareStatus(Long shareId, String fileIds) {
        checkFileIsOnShareStatusAndGetAllShareUserFiles(shareId, fileIds);
    }

    /**
     * 刷新一个分享的状态
     *
     * @param shareId
     */
    private void refreshOneShareStatus(Long shareId) {
        ShareConstant.ShareStatus shareStatus = ShareConstant.ShareStatus.NORMAL;
        if (!checkShareFileAvailable(shareId)) {
            shareStatus = ShareConstant.ShareStatus.FILE_DELETED;
        }
        if (rPanShareMapper.selectByPrimaryKey(shareId).getShareStatus().equals(shareStatus.getCode())) {
            return;
        }
        if (rPanShareMapper.changeShareStatusByShareId(shareId, shareStatus.getCode()) != CommonConstant.ONE_INT) {
            throw new RPanException("更新分享状态失败");
        }
    }

    /**
     * 校验分享文件是否有效
     *
     * @param shareId
     * @return
     */
    private boolean checkShareFileAvailable(Long shareId) {
        List<Long> fileIds = iShareFileService.getFileIdsByShareId(shareId);
        return iUserFileService.checkAllUpFileAvailable(fileIds);
    }


}
