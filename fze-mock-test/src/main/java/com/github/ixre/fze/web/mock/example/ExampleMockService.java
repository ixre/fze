package com.github.ixre.fze.web.mock.example;


import com.github.ixre.fze.web.mock.MockCase;
import com.google.inject.Injector;
import com.google.inject.Module;

public class ExampleMockService extends MockCase {
    private static ExampleMockService example;

    static {
        example = new ExampleMockService();
    }

    private ExampleMockService() {
        super("conf");
    }

    public static MockCase singleton() {
        return example;
    }


    public Module module() {
        return new ExampleModule();
    }


    public void configure(String conPath, Injector ioc) {
    }
}
