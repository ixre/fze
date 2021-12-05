package net.fze.util

class RegexpTester {
    companion object {
        private val numberRegex: Regex = Regex("^(\\d)+\\.*(\\d)*$")
        private val userRegexp: Regex = Regex("^[a-zA-Z0-9_]{6,}$")
        private val emailRegexp: Regex = Regex("^[A-Za-z0-9_\\-]+@[a-zA-Z0-9\\-]+(\\.[a-zA-Z0-9]+)+$")
        private val phoneRegexp: Regex =
            Regex("^(13[0-9]|14[5|6|7]|15[0-9]|16[5|6|7|8]|18[0-9]|17[0|1|2|3|4|5|6|7|8]|19[1|8|9])(\\d{8})$")
        private val specCharRegexp: Regex = Regex("(.+)(\\|\\$|\\^|%|#|!|\\\\/)+(.+)")

        @JvmStatic
        fun regexp(pattern: String): Regex = Regex(pattern)

        @JvmStatic
        fun regexp(pattern: String, opt: RegexOption): Regex = Regex(pattern, opt)

        /**
         * 验证手机号码
         *
         * @param phone 手机
         * @return 是否匹配
         */
        @JvmStatic
        fun testPhone(phone: String): Boolean {
            return phoneRegexp.matches(phone)
        }

        /**
         * 是否包含特殊字符
         *
         * @param str 字符串
         * @return 是否包含
         */

        @JvmStatic
        fun containSpecChar(str: String): Boolean {
            return this.specCharRegexp.matches(str)
        }

        /**
         * 验证用户名格式是否正确
         *
         * @param user 用户名
         * @return 是否正确
         */

        @JvmStatic
        fun testUser(user: String): Boolean {
            return this.userRegexp.matches(user)
        }

        /**
         * 验证用邮箱格式是否正确
         *
         * @param email 邮箱地址
         * @return 是否正确
         */

        @JvmStatic
        fun testEmail(email: String): Boolean {
            return this.emailRegexp.matches(email)
        }

        /**
         * 是否为数字
         */
        @JvmStatic
        fun isNumber(value: Any?): Boolean {
            if (value == null) return false
            return numberRegex.matches(value.toString())
        }
    }
}