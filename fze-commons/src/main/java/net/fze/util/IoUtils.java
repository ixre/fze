package net.fze.util;

import java.io.*;
import java.nio.file.Files;

/**
 * IO工具类
 */
public class IoUtils {

    /**
     * 读取流中的所有字节
     * @param stream 流
     */
    public static byte[] readAllBytes(InputStream stream) throws IOException {
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
        if (charset == null || charset.isEmpty()) {
            charset = "UTF-8";
        }
        return new String(readAllBytes(stream), charset);
    }

    public static InputStream getResourceAsStream(String path) {
        return IoUtils.class.getClassLoader().getResourceAsStream(path);
    }

    public static InputStream getFileStream(String path) throws IOException {
        File file = new File(path);
        if(!file.exists()){
            throw new RuntimeException("文件不存在");
        }
        return Files.newInputStream(file.toPath());
    }
}
