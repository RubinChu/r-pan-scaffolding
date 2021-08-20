package com.rubin.rpan.util;

import com.google.common.base.Splitter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 字符串和集合转换工具类
 */
public class StringListUtil {

    public static final String COMMON_SEPARATOR = "__,__";

    /**
     * 字符串分隔成Integer集合
     *
     * @param origin
     * @return
     */
    public static List<Integer> string2IntegerList(String origin) {
        return Splitter.on(COMMON_SEPARATOR).splitToList(origin).stream().map(Integer::valueOf).collect(Collectors.toList());
    }

    /**
     * 字符串分隔成Long集合
     *
     * @param origin
     * @return
     */
    public static List<Long> string2LongList(String origin) {
        return Splitter.on(COMMON_SEPARATOR).splitToList(origin).stream().map(Long::valueOf).collect(Collectors.toList());
    }

    /**
     * Long集合按照统一分割符拼装字符串
     *
     * @param ids
     * @return
     */
    public static String longListToString(Long... ids) {
        return StringUtils.join(Arrays.asList(ids), COMMON_SEPARATOR);
    }

    /**
     * Long集合按照统一分割符拼装字符串
     *
     * @param ids
     * @return
     */
    public static String longListToString(Collection<Long> ids) {
        Long[] idArr = new Long[ids.size()];
        ids.toArray(idArr);
        return longListToString(idArr);
    }

}
