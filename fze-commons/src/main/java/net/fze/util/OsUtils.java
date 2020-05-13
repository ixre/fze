package net.fze.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Arrays;

/**
 * IO工具类
 */
public class OsUtils {
    private static final Log log = LogFactory.getLog(OsUtils.class.getCanonicalName());

    /**
     * 读取输入流
     *
     * @param stream 流
     * @return 字符
     */
    private static String readStream(InputStream stream) throws IOException {
        StringBuilder sb = new StringBuilder();
        byte[] offset = new byte[1024];
        int size;
        while ((size = stream.read(offset)) != -1) {
            sb.append(Arrays.toString(Arrays.copyOfRange(offset, 0, size)));
        }
        return sb.toString();
    }

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
    public static String readStream(InputStream stream, String charset) throws IOException {
        if (charset == null || charset.equals("")) {
            charset = "UTF8";
        }
        Reader read = new InputStreamReader(stream, charset);
        StringBuilder sb = new StringBuilder();
        char[] offset = new char[1024];
        int size;
        while ((size = read.read(offset)) != -1) {
            sb.append(Arrays.copyOfRange(offset, 0, size));
        }
        return sb.toString();
    }

    /**
     * 运行终端命令
     *
     * @param cmd 命令
     * @return 结果
     */
    public static String[] exec(String[] cmd) throws Exception {
        InputStream errorStream = null;
        InputStream inputStream = null;
        Process ps = null;
        try {
            ps = Runtime.getRuntime().exec(cmd);
            log.debug("开始执行本地线程");
            errorStream = ps.getErrorStream(); // 正确结果的流
            inputStream = ps.getInputStream(); // 错误结果的流
            ps.waitFor();
            String[] result = new String[2];
            result[0] = String.valueOf(ps.exitValue());
            if (ps.exitValue() == 0) {
                result[1] = readStream(inputStream);
            } else {
                result[1] = readStream(errorStream);
            }
            return result;
        } catch (Exception e) {
            log.error("execute linux command error...", e);
            throw new IOException("execute linux command error...");
        } finally {
            if (errorStream != null) {
                errorStream.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }

            if (ps != null) {
                ps.destroy();
            }
        }
    }
}
