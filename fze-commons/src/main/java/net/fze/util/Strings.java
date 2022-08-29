package net.fze.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public  class Strings {

    /**
     * 是否为空字符串或空
     *
     * @param s 字符串
     * @return
     */
    public static boolean isNullOrEmpty(String s){
        return s == null || s.trim().isEmpty();
    }

    /**
     * 字符模板
     * @param text 文本
     * @param args 参数
     */

    public static String template(String text, Map<String, String> args) {
        return replace(text,"\\{([^{]+?)}",matcher ->
                Types.valueOrDefault( args.get(matcher.group(1)),matcher.group()));
    }

    /**
     * 将数组拼接为字符串
     *
     * @param arr 数组
     * @return 以","分割的字符串
     */
    public static  <T>  String join(Iterable<T> arr,CharSequence delimiter) {
        List<String> dst = new ArrayList<>();
        arr.forEach(a->dst.add(a.toString()));
        return String.join(delimiter,dst);
    }

    /**
     * 生成md5
     *
     * @param str
     * @return 32位MD5
     */
    public static String md5(String str) {
        String md5str = "";
        try {
            // 1 创建一个提供信息摘要算法的对象，初始化为md5算法对象
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] input = str.getBytes(StandardCharsets.UTF_8);
            // 3 计算后获得字节数组,这就是那128位了
            byte[] buff = md.digest(input);
            // 4 把数组每一字节（一个字节占八位）换成16进制连成md5字符串
            md5str = bytesToHex(buff);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return md5str;
    }

    /** 返回[str]的16位md5 */

    public static String shortMd5( String str) {
        return md5(str).substring(8, 24);
    }

    /**
     * 二进制转十六进制
     *
     * @param bytes
     * @return
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuffer md5str = new StringBuffer();
        // 把数组每一字节换成16进制连成md5字符串
        int digital = 0;
        for(int i=0;i<bytes.length;i++){
            digital = (int) bytes[i];

            if (digital < 0) {
                digital += 256;
            }
            if (digital < 16) {
                md5str.append("0");
            }
            md5str.append(Integer.toHexString(digital));
        }
        return md5str.toString().toUpperCase();
    }


    public static byte[] encodeBase64(byte[] bytes) {
        return Base64.getEncoder().encode(bytes);
    }

    public static byte[] decodeBase64(byte[] bytes) {
        return Base64.getDecoder().decode(bytes);
    }

    public static String encodeBase64String(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }
    public static byte[] decodeBase64String(String s) {
        return Base64.getDecoder().decode(s);
    }

    /** 如果s为空,则返回e, 反之返回s */
    public static String emptyElse( String s,  String e)  {
        if (s == null || s.isEmpty()) return e;
        return s;
    }

    private static final String letterStr = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    // 返回随机字符串,[n]:长度
    public  static String randomLetters(int n) {
        return randomLetters(n, letterStr);
    }

    /**
     * 返回随机字符串,[n]:长度,letters字段
     */
    public static String randomLetters(int n, String letters) {
        int l = letters.length();
        char[] arr =new char[n];
                Random rd = new Random();
                for(int i=0;i<n;i++){
            arr[i] = letters.charAt(rd.nextInt(l) % l);
        }
        return  new String(arr);
    }

    // 获取字符串位置
    public static int endPosition(String s,int b, int n) {
        if (n == -1) {
            return s.length();
        }
        return b + n;
    }

    // 替换顺序b后的n个字符, 如果n为-1, 默认替换到结尾
    public static String replaceRange( String s,int b,int n,String replace) {
        int end = endPosition(s, b, n);
        return s.replace(s.substring(b,end),replace);
       // return s.replaceRange(b, end, replace);
    }

    // 替换顺序b后的n个字符, 如果n为-1, 默认替换到结尾
    public static String replaceN( String s,int b,int n,String replace ) {
        int end = endPosition(s, b, n);
        return s.replace(s.substring(b,end),repeat(replace,end-b));
        //return s.replaceRange(b, end, repeat(replace, end - b));
    }

    // 重复字符串
    public static String repeat(String s, int n) {
        String[] arr = new String[n];
        for(int i=0;i<arr.length;i++){
            arr[i] = s;
        }
        return String.join("",arr);
    }


    public static String replace(String s, String pattern, Function<Matcher,String> re){
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(s);
        while(matcher.find()){
            s = s.replace(s,re.apply(matcher));
        }
        return s;
    }

    public static int indexOfAny(String workspace, Iterable<String> arr) {
        Iterator<String> iterator = arr.iterator();
        while(iterator.hasNext()){
           int i = workspace.indexOf( iterator.next());
           if(i != -1)return i;
        }
        return -1;
    }
}
