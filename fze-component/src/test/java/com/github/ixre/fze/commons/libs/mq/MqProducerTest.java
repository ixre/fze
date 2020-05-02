package com.github.ixre.fze.commons.libs.mq;

import com.github.ixre.fze.commons.std.LangExtension;
import com.github.ixre.fze.commons.std.TypesConv;
import com.github.ixre.fze.commons.nsq.MqProducer;
import org.junit.jupiter.api.Test;


public class MqProducerTest {
    @Test
    public void testSingleton() throws Exception {
        System.out.println(
                "----------------------"
                        + MqProducer.class.getResource("/com/google/common/collect/Sets.class"));
        MqProducer.configure("dbs.dev.meizhuli.net", 4150);
        MqProducer.singleton().produce("test", new LangExtension().uuid().getBytes());
    }

    @Test
    public void testStringToInt() {
        int i = TypesConv.toInt(1.0D);
        System.out.println(i);
    }
}
