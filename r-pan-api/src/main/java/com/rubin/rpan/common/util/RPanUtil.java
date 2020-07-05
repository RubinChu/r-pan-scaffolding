package com.rubin.rpan.common.util;

import com.rubin.rpan.common.constants.Constants;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * Created by rubin on 2020/6/4.
 */
@Component
@AllArgsConstructor
public class RPanUtil {

    private RedisUtil redisUtil;

    /**
     * Get the ID of the logged in user
     * Single device login function will be implemented here, so this set of codes does not consider concurrency
     *
     * @param request
     * @return
     */
    public Integer getUserId(HttpServletRequest request) {
        String token = request.getHeader(Constants.TOKEN_KEY);
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        Integer userId = JwtUtil.analyzeToken(token);
        if (Objects.isNull(userId)) {
            return null;
        }
        String redisValue = redisUtil.getString(Constants.USER_LOGIN_PREFIX + userId);
        if (StringUtils.isEmpty(redisValue)) {
            return null;
        }
        if (StringUtils.equals(redisValue, token)) {
            return userId;
        }
        return null;
    }

    /**
     * Randomly generated salt value
     *
     * @return
     */
    public String getSalt() {
        return MD5Util.getMD5(UUIDUtil.getUUID());
    }

    /**
     * Password encryption
     *
     * @param salt
     * @param inputPassword
     * @return
     */
    public String encryptPassword(String salt, String inputPassword) {
        return MD5Util.getMD5(MD5Util.getMD5(inputPassword) + salt);
    }

}
