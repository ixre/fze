package net.fze.mock.example;

import net.fze.mock.MockCase;

public class Main {
    // 运行模式服务
    public static void main(String[] args) throws Exception {
        MockCase mock = ExampleMockService.singleton();
        StatusService s = mock.getInstance(StatusService.class);
        String result = s.Hello("jarrysix");
        System.out.println(result);
    }
}
