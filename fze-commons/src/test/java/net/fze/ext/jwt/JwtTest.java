package net.fze.ext.jwt;

import com.auth0.jwt.interfaces.Claim;
import net.fze.util.Times;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;


class JwtTest {
    private final String privateKey = "3344555";
    @Test
    void create() {
        String accessToken = Jwt.create(privateKey, "go2o", "", "1", Times.unix() + 3600);
        JwtDecoder jwt = Jwt.decode(privateKey, accessToken);
        System.out.println("---- jwt="+accessToken);
        Assertions.assertEquals(jwt.checkIssuer("go2o"),true);

        Assertions.assertEquals(jwt.getAud(),"1");
        Assertions.assertEquals(jwt.isExpires(Times.unix()),false);

        Map<String, Claim> claim = jwt.getClaim();
        accessToken = Jwt.createWithClaims(privateKey,claim,Times.unix()+3600);
         jwt = Jwt.decode(privateKey, accessToken);
        Assertions.assertEquals(jwt.isExpires(Times.unix()),false);
    }
}