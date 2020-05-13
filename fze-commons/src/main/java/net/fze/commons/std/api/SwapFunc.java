package net.fze.commons.std.api;

// 交换信息，根据key返回用户编号、密钥
public interface SwapFunc {
    void swap();

    int getUserId();

    String getSecret();
}
