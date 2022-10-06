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
        Pattern regex = Pattern.compile("#if\\s+\\{([^\\}]+)\\}\\s*([\\S\\s]+?)\\s*#end",
                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        Matcher matcher = regex.matcher(origin);
        Map<String, Object> finalData = data;
        Function<Matcher, String> replace = (Matcher match) -> {
            String p = match.group(1);
            String body = match.group(2);
            int i = body.indexOf("#else");
            if (finalData.containsKey(p)) {
                Object v = finalData.get(p);
                // 不为空,为true或者字符长度超过0均为true
                if (checkTrue(v)) {
                    return i == -1 ? body : body.substring(0, i);
                } else if (i != -1) {
                    return body.substring(i + 5);
                }
            } else {
                throw new IllegalArgumentException("参数" + p + "不存在于字典中");
            }
            return "";
        };
        while (matcher.find()) {
            origin = origin.replace(matcher.group(), replace.apply(matcher));
        }
        return origin;
    }

    private static boolean checkTrue(Object v) {
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
        } catch (Throwable ex) {
        }
        String s = v.toString();
        return !(s.equals("false") || s.equals("False") || s.equals("0"));
        // v == null || v.equals("") ||
        // !(TypeConv.toBoolean(v)||
        // TypeConv.toString(v).length() > 0)
    }
}
