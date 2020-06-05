package net.fze.util

import net.fze.commons.std.KotlinLangExtension
import java.security.MessageDigest

/** 字符工具 */
class StrUtils {
    companion object {
        private val ext: KotlinLangExtension = KotlinLangExtension()

        /**
         * 字符模板
         * @param text 文本
         * @param args 参数
         */
        fun template(text: String, args: Map<String, String>): String {
            return ext.template(text, args)
        }

        /**
         * 将INT数组拼接为字符串
         *
         * @param arr 数组
         * @return 以","分割的字符串
         */
        fun intArrayJoin(arr: Array<Int>, delimer: CharSequence): String {
            if (arr.isEmpty()) return ""
            var i = 0
            val sa = arrayOfNulls<String>(arr.size)
            for (a in arr) {
                sa[i++] = a.toString()
            }
            return sa.joinToString(delimer)
        }

        /**
         * 将integer数组拼接为字符串
         *
         * @param arr 数组
         * @return 以","分割的字符串
         */
        fun integerArrayJoin(arr: IntArray, delimer: CharSequence): String {
            var i = 0
            val sa = arrayOfNulls<String>(arr.size)
            for (a in arr) {
                sa[i++] = a.toString()
            }
            return sa.joinToString(delimer)
        }

        /** 用指定字符号拼接字符串 */
        fun arrayJoin(arr: Array<String>, delimer: CharSequence): String {
            return arr.joinToString(delimer)
        }

        /**
         * 生成md5
         *
         * @param str
         * @return 32位MD5
         */
        fun md5(str: String): String {
            var md5str = ""
            try {
                // 1 创建一个提供信息摘要算法的对象，初始化为md5算法对象
                val md = MessageDigest.getInstance("MD5")
                // 2 将消息变成byte数组
                val input = str.toByteArray()
                // 3 计算后获得字节数组,这就是那128位了
                val buff = md.digest(input)
                // 4 把数组每一字节（一个字节占八位）换成16进制连成md5字符串
                md5str = bytesToHex(buff)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return md5str
        }

        /** 返回[str]的16位md5 */
        fun md5_16(str: String): String {
            return md5(str).substring(8, 24)
        }

        /**
         * 二进制转十六进制
         *
         * @param bytes
         * @return
         */
        private fun bytesToHex(bytes: ByteArray): String {
            val md5str = StringBuffer()
            // 把数组每一字节换成16进制连成md5字符串
            var digital: Int
            for (i in bytes.indices) {
                digital = bytes[i].toInt()

                if (digital < 0) {
                    digital += 256
                }
                if (digital < 16) {
                    md5str.append("0")
                }
                md5str.append(Integer.toHexString(digital))
            }
            return md5str.toString().toUpperCase()
        }
    }
}