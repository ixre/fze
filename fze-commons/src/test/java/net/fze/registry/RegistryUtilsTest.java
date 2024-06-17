package net.fze.registry;

import net.fze.util.Types;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author jarrysix
 */
class RegistryUtilsTest {
    static class RegistryConfig {
        @RegistryKey(value = "app_enable_nats_subscription")
        public String natsSubscription;
    }

    @Test
    void setAttribute() {
        RegistryConfig testClass = new RegistryConfig();
        // 获取注册表中的key
        List<String> registryKeys = RegistryUtils.getRegistryKeys(testClass);
        System.out.println(Types.toJson(registryKeys));
        // 设置注册表中的key
        RegistryUtils.setAttribute(testClass, "app_enable_nats_subscription", "newValue");
        assertEquals(testClass.natsSubscription,"newValue");
    }
}