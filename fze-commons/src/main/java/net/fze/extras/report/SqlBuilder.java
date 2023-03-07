package net.fze.extras.report;

import kotlin.text.Regex;
import net.fze.util.TypeConv;
import org.intellij.lang.annotations.JdkConstants;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlBuilder {

    public static String resolve(String origin, Map<String, Object> data) {
        Pattern regex = Pattern.compile("#if\\s*[\\{|\\(](.+?)[\\}\\)]\\s*\\n*([\\s\\S]+?)#end",
                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        Matcher matcher = regex.matcher(origin);
        Map<String, Object> finalData = data;
        Function<Matcher, String> replace = (Matcher match) -> {
            String p = match.group(1);
            String body = match.group(2);
            int i = body.indexOf("#else");
            if (checkIfCompare(data, p) || checkTrue(data,p)) {
                return i == -1 ? body : body.substring(0, i);
            } else if (i != -1) {
                return body.substring(i + 5);
            }
            return "";
        };
        while (matcher.find()) {
            origin = origin.replace(matcher.group(), replace.apply(matcher));
        }
        return origin;
    }

    private static boolean checkIfCompare(Map<String, Object> map, String p) {
        Pattern regex = Pattern.compile("([^\\s]+?)\\s*([><!=]*)\\s*([-\\d+])\\s*");
        Matcher matcher = regex.matcher(p);
        while (matcher.find()) {
            String key = matcher.group(1);  // 参数key
            if (null == key) {
                throw new IllegalArgumentException("参数" + key + "不存在于字典中");
            }
            int params = Integer.parseInt(String.valueOf(map.get(key))); //获取前端传过来的值
            String e = matcher.group(2);    // 表达式
            int value = Integer.parseInt(matcher.group(3)); //需要验证的值
            switch (e) {
                case ">":
                    return params > value;
                case ">=":
                    return params >= value;
                case "<":
                    return params < value;
                case "<=":
                    return params <= value;
                case "<>":
                case "!=":
                    return params != value;
            }
            return false;
        }
        return false;
    }



    private static boolean checkTrue(Map<String, Object> data,String p) {
        if (data.containsKey(p)) {
            Object v = data.get(p);
            if (v == null)
                return false;
            if (v.equals(""))
                return false;
            if (TypeConv.toBoolean(v))
                return true;
            if (v.equals("True"))
                return true;
            if (v.equals("1"))
                return true;
            if (v.equals("0.0"))
                return false;
            try {
                return TypeConv.toInt(v) != 0;
            } catch (Throwable ignored) {
            }
            String s = v.toString();
            return !(s.equals("false") || s.equals("False") || s.equals("0"));
        } else {
            throw new IllegalArgumentException("参数" + p + "不存在于字典中");
        }
    }
}
