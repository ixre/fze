package net.fze.util;

import org.junit.jupiter.api.Test;

class OsUtilsTest {

    @Test
    void getExternalIp() {
        String ip = OsUtils.getExternalIp();
        System.out.println("ip="+ip);
         ip = OsUtils.getExternalIp();
        System.out.println("ip2="+ip);
    }
}