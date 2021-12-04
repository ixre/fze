package net.fze.common.infrastructure

import net.fze.common.KotlinLangExtension
import net.fze.util.Strings
import org.junit.jupiter.api.Test
import java.util.*

class KotlinLangExtensionTest {
    @Test
    fun testTemplate() {
        var b = StringBuffer()
        b.append("  'appId':'{appId}', /*公众号名称，由商户传入*/")
                .append("  'timeStamp' :'{timeStamp}', /*时间戳，自1970年以来的秒数*/")
                .append("  'nonceStr'  :'{nonceStr}', /*随机串*/")
                .append("  'package'   :'{package}',/*  */")
                .append("  'signType'  :'{signType}', /*微信签名方式*/")
                .append("  'paySign'   :'{paySign}'  /*微信签名*/")
                .append(" },")
                .append("function(res){")
        val data = HashMap<String, String>()
        data["appId"] = "123"
        data["timeStamp"] = (System.currentTimeMillis() / 1000).toString()
        data["nonceStr"] = "23423"
        data["package"] = "prepay_id="
        data["signType"] = "MD5"
        data["paySign"] = "FFER"
        println(Strings.template(b.toString(), data))
    }
}