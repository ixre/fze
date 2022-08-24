package net.fze.extras.report;

import kotlin.text.Regex;
import org.intellij.lang.annotations.JdkConstants;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlBuilder {

    public static String resolve(String origin, Map<String, Object> data)
    {
        Pattern regex = Pattern.compile( "#if\\s+\\{([^\\}]+)\\}\\s*([\\S\\s]+?)\\s*#endif",
                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        Matcher matcher = regex.matcher(origin);
        Map<String, Object> finalData = data;
        Function<Matcher,String> replace = (Matcher match)->{
            String p = match.group(1);
            String body = match.group(2);
            int i = body.indexOf("#else");
            if (finalData.containsKey(p))
            {
                Object v = finalData.get(p);
                if (!(v == null || v.equals(false) || v.equals(0) || v.equals("")))
                {
                    return i == -1 ? body : body.substring(0, i);
                }
                else if (i != -1)
                {
                    return body.substring(i + 5);
                }
            }
            else
            {
                throw new IllegalArgumentException("参数"+p + "不存在于字典中");
            }
            return "";
        };
        while(matcher.find()) {
            origin = origin.replace(matcher.group(),replace.apply(matcher));
        }
        return origin;
    }
}
