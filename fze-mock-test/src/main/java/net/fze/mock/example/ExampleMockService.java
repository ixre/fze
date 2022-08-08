package net.fze.mock.example;


import com.google.inject.Injector;
import com.google.inject.Module;
import net.fze.mock.MockCase;

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
