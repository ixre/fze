/**
 * Copyright 2009-2020 @ to2.net
 * name : InjectValidator.kt
 * author : jarrysix (jarrysix#gmail.com)
 * date : 2020-01-06 17:41
 * description :
 * history :
 */
package net.fze.arch.commons.util.sql

/** 注入检查 */
class InjectValidator {
    companion object{
        private val regexp :Regex = Regex("\\b(and|exec|insert|select|drop|grant|alter|delete|update|count|chr|mid|master|truncate|char|declare|or)\\b|(\\*|;|\\+|'|%)",
               setOf(RegexOption.IGNORE_CASE, RegexOption.DOT_MATCHES_ALL));
        private val regexp1 :Regex = Regex("(.*)(\\b(and|exec|insert|select|drop|grant|alter|delete|update|chr|mid|master|truncate|char|declare|or)\\b|(\\*|;|\\+|'|%))(.*)",
                setOf(RegexOption.IGNORE_CASE, RegexOption.DOT_MATCHES_ALL));
        /**
         * 判断是否包含注入字符
         */
        fun test(s:String):Boolean{
            return regexp1.matches(s);
        }
    }
}