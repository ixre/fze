package net.fze.ext.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import net.fze.util.Strings;
import net.fze.util.Times;
import net.fze.util.Types;
import net.fze.util.crypto.RSAKeyPair;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT
 */
public class Jwt {

    /**
     * 创建令牌
     *
     * @param privateKey 私钥
     * @param claims     JWT数据
     * @param expires    过期时间(单位：s)
     * @return
     */
    public static String create(String privateKey, Map<String, String> claims, long expires) {
        if (Strings.isNullOrEmpty(privateKey)) throw new IllegalArgumentException("privateKey");
        // 去掉头尾信息
        privateKey = RSAKeyPair.removeBeginEnd(privateKey);
        Algorithm algorithm = Algorithm.HMAC256(privateKey);
        JWTCreator.Builder builder = JWT.create();
        claims.forEach(builder::withClaim);
        builder.withIssuedAt(new Date());
        builder.withExpiresAt(Times.unixTime(expires, 0));
        return builder.sign(algorithm);
    }

    /**
     * 创建令牌
     *
     * @param privateKey 私钥
     * @param claims     JWT数据
     * @param expires    过期时间(单位：s)
     * @return
     */
    public static String createWithClaims(String privateKey, Map<String, Claim> claims, long expires) {
        Map<String, String> map = new HashMap<>();
        map.put("iss", claims.get("iss").asString());
        map.put("aud", claims.get("aud").asString());
        map.put("sub", claims.get("sub").asString());
        return create(privateKey, map, expires);
    }

    /**
     * 创建令牌
     *
     * @param privateKey 私钥
     * @param issuer     颁发者
     * @param sub        主题
     * @param aud        接收方用户
     * @param exp        过期时间,时间戳,精确到秒
     * @return
     */
    public static String create(String privateKey, String issuer, String sub, String aud, long exp) {
        Map<String, String> claims = new HashMap<>();
        claims.put("iss", issuer);
        claims.put("aud", aud);
        claims.put("sub", Types.orValue(sub, ""));
        return create(privateKey, claims, exp);
    }

    /**
     * 解码令牌
     *
     * @param privateKey 私钥
     * @param token      令牌
     * @return
     */
    public static JwtDecoder decode(String privateKey, String token) {
        if (Strings.isNullOrEmpty(token)) throw new IllegalArgumentException("token");
        if (Strings.isNullOrEmpty(privateKey)) throw new IllegalArgumentException("privateKey");
        // 去掉头尾信息
        privateKey = RSAKeyPair.removeBeginEnd(privateKey);
        Algorithm algorithm = Algorithm.HMAC256(privateKey);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        try {
            DecodedJWT jwt = jwtVerifier.verify(token);
            return new JwtDecoder(jwt);
        } catch (Throwable ex) {
            if (ex instanceof TokenExpiredException) {
                throw new JwtTokenExpiredException((TokenExpiredException) ex);
            }
            throw ex;
        }
    }
}
