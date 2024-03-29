package net.fze.util;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 数据类型转换工具类
 */
public class TypeConv {
    /**
     * 将obj转换为string，如果obj为null则返回defaultVal
     *
     * @param obj        需要转换为string的对象
     * @param defaultVal 默认值
     * @return obj转换为string
     */
    public static String toString(Object obj, String defaultVal) {
        if (obj == null)
            return defaultVal;
        if (obj.equals("") && defaultVal.equals("0"))
            return "0";
        return obj.toString();
    }

    /**
     * 将obj转换为string，默认为空
     *
     * @param obj 需要转换为string的对象
     * @return 将对象转换为string的字符串
     */
    public static String toString(Object obj) {
        return toString(obj, "");
    }

    /**
     * 将对象转换为int
     *
     * @param obj        需要转换为int的对象
     * @param defaultVal 默认值
     * @return obj转换成的int值
     */
    public static Integer toInteger(Object obj, Integer defaultVal) {
        if (obj == null) {
            return defaultVal;
        }
        if (obj instanceof Double) {
            return ((Double) obj).intValue();
        }
        if (obj instanceof Float) {
            return ((Float) obj).intValue();
        }
        if (obj instanceof Long) {
            return ((Long) obj).intValue();
        }
        if (obj instanceof Integer) {
            return (Integer) obj;
        }
        return Integer.parseInt(toString(obj, "0"));
    }

    /**
     * 将对象转换为int
     *
     * @param obj 需要转换为int的对象
     * @return obj转换成的int值
     */
    public static int toInt(Object obj) {
        return toInteger(obj, 0);
    }

    /**
     * 将对象转换为int
     *
     * @param obj 需要转换为int的对象
     * @return obj转换成的int值
     */
    public static Integer toInteger(Object obj) {
        return toInteger(obj, 0);
    }

    /**
     * 将对象转换为int
     *
     * @param obj        需要转换为int的对象
     * @param defaultVal 默认值
     * @return obj转换成的int值
     */
    public static Float toFloat(Object obj, float defaultVal) {
        if (obj == null) {
            return defaultVal;
        }
        if (obj instanceof Double) {
            return ((Double) obj).floatValue();
        }
        if (obj instanceof Float) {
            return (Float) obj;
        }
        if (obj instanceof Long) {
            return ((Long) obj).floatValue();
        }
        if (obj instanceof Integer) {
            return ((Integer) obj).floatValue();
        }
        return Float.parseFloat(toString(obj, "0"));
    }

    /**
     * 将对象转换为Float
     *
     * @param obj 需要转换为Float的对象
     * @return obj转换成的Float值
     */
    public static Float toFloat(Object obj) {
        return toFloat(obj, 0);
    }

    /**
     * 将obj转换为long
     *
     * @param obj        需要转换的对象
     * @param defaultVal 默认值
     * @return 如果obj为空则返回默认，不为空则返回转换后的long结果
     */
    public static Long toLong(Object obj, long defaultVal) {
        if (obj instanceof Double) {
            return ((Double) obj).longValue();
        }
        if (obj instanceof Float) {
            return ((Float) obj).longValue();
        }
        if (obj instanceof Long) {

            return (Long) obj;
        }
        if (obj instanceof Integer) {
            return ((Integer) obj).longValue();
        }
        return (obj != null) ? Long.parseLong(toString(obj)) : defaultVal;
    }

    /**
     * 将obj转换为long
     *
     * @param obj 需要转换的对象
     * @return 如果obj为空则返回默认的0l，不为空则返回转换后的long结果
     */
    public static Long toLong(Object obj) {
        return toLong(obj, 0l);
    }

    /**
     * 将object转换为double类型，如果出错则返回 defaultVal
     *
     * @param obj        需要转换的对象
     * @param defaultVal 默认值
     * @return 转换后的结果
     */
    public static Double toDouble(Object obj, Double defaultVal) {
        try {
            return Double.parseDouble(obj.toString());
        } catch (Exception e) {
            return defaultVal;
        }
    }

    /**
     * 将object转换为double类型，如果出错则返回 0d
     *
     * @param obj 需要转换的对象
     * @return 转换后的结果
     */
    public static double toDouble(Object obj) {
        return toDouble(obj, 0d);
    }

    /**
     * 将object转换为Boolean类型
     *
     * @param obj 需要转换的对象
     * @return 转换后的结果
     */
    public static Boolean toBoolean(Object obj) {
        if (obj == null)
            return false;
        if (obj instanceof Boolean)
            return (Boolean) obj;
        return Boolean.parseBoolean(obj.toString());
    }

    /**
     * 将object转换为Date类型，如果出错则返回当前时间
     *
     * @param obj 需要转换的对象
     * @return 转换后的结果
     */
    public static Date toDateTime(Object obj) {
        if (obj instanceof Date)
            return (Date) obj;
        if (obj instanceof Long)
            return Times.unixTime((Long) obj, 0);
        if (obj instanceof Integer)
            return Times.unixTime((Integer) obj, 0);
        return Times.time(obj.toString(), Times.DefaultDateFormat.toPattern());
    }

    /**
     * 将object转换为BigDecimal类型
     *
     * @param obj 需要转换的对象
     * @return 转换后的结果
     */
    public static BigDecimal toBigDecimal(Object obj) {
        if (obj instanceof BigDecimal)
            return (BigDecimal) obj;
        if (obj instanceof Double)
            return BigDecimal.valueOf((Double) obj);
        if (obj instanceof Integer)
            return BigDecimal.valueOf((Integer) obj);
        return BigDecimal.valueOf(toDouble(obj));
    }


    /**
     * 数组转为List
     *
     * @param arr 数组
     * @param <T> 泛型
     * @return 列表
     */
    @SafeVarargs
    public static <T> List<T> arrAsList(T... arr) {
        return Arrays.asList(arr);
    }

}
