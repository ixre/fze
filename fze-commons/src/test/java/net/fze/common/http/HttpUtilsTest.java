package net.fze.common.http;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HttpUtilsTest {
    @Test
    void getBaseURL() {
        // HttpServletRequest req = new ServletContext()
        // HttpUtils.getBaseURL()
    }

    @Test
    void join() {
        String s = HttpUtils.join("http://fze.net","info.html","size=100");
        Assertions.assertEquals(s,"http://fze.net/info.html?size=100");
        String s2 = HttpUtils.join("http://fze.net","info.html?r=1","size=100");
        Assertions.assertEquals(s2,"http://fze.net/info.html?r=1&size=100");
        String s3 = HttpUtils.join("http://fze.net/info.html?r=1","","size=100");
        Assertions.assertEquals(s3,"http://fze.net/info.html?r=1&size=100");
    }
}