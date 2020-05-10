package net.fze.arch.commons.libs.mq;

import net.fze.arch.commons.std.LangExtension;
import net.fze.arch.commons.std.TypesConv;
import org.junit.jupiter.api.Test;


public class MqProducerTest {
    @Test
    public void testSingleton() throws Exception {
//        System.out.println(
//                "----------------------"
//                        + MqProducer.class.getResource("/com/google/common/collect/Sets.class"));
//        MqProducer.configure("dbs.dev.meizhuli.net", 4150);
//        MqProducer.singleton().produce("test", new LangExtension().uuid().getBytes());
    }

    @Test
    public void testStringToInt() {
        int i = TypesConv.toInt(1.0D);
        System.out.println(i);
    }
}
