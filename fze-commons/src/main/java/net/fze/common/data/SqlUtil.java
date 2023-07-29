package net.fze.common.data;

import java.util.regex.Pattern;

/**
 * SQL工具类
 */
public class SqlUtil {
    private static final Pattern injectRegexp = Pattern.compile(
            "\\bEXEC\\b|UNION.+?SELECT|UPDATE.+?SET|INSERT\\s+INTO.+?VALUES|DELETE.+?FROM|(CREATE|ALTER|DROP|TRUNCATE)\\s+(TABLE|DATABASE)");


    /**
     * 判断是否存在危险的注入操作
     */
    public static boolean checkInject(String str) {
        return !injectRegexp.matcher(str).find();
    }
}
