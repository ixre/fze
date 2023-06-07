package net.fze.lib.injector;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InjectFactoryTest {

    @Test
    void configure() {
        InjectFactory.configure(new Injector() {
            @Override
            public <T> T getInstance(Class<T> c) {
                return null;
            }
        });
    }
}