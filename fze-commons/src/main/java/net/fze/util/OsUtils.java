package net.fze.util;



import net.fze.common.http.HttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OsUtils {
    /**
     * 获取 IP 地址的服务列表
     */
    private static final String[] IPV4_SERVICES = {
            "http://checkip.amazonaws.com/",
            "https://myip.ipip.net",
            "https://ipv4.icanhazip.com/",
    };
    /**
     * IP 地址校验的正则表达式
     */
    private static final Pattern IPV4_PATTERN = Pattern.compile(
            "((\\d+).(\\d+).(\\d+).(\\d+))");
    private static String _cacheExternalIp = null;

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

    /**
     * 读取输入流
     *
     * @param stream 流
     * @return 字符
     */
    public static String readStream(InputStream stream) throws IOException {
        StringBuilder sb = new StringBuilder();
        byte[] offset = new byte[1024];
        int size;
        while ((size = stream.read(offset)) != -1) {
            sb.append(Arrays.toString(Arrays.copyOfRange(offset, 0, size)));
        }
        return sb.toString();
    }

    /**
     * 对服务器进行ping操作
     *
     * @param server
     * @return
     */
    public static Boolean ping(String server) {
        try {
            return InetAddress.getByName(server).isReachable(10);
        } catch (Exception ignored) {
        }
        return false;
    }

    /**
     * 检测端口是否开放
     *
     * @param host 主机
     * @param port 端口
     * @return 是否开放
     */
    public static Boolean detectPort(String host, int port) {
        try {
            new Socket(host, port);
            return true;
        } catch (Exception ignored) {
        }
        return false;
    }

    public static String getExternalIp() {
        if (!Strings.isNullOrEmpty(_cacheExternalIp)) {
            return _cacheExternalIp;
        }
        List<Callable<String>> callables = new ArrayList<>();
        for (String ipService : IPV4_SERVICES) {
            callables.add(() -> get(ipService));
        }
        // 线程池的 ExecutorService.invokeAny(callables) 方法用于并发执行多个线程，
        // 并拿到最快的执行成功的线程的返回值，只要有一个执行成功，其他失败的任务都会忽略
        ExecutorService executorService = Executors.newCachedThreadPool();
        try {
            // 返回第一个成功获取的 IP
            _cacheExternalIp = executorService.invokeAny(callables);
            return _cacheExternalIp;
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
        return null;
    }

    private static String get(String url) throws IOException {
        String content = new String(HttpClient.get(url, 10000));
        Matcher matcher = IPV4_PATTERN.matcher(content);
        if (matcher.find()) {
            return matcher.group(1);
        }
        throw new IOException("invalid IPv4 address: " + content);
    }

    public static byte[] readAllBytes(InputStream inputStream) throws IOException {
        final int bufLen = 4 * 0x400; // 4KB
        byte[] buf = new byte[bufLen];
        int readLen;
        IOException exception = null;

        try {
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                while ((readLen = inputStream.read(buf, 0, bufLen)) != -1)
                    outputStream.write(buf, 0, readLen);

                return outputStream.toByteArray();
            }
        } catch (IOException e) {
            exception = e;
            throw e;
        } finally {
            if (exception == null) inputStream.close();
            else try {
                inputStream.close();
            } catch (IOException e) {
                exception.addSuppressed(e);
            }
        }
    }
}
