package com.rubin.rpan.vo;

import com.rubin.rpan.entity.RPanFile;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * File return entity
 * Created by RubinChu on 2020/6/5 9:32
 */
@Data
@Accessors(chain = true)
public class RPanFileVO implements Serializable {

    private static final long serialVersionUID = 4055326597084822434L;

    /**
     * fileId
     */
    private String fileId;

    /**
     * parentId
     */
    private String parentId;

    /**
     * fileName
     */
    private String fileName;

    /**
     * showPath
     */
    private String showPath;

    /**
     * fileSize
     */
    private String fileSize;

    /**
     * file type
     *
     * @see com.rubin.rpan.common.constants.Constants.FileType
     */
    private Integer type;

    /**
     * createTime
     */
    private Date createTime;

    /**
     * updateTime
     */
    private Date updateTime;

    /**
     * assembly file display VO
     *
     * @param rPanFile
     * @return
     */
    public static RPanFileVO assembleRPanFileVO(RPanFile rPanFile) {
        RPanFileVO rPanFileVO = new RPanFileVO();
        rPanFileVO.setFileId(rPanFile.getFileId())
                .setParentId(rPanFile.getParentId())
                .setFileName(rPanFile.getFileName())
                .setShowPath(rPanFile.getShowPath())
                .setType(rPanFile.getType())
                .setFileSize(rPanFile.getFileSize())
                .setCreateTime(rPanFile.getCreateTime())
                .setUpdateTime(rPanFile.getUpdateTime());
        return rPanFileVO;
    }

}
