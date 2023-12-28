package net.fze.common;

//import com.esotericsoftware.reflectasm.FieldAccess;
//import com.esotericsoftware.reflectasm.MethodAccess;

import java.util.UUID;

;

/**
 * 语言扩展
 */
public class LangExtension extends KotlinLangExtension {
  //
//    /**
//     * 拷贝相同的属性和数据到另一个对象
//     *
//     * @param src 源对象
//     * @param dst 目标对象
//     */
//    public void copyTo(Object src, Object dst) {
//        this.copy(dst, src);
//    }
//
//    /**
//     * 拷贝相同的属性和数据到另一个对象
//     *
//     * @param dst 目标对象
//     * @param src 源对象
//     */
//    public void copy(Object dst, Object src) {
//        Class srcClass = src.getClass();
//        Class dstClass = dst.getClass();
//        this.copyField(src, srcClass, dst, dstClass);
//        copyGetMethod(src, srcClass, dst, dstClass);
//    }
//
//    private void copyField(Object src, Class srcClass, Object dst, Class dstClass) {
//        FieldAccess sa = FieldAccess.get(srcClass);
//        FieldAccess da = FieldAccess.get(dstClass);
//        Map<String, Integer> props = new HashMap<>();
//        int i = 0;
//        for (String n : sa.getFieldNames()) {
//            props.put(n, i++);
//        }
//        for (String field : da.getFieldNames()) {
//            if (props.keySet().contains(field)) {
//                Object obj = sa.get(src, field);
//                if (obj != null) {
//                    try {
//                        da.set(dst, field, obj);
//                    } catch (ClassCastException ex) {
//                        System.out.println("java.lang.ClassCastException"
//                                + ex.getMessage() + "; target field:" + field
//                                + " source type:" + src.getClass().getTypeName()
//                                + " ; target type:" + dst.getClass().getTypeName());
//                        throw ex;
//                    }
//                }
//            }
//        }
//    }
//
//    private void copyGetMethod(Object src, Class srcClass, Object dst, Class dstClass) {
//        MethodAccess sa = MethodAccess.get(srcClass);
//        MethodAccess da = MethodAccess.get(dstClass);
//        Map<String, Integer> methods = new HashMap<>();
//        int i = 0;
//        for (String n : sa.getMethodNames()) {
//            methods.put(n, i++);
//        }
//        for (String method : da.getMethodNames()) {
//            if (method.startsWith("set")) {
//                String getMethod = "get" + method.substring(3);
//                if (methods.keySet().contains(getMethod)) {
//                    Object obj = sa.invoke(src, methods.get(getMethod));
//                    if (obj != null) {
//                        try {
//                            da.invoke(dst, method, obj);
//                        } catch (ClassCastException ex) {
//                            System.out.println("java.lang.ClassCastException" + ex.getMessage() + "; target method:"
//                                    + method + " source type:" + src.getClass().getTypeName()
//                                    + " ; target type:" + dst.getClass().getTypeName());
//                            throw ex;
//                        }
//                    }
//                }
//            }
//        }
//    }




}
