package com.rubin.rpan.common.util;

import com.rubin.rpan.common.constants.Constants;
import com.rubin.rpan.entity.RPanUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.Objects;

/**
 * Created by rubin on 2020/6/4.
 */

public class JwtUtil {

    /**
     * Generate successful login token
     *
     * @param rPanUser
     * @param expire
     * @return
     */
    public static String generateToken(RPanUser rPanUser, long expire) {
        String token = Jwts.builder()
                .setSubject(rPanUser.getUsername())
                .claim(Constants.LOGIN_USER_ID, rPanUser.getId())
                .claim(Constants.RENEWAL_TIME, new Date(System.currentTimeMillis() + expire / 2))
                .setExpiration(new Date(System.currentTimeMillis() + expire))
                .signWith(SignatureAlgorithm.HS256, Constants.JWT_PRIVATE_KEY)
                .compact();
        return token;
    }

    /**
     * Parse token
     *
     * @param token
     * @return
     */
    public static Integer analyzeToken(String token) {
        if (StringUtils.isBlank(token)) {
            return null;
        }
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(Constants.JWT_PRIVATE_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            Object userId = claims.get(Constants.LOGIN_USER_ID);
            return Objects.isNull(userId) ? null : (Integer) userId;
        } catch (Exception e) {
            return null;
        }
    }

}
