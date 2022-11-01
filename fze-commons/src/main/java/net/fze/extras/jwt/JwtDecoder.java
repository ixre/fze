package net.fze.extras.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import net.fze.util.Strings;
import net.fze.util.Times;
import net.fze.util.crypto.RSAKeyPair;
import com.auth0.jwt.JWTVerifier;

import java.util.Map;

/**
 * JWT解码器
 */
public class JwtDecoder {
    private DecodedJWT jwt;

    JwtDecoder(DecodedJWT jwt) {
        this.jwt = jwt;
    }

    public Map<String, Claim> getClaim() {
        return this.jwt.getClaims();
    }

    public boolean isExpires(long unix) {
        long exp = jwt.getClaim("exp").asLong();
        return exp - (unix == 0 ? Times.unix() : unix) <= 0;
    }

    public boolean checkIssuer(String issuer) {
        return jwt.getIssuer().equals(issuer);
    }

    public String getAud() {
        return jwt.getClaim("aud").asString();
    }

}

