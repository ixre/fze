/**
 * Copyright (C) 2007-2020 56X.NET,All rights reserved.
 *
 * name : StringValidator.kt
 * author : jarrysix (jarrysix#gmail.com)
 * date : 2020-10-28 19:39
 * description :
 * history :
 */
package net.fze.extras

import org.jetbrains.annotations.NotNull


/**
 *　验证器工具类
 */
class Validator {
    companion object{
        private var userRegexp: Regex = Regex("^[a-zA-Z0-9_]{6,}$")
        private var emailRegexp: Regex = Regex("^[A-Za-z0-9_\\-]+@[a-zA-Z0-9\\-]+(\\.[a-zA-Z0-9]+)+$")
        private var phoneRegexp: Regex = Regex("^(13[0-9]|14[5|6|7]|15[0-9]|16[5|6|7|8]|18[0-9]|17[0|1|2|3|4|5|6|7|8]|19[1|8|9])(\\d{8})$")
        private var specCharRegexp: Regex = Regex("(.+)(\\|\\$|\\^|%|#|!|\\\\/)+(.+)")


        /**
         * 验证手机号码
         *
         * @param phone 手机
         * @return 是否匹配
         */
        @JvmStatic
        fun testPhone(@NotNull phone: String): Boolean {
            return phoneRegexp.matches(phone)
        }

        /**
         * 是否包含特殊字符
         *
         * @param s 字符串
         * @return 是否包含
         */
        @JvmStatic
        fun containSpecChar(@NotNull s: String): Boolean {
            return specCharRegexp.matches(s)
        }

        /**
         * 验证用户名格式是否正确
         *
         * @param user 用户名
         * @return 是否正确
         */
        @JvmStatic
        fun testUser(@NotNull user: String): Boolean {
            return userRegexp.matches(user)
        }

        /**
         * 验证用邮箱格式是否正确
         *
         * @param email 邮箱地址
         * @return 是否正确
         */
        @JvmStatic
        fun testEmail(@NotNull email: String): Boolean {
            return this.emailRegexp.matches(email)
        }
    }
}