package net.fze.lib.injector;

import net.fze.ext.injector.InjectFactory;
import net.fze.ext.injector.Injector;
import org.junit.jupiter.api.Test;

class InjectFactoryTest {

    @Test
    void configure() {

        //InjectFactory.configure(ctx::getBean);  // SpringBoot
        InjectFactory.configure(new Injector() {
            @Override
            public <T> T getInstance(Class<T> c) {
                return null;
            }
        });
    }
}