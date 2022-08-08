package net.fze.util;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * IO工具类
 */
public class IoUtils {
    @NotNull
    public static byte[] streamToByteArray(InputStream stream) throws IOException {
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        int l;
        byte[] buff = new byte[100];
        while ((l = stream.read(buff, 0, 100)) > 0) {
            bs.write(buff, 0, l);
        }
        byte[] in2b = bs.toByteArray();
        bs.close();
        return in2b;
    }

    /**
     * 读取输入流
     *
     * @param stream 流
     * @return 字符
     */
    public static String readToEnd(InputStream stream, String charset) throws IOException {
        if (charset == null || charset.equals("")) {
            charset = "UTF-8";
        }
        return new String(streamToByteArray(stream), charset);
    }
}
