package net.fze.util;

/** 计算工具 */
public class MathUtil {
    /**
     * 求两个数的最大公约数
     * 如： 2个机器每隔几秒操作一次,如何确保他们不会同时操作.　其中时间间隔的最大公倍不能为１
     */
    public static int maxDivisor(int v1, int v2) {
        // euclid欧几里得算法
        int surplus ;
        surplus  = v1%v2;
        while(surplus  != 0) {
            v1 = v2;
            v2 = surplus ;
            surplus  = v1%v2;
        }
        return v2;
    }
}