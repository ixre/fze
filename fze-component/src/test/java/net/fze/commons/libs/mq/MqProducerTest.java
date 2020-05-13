package net.fze.commons.libs.mq;

import net.fze.commons.std.LangExtension;
import net.fze.commons.std.TypesConv;
//import net.fze.arch.component.nsq.MqProducer;
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
