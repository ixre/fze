package com.github.ixre.fze.commons.infrastructure;
/*
  created for mzl-server [ Validator.java ]
  user: liuming (jarrysix@gmail.com)
  date: 24/11/2017 19:31
  description: 
 */

import com.github.ixre.fze.commons.std.LangExtension;
import kotlin.text.Regex;
import org.jetbrains.annotations.NotNull;

/**
 * 验证器
 */
public class Validator {
    private LangExtension _lang;
    private Regex userRegexp;
    private Regex emailRegexp;
    private Regex phoneRegexp;
    private Regex specCharRegexp;


    public Validator(LangExtension lang) {
        this._lang = lang;
        this.userRegexp = lang.regexp("^[a-zA-Z0-9_]{6,}$");
        this.emailRegexp = lang.regexp("^[A-Za-z0-9_\\-]+@[a-zA-Z0-9\\-]+(\\.[a-zA-Z0-9]+)+$");
        this.phoneRegexp = lang.regexp("^(13[0-9]|14[5|6|7]|15[0-9]|16[5|6|7|8]|18[0-9]|17[0|1|2|3|4|5|6|7|8]|19[1|8|9])(\\d{8})$");
        this.specCharRegexp = lang.regexp("(.+)(\\|\\$|\\^|%|#|!|\\\\/)+(.+)");
    }

    /**
     * 验证手机号码
     *
     * @param phone 手机
     * @return 是否匹配
     */
    public boolean testPhone(String phone) {
        return this.phoneRegexp.matches(phone);
    }

    /**
     * 是否包含特殊字符
     *
     * @param str 字符串
     * @return 是否包含
     */
    public boolean containSpecChar(@NotNull String str) {
        return this.specCharRegexp.matches(str);
    }

    /**
     * 验证用户名格式是否正确
     *
     * @param user 用户名
     * @return 是否正确
     */
    @NotNull
    public Boolean testUser(@NotNull String user) {
        return this.userRegexp.matches(user);
    }

    /**
     * 验证用邮箱格式是否正确
     *
     * @param email 邮箱地址
     * @return 是否正确
     */
    public boolean testEmail(@NotNull String email) {
        return this.emailRegexp.matches(email);
    }
}
