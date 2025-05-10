package net.fze.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 字符串扩展对象
 */
public class Types {
    private static final Gson gson = new GsonBuilder()
            .serializeNulls()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .create();
    private static final Logger log = LoggerFactory.getLogger(Types.class);

    /**
     * 获取值，如果v为空，返回默认值d
     *
     * @param v 值
     * @param d 默认值
     * @return 不为空的值
     */
    public static <T> T orValue(T v, T d) {
        return v == null || v.equals("") ? d : v;
    }

    /**
     * 获取值，如果v为空，返回默认值d,使用orValue代替
     *
     * @param v 值
     * @param d 默认值
     * @return 不为空的值
     */
    @Deprecated()
    public static <T> T valueOrDefault(T v, T d) {
        return v == null ? d : v;
    }

    private static Gson getGson() {
        // return new Gson();
        return gson;
    }

    public static String toJson(Object obj) {
        if (obj instanceof Integer || obj instanceof Long || obj instanceof Float ||
                obj instanceof Double || obj instanceof Boolean || obj instanceof String) {
            return String.valueOf(obj);
        }
        return getGson().toJson(obj);
    }

    /**
     * 反序列化
     *
     * @return Object
     */
    public static <T> T fromJson(String json, Class<T> c) {
        return getGson().fromJson(json, c);
    }

    /**
     * 反序列化(泛型)
     */
    public static <T> T fromTypeJson(String json, Class<?> typeOfT, Class<?>... typeArgs) {
        Type gt;
        if (typeArgs.length == 0) {
            gt = TypeToken.get(typeOfT).getType();
        } else {
            gt = TypeToken.getParameterized(typeOfT, typeArgs).getType();
        }
        return getGson().fromJson(json, gt);
    }

    /**
     * 通过类型反序列化JSON
     *
     * @param json json字符串
     * @param gt   类型
     */
    public static <T> T fromJson(String json, Type gt) {
        return new Gson().fromJson(json, gt);
    }

    /**
     * 使用反射拷贝对象
     * @param raw 原对象
     * @param target 目标对象
     * @param <T> 类型
     */
    public static <T> void copyObject(T raw,T target) {
        ReflectUtils.copyObject(raw, target);
    }

    /**
     * 拷贝属性(使用BeanUtils)
     * @param dst 目标对象
     * @param src 源对象
     */
    public static void copyProperties(Object dst, Object src) {
        try {
            BeanUtils.copyProperties(dst, src);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }


    // **
    // * 使用Dozer将对象隐式转换
    // *
    // *
    // public static <T> T mapObject(Object src, Class<T> classes) {
    // Mapper mapper = DozerBeanMapperBuilder.buildDefault();
    // return mapper.map(src, classes);
    // }

    /**
     * 克隆一个字典
     */
    public static <T, K> Map<T, K> cloneMap(Map<T, K> src) {
        return new HashMap<>(src);
    }

    /**
     * 拷贝字典数据
     */
    public static <T, K> void copyMap(Map<T, K> src, Map<T, K> dst) {
        dst.putAll(src);
    }


    /**
     * 将对象转换为字典
     * @param obj 对象
     * @return 字典
     */
    public static Map<String, Object> objectToMap(Object obj,String ...ignoreFields) {
        return Arrays.stream(obj.getClass().getDeclaredFields())
                .peek(field -> field.setAccessible(true))
                .filter(field -> !Arrays.asList(ignoreFields).contains(field.getName()))
                .collect(Collectors.toMap(Field::getName, field -> {
                    try {
                        return field.get(obj);
                    } catch (IllegalAccessException e) {
                        log.error("objectToMap error", e);
                        return null;
                    }
                }));
    }
    /**
     * 判断ra,rb是否在la,lb区间内
     */
    public static Boolean inRange(int la, int lb, int ra, int rb) {
        return (la >= ra && la < rb) ||
                (la > ra && la <= rb) ||
                (la <= ra && lb > rb) ||
                (la < ra && lb >= ra);
    }

    /**
     * 将json数组反序列化为列表
     */
    public <T> List<T> fromArrayJson(String json, Class<T> typeOfT) {
        Type gt = TypeToken.getParameterized(List.class, new Type[]{typeOfT}).getType();
        return new Gson().fromJson(json, gt);
    }

    public interface TProp<T, K> {
        K get(T t);
    }

    public interface TCond<T> {
        boolean test(T t);
    }
    /*
     * public static <T> void eachArray(Iterator<T> e, TFunc<T> f) {
     * while (e.hasNext()) {
     * f.call(e.next());
     * }
     * }
     */

    public interface TFunc<T> {
        void call(T t);
    }


}
