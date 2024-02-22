package net.fze.ext.report;

import net.fze.util.TypeConv;

import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jarrysix
 */
public class SqlBuilder {

    public static String resolve(String origin, Map<String, Object> data) {
        // 兼容#end结尾
        origin = origin.replace("#end", "#fi");

//        Pattern regex = Pattern.compile("#if\\s*[\\{|\\(](.+?)[\\}\\)]\\s*\\n*([\\s\\S]+?)#fi",
//                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

        Pattern regex = Pattern.compile("#if\\s*[{|(](.+?)[})]\\s*\\n*([\\s\\S]+?)#fi",
                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        Matcher matcher = regex.matcher(origin);
        Function<Matcher, String> replace = (Matcher match) -> {
            //String block = match.group(0);
            String key = match.group(1);
            String body = match.group(2);
            if (data.containsKey(key)) {
                Boolean b = checkTrueValue(data.get(key));
                return getSqlBlock(b, body);
            }
            Boolean b = checkIfCompare(data, key);
            return getSqlBlock(b, body);
        };
        while (matcher.find()) {
            origin = origin.replace(matcher.group(), replace.apply(matcher));
        }
        return origin;
    }

    /**
     * 判断传入的值是否为true
     *
     * @param v 值
     * @return 是否符合条件
     */
    private static Boolean checkTrueValue(Object v) {
        if (v == null) {
            return false;
        }
        if ("".equals(v)) {
            return false;
        }
        if (TypeConv.toBoolean(v)) {
            return true;
        }
        if ("True".equals(v)) {
            return true;
        }
        if ("1".equals(v)) {
            return true;
        }
        if ("0.0".equals(v)) {
            return false;
        }
        try {
            return TypeConv.toInt(v) != 0;
        } catch (Throwable ignored) {
        }
        String s = v.toString();
        return !("false".equals(s) || "False".equals(s) || "0".equals(s));
    }

    /**
     * 获取SQL块的内容
     *
     * @param b 条件
     * @param body 代码块
     * @return SQL语句
     */
    private static String getSqlBlock(Boolean b, String body) {
        int i = body.indexOf("#else");
        String tf = body;
        String ff = "";
        if (i != -1) {
            tf = body.substring(0, i);
            ff = body.substring(i + 5);
        }
        return b ? tf : ff;
    }

    /**
     * 检查是否符合条件判断
     *
     * @param map 参数
     * @param p 表达式
     * @return 是否符合条件
     */
    private static boolean checkIfCompare(Map<String, Object> map, String p) {
        Pattern regex = Pattern.compile("(\\S+)\\s*([><!=]*)\\s*(\\S+)\\s*");
        Matcher matcher = regex.matcher(p);
        while (matcher.find()) {
            // 参数key
            String key = matcher.group(1);
            if (map.containsKey(key)) {
                // 表达式
                String op = matcher.group(2);
                if ((op.contains("<") || op.contains(">")) && !"<>".equals(op)) {
                    return checkIntCompare(op, map.get(key), matcher.group(3));
                }
                String v1 = TypeConv.toString(map.get(key));
                String v2 = matcher.group(3);
                switch (op) {
                    case "=":
                    case "==":
                        return v1.equals(v2);
                    case "<>":
                    case "!=":
                        return !v1.equals(v2);
                }
            }
        }
        return false;
    }


    private static boolean checkIntCompare(String op, Object o, String dst) {
        //获取前端传过来的值
        int params = TypeConv.toInt(o);
        //需要验证的值
        int value = TypeConv.toInt(dst);
        switch (op) {
            case ">":
                return params > value;
            case ">=":
                return params >= value;
            case "<":
                return params < value;
            case "<=":
                return params <= value;
        }
        return false;
    }
}