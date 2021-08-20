package com.rubin.rpan.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * Jwt工具类
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
public class JwtUtil {

    public static final Long TWO_LONG = 2L;
    public static final String EMPTY_STR = "";

    /**
     * 秘钥
     */
    private final static String JWT_PRIVATE_KEY = "0123456789";

    /**
     * 刷新时间
     */
    private final static String RENEWAL_TIME = "RENEWAL_TIME";

    /**
     * 生成token
     *
     * @param subject
     * @param claimKey
     * @param claimValue
     * @param expire
     * @return
     */
    public static String generateToken(String subject, String claimKey, Object claimValue, Long expire) {
        String token = Jwts.builder()
                .setSubject(subject)
                .claim(claimKey, claimValue)
                .claim(RENEWAL_TIME, new Date(System.currentTimeMillis() + expire / TWO_LONG))
                .setExpiration(new Date(System.currentTimeMillis() + expire))
                .signWith(SignatureAlgorithm.HS256, JWT_PRIVATE_KEY)
                .compact();
        return token;
    }

    /**
     * 解析token
     *
     * @param token
     * @return
     */
    public static Object analyzeToken(String token, String claimKey) {
        if (StringUtils.isBlank(token)) {
            return null;
        }
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(JWT_PRIVATE_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.get(claimKey);
        } catch (Exception e) {
            return null;
        }
    }

}
