package net.fze.common.libs.mq;

import net.fze.common.std.LangExtension;
import net.fze.common.std.TypesConv;
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
