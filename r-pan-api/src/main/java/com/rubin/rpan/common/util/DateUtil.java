package com.rubin.rpan.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
public class DateUtil {

    /**
     * 获取当天的日期字符串的格式常量
     */
    private static final String TODAY_DAY_STR_FORMAT = "yyyyMMdd";

    /**
     * 获取当前时间N天后的日期
     *
     * @param days
     * @return
     */
    public static Date afterDays(Integer days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    /**
     * 获取当天的日期字符串，按照yyyyMMdd格式返回
     * @return
     */
    public static String getTodayDayString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TODAY_DAY_STR_FORMAT);
        return simpleDateFormat.format(new Date());
    }

}
