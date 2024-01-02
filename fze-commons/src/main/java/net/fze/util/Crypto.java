package net.fze.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * 加密类
 */
public class Crypto {

    /**
     * 生成md5
     *
     * @return 32位MD5
     */
    public static String md5(String str) {
        byte[] input = str.getBytes(StandardCharsets.UTF_8);
        return md5(input);
    }

    /**
     * 生成md5
     *
     * @return 32位MD5
     */
    public static String md5(byte[] bytes) {
        String md5str;
        try {
            // 1 创建一个提供信息摘要算法的对象，初始化为md5算法对象
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 3 计算后获得字节数组,这就是那128位了
            byte[] buff = md.digest(bytes);
            // 4 把数组每一字节（一个字节占八位）换成16进制连成md5字符串
            md5str = bytesToHex(buff);
        } catch (Throwable e) {
            throw new RuntimeException(e.getMessage());
        }
        return md5str;
    }

    /**
     * 返回[str]的16位md5
     */

    public static String shortMd5(String str) {
        return md5(str).substring(8, 24);
    }

    /**
     * 二进制转十六进制
     *
     * @param bytes 字节数
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuilder md5str = new StringBuilder();
        // 把数组每一字节换成16进制连成md5字符串
        int digital = 0;
        for (byte aByte : bytes) {
            digital = aByte;
            if (digital < 0) {
                digital += 256;
            }
            if (digital < 16) {
                md5str.append("0");
            }
            md5str.append(Integer.toHexString(digital));
        }
        return md5str.toString().toUpperCase();
    }


}
