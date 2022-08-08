package net.fze.util

import java.util.*

/**
 * 列表工具类
 */
class Lists {
    companion object {

        /**
         * 按顺序排列
         */
        @JvmStatic
        fun <T> sort(list: List<T>, c: Comparator<in T>): List<T> {
            return list.sortedWith(c)
        }

        /**
         * 将列表顺序颠倒
         */
        @JvmStatic
        fun <T> reverse(list: List<T>) {
            Collections.reverse(list)
        }

        /**
         * 按倒序排列
         */
        @JvmStatic
        fun <T> sortByDescending(list: List<T>, c: Comparator<in T>): List<T> {
            val dst = sort(list, c)
            reverse(dst)
            return dst
        }
    }
}