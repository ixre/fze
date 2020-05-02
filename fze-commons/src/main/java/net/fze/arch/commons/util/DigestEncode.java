package net.fze.arch.commons.util;

import java.security.MessageDigest;

public class DigestEncode {
    private static final char[] HEX_DIGITS = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };

    /**
     * encode string
     *
     * <p>return DigestEncode.encode("sha1", data); }
     *
     * <p>return DigestEncode.encode("md5",data);
     *
     * @param algorithm 算法
     * @param bytes     字节
     * @return String 加密后结果
     */
    public static String encode(String algorithm, byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return "";
        }
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            digest.update(bytes);
            return getHexText(digest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String encode(String algorithm, String s) {
        return encode(algorithm, s.getBytes());
    }

    /**
     * Takes the raw bytes from the digest and formats them correct.
     *
     * @param bytes the raw bytes from the digest.
     * @return the formatted bytes.
     */
    private static String getHexText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        // 把密文转换成十六进制的字符串形式
        for (byte aByte : bytes) {
            buf.append(HEX_DIGITS[(aByte >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[aByte & 0x0f]);
        }
        return buf.toString();
    }
}
