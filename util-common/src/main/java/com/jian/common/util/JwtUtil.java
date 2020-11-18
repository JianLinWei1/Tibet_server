package com.jian.common.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @auther JianLinWei
 * @date 2020-03-23 17:13
 */
@Log4j2
public class JwtUtil {
    private static JwtUtil jwtUtil;

    private JwtUtil() {

    }

    public static synchronized JwtUtil getInstance() {
        if (jwtUtil != null)
            return jwtUtil;
        return new JwtUtil();
    }




    /**
     * 秘钥
     */
    private final String APPSECRET = "manage_token";

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 生成jwt
     *
     * @param username ,merchantId
     * @return
     */
    public String geneJsonWebToken(String username, String merchantId) throws Exception {

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(merchantId)) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 60);
        Date expiresAt = calendar.getTime();
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("userId", merchantId);
        String token = JWT.create()
                .withIssuer("auth0")
                //.withClaim("username", username)
                .withClaim("user", map)
                .withExpiresAt(expiresAt)
                // 使用了HMAC256加密算法。
                .sign(Algorithm.HMAC256(APPSECRET));

        return token;
    }


    /**
     * 校验token
     *
     * @param token
     * @return
     */
    public Map<? , ?> checkJWT(String token) throws Exception {


        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(APPSECRET)).withIssuer("auth0").build();
        DecodedJWT jwt = verifier.verify(token);
        Map<? , ?> map = jwt.getClaim("user").asMap();
        log.info("认证通过：");
        log.info("issuer: " + jwt.getIssuer());
        log.info("user: " + map);
        log.info("过期时间：      " + simpleDateFormat.format(jwt.getExpiresAt()));
        return map;

    }

    public Map<String, Object> obj2Map(Object obj) throws IllegalAccessException {
        Map<String, Object> objectMap = new HashMap<>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            objectMap.put(field.getName(), field.get(obj));
        }
        return objectMap;
    }

}
