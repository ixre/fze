package net.fze.util;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符工具类
 */
public class Strings {

    private static final String letterStr = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    /**
     * 是否为空字符串或空
     *
     * @param s 字符串
     */
    public static boolean isNullOrEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    /**
     * 字符模板
     *
     * @param text 文本
     * @param args 参数
     */

    public static String template(String text, Map<String, String> args) {
        return replace(text, "\\{([^{]+?)}",
                matcher -> Types.orValue(args.get(matcher.group(1)), matcher.group()));
    }

    /**
     * 将数组拼接为字符串
     *
     * @param arr 数组
     * @return 以","分割的字符串
     */
    public static <T> String join(Iterable<T> arr, CharSequence delimiter) {
        List<String> dst = new ArrayList<>();
        arr.forEach(a -> dst.add(a.toString()));
        return String.join(delimiter, dst);
    }

    // 返回随机字符串,[n]:长度
    public static String randomLetters(int n) {
        return randomLetters(n, letterStr);
    }

    /**
     * 返回随机字符串,[n]:长度,letters字段
     */
    public static String randomLetters(int n, String letters) {
        int l = letters.length();
        char[] arr = new char[n];
        Random rd = new Random();
        for (int i = 0; i < n; i++) {
            arr[i] = letters.charAt(rd.nextInt(l) % l);
        }
        return new String(arr);
    }

    // 获取字符串位置
    public static int endPosition(String s, int b, int n) {
        if (n == -1) {
            return s.length();
        }
        return b + n;
    }

    // 替换顺序b后的n个字符, 如果n为-1, 默认替换到结尾
    public static String replaceRange(String s, int b, int n, String replace) {
        int end = endPosition(s, b, n);
        return s.replace(s.substring(b, end), replace);
        // return s.replaceRange(b, end, replace);
    }

    // 替换顺序b后的n个字符, 如果n为-1, 默认替换到结尾
    public static String replaceN(String s, int b, int n, String replace) {
        int end = endPosition(s, b, n);
        return s.replace(s.substring(b, end), repeat(replace, end - b));
        // return s.replaceRange(b, end, repeat(replace, end - b));
    }

    // 重复字符串
    public static String repeat(String s, int n) {
        String[] arr = new String[n];
        Arrays.fill(arr, s);
        return String.join("", arr);
    }

    public static String replace(String s, String pattern, Function<Matcher, String> re) {
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(s);
        while (matcher.find()) {
            s = s.replace(s, re.apply(matcher));
        }
        return s;
    }

    public static int indexOfAny(String workspace, Iterable<String> arr) {
        for (String s : arr) {
            int i = workspace.indexOf(s);
            if (i != -1)
                return i;
        }
        return -1;
    }
}
