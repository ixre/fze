package net.fze.util;

import kotlin.text.Regex;
import kotlin.text.RegexOption;

import java.util.regex.Pattern;

public class RegexpTester {
    private static final Pattern numberRegex = Pattern.compile("^(\\d)+\\.*(\\d)*$");
    private static final Pattern userRegexp = Pattern.compile("^[a-zA-Z0-9_]{6,}$");
    private static final Pattern emailRegexp = Pattern.compile("^[A-Za-z0-9_\\-]+@[a-zA-Z0-9\\-]+(\\.[a-zA-Z0-9]+)+$");
    private static final Pattern phoneRegexp = Pattern.compile(
            "^(13[0-9]|14[5|6|7]|15[0-9]|16[5|6|7|8]|18[0-9]|17[0|1|2|3|4|5|6|7|8]|19[0|1|2|3|4|6|7|8|9])(\\d{8})$");
    private static final Pattern specCharRegexp = Pattern.compile("(.+)(\\|\\$|\\^|%|#|!|\\\\/)+(.+)");

    /**
     * 返回Kotlin正则表达式对象
     *
     * @param pattern
     * @return
     */
    public static Regex regexp(String pattern) {
        return new Regex(pattern);
    }

    public static Regex regexp(String pattern, RegexOption opt) {
        return new Regex(pattern, opt);
    }

    /**
     * 验证参数是否未填写
     *
     * @param o 参数
     * @return
     */
    public static Boolean testRequired(Object o) {
        return !(o != null || o.equals(""));
    }

    /**
     * 验证手机号码
     *
     * @param phone 手机
     * @return 是否匹配
     */
    public static Boolean isPhone(String phone) {
        return phoneRegexp.matcher(phone).find();
    }

    /**
     * 是否包含特殊字符
     *
     * @param str 字符串
     * @return 是否包含
     */
    public static Boolean containSpecChar(String str) {
        return specCharRegexp.matcher(str).find();
    }

    /**
     * 验证用户名格式是否正确
     *
     * @param user 用户名
     * @return 是否正确
     */
    public static Boolean testUserName(String user) {
        return userRegexp.matcher(user).find();
    }

    /**
     * 验证用邮箱格式是否正确
     *
     * @param email 邮箱地址
     * @return 是否正确
     */

    public static Boolean isEmail(String email) {
        return emailRegexp.matcher(email).find();
    }

    /**
     * 是否为数字
     */
    public static Boolean isNumber(String value) {
        if (Strings.isNullOrEmpty(value))
            return false;
        return numberRegex.matcher(value).find();
    }

    /**
     * 判断是否包含注入字符
     */
    public static Boolean checkSqlInject(String s) {
        Pattern regexp1 = Pattern.compile(
                "(.*)(\\b(and|exec|insert|select|drop|grant|alter|delete|update|chr|mid|master|truncate|char|declare|or)\\b|(\\*|;|\\+|'|%))(.*)",
                Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

        // (RegexOption.IGNORE_CASE, RegexOption.DOT_MATCHES_ALL)
        return regexp1.matcher(s).find();
    }
}
