package net.fze.util

import java.security.MessageDigest
import java.util.*

/** 字符工具 */
class Strings {
    companion object {
        /**
         * 是否为空字符串或空
         *
         * @param s 字符串
         * @return
         */
        @JvmStatic
        fun isNullOrEmpty(s: String?): Boolean {
            return s == null || s.trim().isEmpty()
        }

        /**
         * 字符模板
         * @param text 文本
         * @param args 参数
         */
        @JvmStatic
        fun template(text: String, args: Map<String, String>): String {
            val re = Regex("\\{([^{]+?)}")
            return re.replace(text) {
                args[it.groups[1]!!.value] ?: it.value
            }
        }

        /**
         * 将数组拼接为字符串
         *
         * @param arr 数组
         * @return 以","分割的字符串
         */
        @JvmStatic
        fun <T> join(arr: Iterable<T>, delimiter: CharSequence): String {
            var i = 0
            val sa = arrayOfNulls<String>(arr.count())
            arr.forEach{a->
                sa[i++] = a.toString()
            }
            return sa.joinToString(delimiter)
        }

        /**
         * 生成md5
         *
         * @param str
         * @return 32位MD5
         */
        @JvmStatic
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
        @JvmStatic
        fun shortMd5(str: String): String {
            return md5(str).substring(8, 24)
        }

        /**
         * 二进制转十六进制
         *
         * @param bytes
         * @return
         */
        @JvmStatic
        fun bytesToHex(bytes: ByteArray): String {
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

        @JvmStatic
        fun encodeBase64(bytes: ByteArray): ByteArray {
            return Base64.getEncoder().encode(bytes)
        }

        @JvmStatic
        fun decodeBase64(bytes: ByteArray): ByteArray {
            return Base64.getDecoder().decode(bytes)
        }

        @JvmStatic
        fun encodeBase64String(bytes: ByteArray): String {
            return Base64.getEncoder().encodeToString(bytes)
        }

        @JvmStatic
        fun decodeBase64String(s: String): ByteArray {
            return Base64.getDecoder().decode(s)
        }

        /** 如果s为空,则返回e, 反之返回s */
        @JvmStatic
        fun emptyElse(s: String?, e: String): String {
            if (s == null) return e;
            if (s.isEmpty()) return e
            return s
        }

        private const val letterStr = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"

        // 返回随机字符串,[n]:长度
        @JvmStatic
        fun randomLetters(n: Int): String {
            return this.randomLetters(n, letterStr)
        }

        /**
         * 返回随机字符串,[n]:长度,letters字段
         */
        @JvmStatic
        fun randomLetters(n: Int, letters: String): String {
            val l = letters.length
            var arr = arrayOfNulls<Char>(n)
            var rd = java.util.Random()
            for (i in 0 until n) {
                arr[i] = letters[rd.nextInt(l) % l]
            }
            return arr.joinToString("")
        }

        // 获取字符串位置
        @JvmStatic
        fun endPosition(s: String, b: Int, n: Int): Int {
            if (n == -1) {
                return s.length
            }
            return b + n
        }

        // 替换顺序b后的n个字符, 如果n为-1, 默认替换到结尾
        @JvmStatic
        fun replaceRange(s: String, b: Int, n: Int, replace: String): String {
            val end = this.endPosition(s, b, n)
            return s.replaceRange(b, end, replace)
        }

        // 替换顺序b后的n个字符, 如果n为-1, 默认替换到结尾
        @JvmStatic
        fun replaceN(s: String, b: Int, n: Int, replace: String): String {
            val end = this.endPosition(s, b, n)
            return s.replaceRange(b, end, this.repeat(replace, end - b))
        }

        // 重复字符串
        @JvmStatic
        fun repeat(s: String, n: Int): String {
            var arr = arrayOfNulls<String>(n)
            arr.forEachIndexed { i, _ -> arr[i] = s }
            return arr.joinToString("")
        }

    }
}
