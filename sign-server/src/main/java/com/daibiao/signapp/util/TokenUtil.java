package com.daibiao.signapp.util;


import io.jsonwebtoken.*;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;


/**
 * @author hudaibiao-1
 * @version 1.0.0
 * @ClassName TokenUtil
 * @description TokenUtil
 * @date 2020-03-18 16:04:00
 */
public class TokenUtil {

    /**
     * 签名秘钥
     */
    private static final String SECRET = "admin";

    /**
     * TokenUtil
     *
     * @description 生成token
     * @author hudaibiao-1
     * @param id 用户id
     * @date 2020/9/23 20:23
     * @return token
     * @version v1.0.0
     */
    public static String createJwtToken(String id) {
        String issuer = "hudaibiao";
        String subject = "daibiao-sign";
        // 有效时长30分钟
        long ttlMillis = 30*60*1000;
        return createJwtToken(id, issuer, subject, ttlMillis);
    }

    /**
     * 生成Token
     *
     * @param id        编号
     * @param issuer    该JWT的签发者
     * @param subject   该JWT所面向的用户
     * @param ttlMillis 签发时间 （有效时间，过期会报错）
     * @return token String
     */
    private static String createJwtToken(String id, String issuer, String subject, long ttlMillis) {
        // 签名算法 ，将对token进行签名
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        // 生成签发时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        // 通过秘钥签名JWT
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        JwtBuilder builder = Jwts.builder().setId(id)
                .setIssuedAt(now)
                .setSubject(subject)
                .setIssuer(issuer)
                .signWith(signatureAlgorithm, signingKey);
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        return builder.compact();
    }

    /**
     * TokenUtil
     *
     * @description 解析token
     * @author hudaibiao-1
     * @param jwt jwt
     * @date 2020/9/23 20:24
     * @return Claims
     * @version v1.0.0
     */
    public static Claims parseJWT(String jwt) {
        return Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET))
                .parseClaimsJws(jwt).getBody();
    }

}
