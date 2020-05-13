package net.fze.commons.infrastructure;

import net.fze.util.DigestEncode;
import org.jetbrains.annotations.NotNull;

@Deprecated
public class Encoder {
    private static String offset = "@#$#@";

    /**
     * 获取SHA1算法偏移量
     *
     * @return 偏移量
     */
    public static String getSHA1Offset() {
        return offset;
    }

    /**
     * 设置SHA1算法偏移量
     *
     * @param o 偏移量
     */
    public static void setSHA1Offset(String o) {
        offset = o;
    }

    /**
     * 加密密码
     *
     * @param s 原密码
     * @return 加密后的密码
     */
    public String sha1Pwd(String s) {
        return DigestEncode.encode("sha1", s + offset);
    }

    /**
     * 检查密码强度
     *
     * @param pwd 密码
     * @return 错误
     */
    public Error checkPwdStrong(String pwd) {
        if (pwd.equals("123456") || pwd.equals("12345678") ||
                pwd.equals("888888") || pwd.equals("666666") ||
                pwd.equals("88888888")) {
            return new Error("密码过于简单");
        }
        if (pwd.length() < 6) {
            return new Error("密码长度必须大于6位");
        }
        return null;
    }

    /**
     * EJB 哈希算法
     *
     * @param str 字符串
     * @return hash
     */
    public int djbHash(@NotNull String str) {
        int hash = 5381;
        for (Byte b : str.getBytes()) {
            hash = ((hash << 5) + hash) + b;
        }
        return hash & 0x7FFFFFFF;
    }

}
