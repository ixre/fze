package net.fze.util;

import java.util.Base64;

/**
 * 编码工具
 */
public class EncodeUtils {
    public static byte[] encodeBase64(byte[] bytes) {
        return Base64.getEncoder().encode(bytes);
    }

    public static byte[] decodeBase64(byte[] bytes) {
        return Base64.getDecoder().decode(bytes);
    }

    public static String encodeBase64String(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static byte[] decodeBase64String(String s) {
        return Base64.getDecoder().decode(s);
    }
}
