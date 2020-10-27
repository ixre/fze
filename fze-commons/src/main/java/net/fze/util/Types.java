package net.fze.util;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.fze.commons.TimeExtensions;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字符串扩展对象
 */
public class Types {
    /**
     * 时间扩展,使用TypedStd.time代替
     */
    public static final TimeExtensions time = new TimeExtensions();

    /**
     * 是否为空字符串或空
     *
     * @param s 字符串
     * @return
     */
    public static boolean emptyOrNull(String s) {
        return s == null || s.trim().equals("");
    }

    /**
     * 获取值，如果v为空，返回默认值d
     *
     * @param v 值
     * @param d 默认值
     * @return 不为空的值
     */
    public static <T> T valueOrDefault(T v, T d) {
        return v == null ? d : v;
    }


    @NotNull
    public static String toJson(@NotNull Object obj) {
        return new Gson().toJson(obj);
    }


    /**
     * 反序列化
     *
     * @return Object
     */
    public static <T> T fromJson(String json, Class<T> c) {
        return new Gson().fromJson(json, c);
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
        return new Gson().fromJson(json, gt);
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
     * 使用Dozer将对象隐式转换
     *
     * @param src     数据对象
     * @param classes 目标对象的类型
     */
    public static <T> T mapObject(Object src, Class<T> classes) {
        Mapper mapper = DozerBeanMapperBuilder.buildDefault();
        return mapper.map(src, classes);
    }

    /**
     * 作为数组返回
     *
     * @param list 列表
     * @param p    表达式
     * @return 数组
     */
    public static <T, K> K[] asArray(List<T> list, TProp<T, K> p) {
        if (list == null || list.size() == 0) return null;
        int i = 0;
        K tmp;
        K[] arr = null;
        for (T e : list) {
            tmp = p.get(e);
            if (arr == null) {
                Class<?> c = tmp.getClass();
                Class<?> cc = c.getComponentType();
                if (cc != null) c = cc;
                arr = (K[]) Array.newInstance(c, list.size());
            }
            arr[i++] = p.get(e);
        }
        return arr;
    }

    /**
     * 将集合转成数组
     */
    public static <T> T[] toArray(Collection<T> list) {
        if (list == null || list.size() == 0) return null;
        int i = 0;
        T[] arr = null;
        for (T e : list) {
            if (arr == null) {
                Class<?> c = e.getClass();
                Class<?> cc = c.getComponentType();
                if (cc != null) c = cc;
                arr = (T[]) Array.newInstance(c, list.size());
            }
            arr[i++] = e;
        }
        return arr;
    }

    public static <T> void each(Iterable<T> e, TCond<T> c) {
        for (T t : e) if (!c.test(t)) break;
    }

    public static <T> void each(Iterable<T> e, TFunc<T> f) {
        for (T t : e) f.call(t);
    }

    public static <T> void eachArray(T[] e, TCond<T> c) {
        for (T t : e) if (!c.test(t)) break;
    }

    public static <T> void eachArray(T[] e, TFunc<T> f) {
        for (T t : e) {
            f.call(t);
        }
    }

    /**
     * 克隆一个字典
     */
    public static <T, K> Map<T, K> cloneMap(Map<T, K> src) {
        Map<T, K> dst = new HashMap<>();
        for (Map.Entry<T, K> e : src.entrySet()) {
            dst.put(e.getKey(), e.getValue());
        }
        return dst;
    }

    /**
     * 拷贝字典数据
     */
    public static <T, K> void copyMap(Map<T, K> src, Map<T, K> dst) {
        for (Map.Entry<T, K> e : src.entrySet()) {
            dst.put(e.getKey(), e.getValue());
        }
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

    public interface TFunc<T> {
        void call(T t);
    }
    /*
    public static <T> void eachArray(Iterator<T> e, TFunc<T> f) {
        while (e.hasNext()) {
            f.call(e.next());
        }
    }*/


}
