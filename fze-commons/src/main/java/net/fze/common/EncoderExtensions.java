package net.fze.common;

import net.fze.util.DigestEncode;

public class EncoderExtensions {

    EncoderExtensions() {
    }

    /**
     * 加密密码
     *
     * @param s 原密码
     * @return 加密后的密码
     */
    public String sha1Pwd(String s, String salt) {
        return DigestEncode.encode("sha1", s + salt);
    }

    public String md5(byte[] bytes) {
        return DigestEncode.encode("md5", bytes);
    }

    public String md5L16(byte[] bytes) {
        String s = this.md5(bytes);
        if (!s.equals("")) {
            return s.substring(8, 24);
        }
        return s;
    }

    public String sha1(byte[] bytes) {
        return DigestEncode.encode("sha1", bytes);
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
    public int djbHash(String str) {
        int hash = 5381;
        for (Byte b : str.getBytes()) {
            hash = ((hash << 5) + hash) + b;
        }
        return hash & 0x7FFFFFFF;
    }

}
