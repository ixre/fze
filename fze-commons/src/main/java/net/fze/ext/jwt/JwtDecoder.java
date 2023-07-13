package net.fze.ext.jwt;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import net.fze.util.Times;
import java.util.Map;

/**
 * JWT解码器
 */
public class JwtDecoder {
    private final DecodedJWT jwt;

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

