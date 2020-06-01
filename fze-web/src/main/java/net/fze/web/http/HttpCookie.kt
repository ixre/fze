package net.fze.web.http

import java.util.*

class HttpCookie {
    /** 名称 */
    var name: String = ""
    /** 值 */
    var value: String = ""
    /** 路径 */
    var path: String = ""    // optional
    /** 域 */
    var domain: String = ""   // optional
    /** 有效时间 */
    var expires: Date = Date() // optional
    // MaxAge=0 means no 'Max-Age' attribute specified.
    // MaxAge<0 means delete cookie now, equivalently 'Max-Age: 0'
    // MaxAge>0 means Max-Age attribute present and given in seconds
    /* 过期时间 */
    var maxAge: Int = 0
    /** 是否安全 */
    var secure: Boolean = false
    /** 是否只在会话中保持 */
    var httpOnly: Boolean = true

    constructor(name: String, value: String) {
        this.name = name;
        this.value = value;
    }

    fun string(): String {
        if (this.name == "") return "";
        val b = StringBuffer()
        b.append(this.name).append("=").append(this.value)
        if (this.path.isNotEmpty()) {
            this.path = "/"
        }
        b.append("; Path=").append(this.path);
        if (this.domain.isNotEmpty()) {
            if (this.domain[0] == '.') {
                this.domain = this.domain.substring(1)
            }
            b.append("; Domain=").append(this.domain)
        }
        /**
        if validCookieExpires(c.Expires) {
        b.WriteString("; Expires=")
        b2 := b.Bytes()
        b.Reset()
        b.Write(c.Expires.UTC().AppendFormat(b2, TimeFormat))
        }
        if c.MaxAge > 0 {
        b.WriteString("; Max-Age=")
        b2 := b.Bytes()
        b.Reset()
        b.Write(strconv.AppendInt(b2, int64(c.MaxAge), 10))
        }
         **/
        if (this.maxAge > 0) {
            b.append("; Max-Age=").append(this.maxAge);
        } else if (this.maxAge < 0) {
            b.append("; Max-Age=0")
        }
        if (this.httpOnly) {
            b.append("; HttpOnly")
        }
        if (this.secure) {
            b.append("; Secure")
        }
        return b.toString()
    }
}