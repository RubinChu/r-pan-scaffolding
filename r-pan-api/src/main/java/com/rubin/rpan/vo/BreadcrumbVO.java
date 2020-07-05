package com.rubin.rpan.vo;

import com.rubin.rpan.common.constants.Constants;
import com.rubin.rpan.entity.RPanFile;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by rubin on 2020/6/27.
 */
@Data
@Accessors(chain = true)
public class BreadcrumbVO implements Serializable {
    private static final long serialVersionUID = -6628343269579741373L;

    /**
     * breadcrumb key
     */
    private String id;

    /**
     * parentId
     */
    private String parentId;

    /**
     * breadcrumb name
     */
    private String name;

    public static BreadcrumbVO assembleBreadcrumbVO(RPanFile rPanFile) {
        BreadcrumbVO breadcrumbVO = new BreadcrumbVO();
        breadcrumbVO.setId(rPanFile.getFileId()).setParentId(rPanFile.getParentId());
        if (Objects.equals(Constants.TOP_STR, breadcrumbVO.getParentId())) {
            breadcrumbVO.setName(Constants.ALL_FILE_CN_STR);
        } else {
            breadcrumbVO.setName(rPanFile.getFileName());
        }
        return breadcrumbVO;
    }

}
