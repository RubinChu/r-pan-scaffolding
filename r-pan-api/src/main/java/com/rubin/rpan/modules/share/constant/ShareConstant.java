package com.rubin.rpan.modules.share.constant;

import com.rubin.rpan.common.constant.CommonConstant;

import java.util.Objects;

/**
 * 分享模块常量类
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
public class ShareConstant {

    /**
     * 分享类型枚举类
     */
    public enum ShareType {
        NEED_SHARE_CODE(0, "有提取码");

        ShareType(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        private Integer code;
        private String name;

        public Integer getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 分享天数枚举类
     */
    public enum ShareDayType {
        PERMANENT_VALIDITY(0, 0, "永久有效"),
        SEVEN_DAYS_VALIDITY(1, 7, "7天有效"),
        THIRTY_DAYS_VALIDITY(2, 30, "30天有效");
        private Integer code;
        private Integer days;
        private String name;

        ShareDayType(Integer code, Integer days, String name) {
            this.code = code;
            this.days = days;
            this.name = name;
        }

        public Integer getCode() {
            return code;
        }

        public Integer getDays() {
            return days;
        }

        public String getName() {
            return name;
        }

        public static Integer getDaysByCode(Integer code) {
            if (Objects.isNull(code)) {
                return CommonConstant.MINUS_ONE_INT;
            }
            for (ShareDayType shareDayType : values()) {
                if (Objects.equals(shareDayType.getCode(), code)) {
                    return shareDayType.getDays();
                }
            }
            return CommonConstant.MINUS_ONE_INT;
        }
    }

    /**
     * 分享状态枚举类
     */
    public enum ShareStatus {
        NORMAL(0, "正常"),
        FILE_DELETED(1, "有文件被删除");

        ShareStatus(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        private Integer code;
        private String name;

        public Integer getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

}
