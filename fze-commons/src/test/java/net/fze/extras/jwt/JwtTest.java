package net.fze.extras.jwt;

import net.fze.util.Times;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class JwtTest {
    private String privateKey = "3344555";
    @Test
    void create() {
        String accessToken = Jwt.create(privateKey, "go2o", "", "1", Times.unix() + 3600);
        JwtDecoder jwt = Jwt.decode(privateKey, accessToken);
        System.out.println("---- jwt="+accessToken);
        Assertions.assertEquals(jwt.checkIssuer("go2o"),true);

        Assertions.assertEquals(jwt.getAud(),"1");
        Assertions.assertEquals(jwt.isExpires(Times.unix()),false);
    }
}