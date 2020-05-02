package com.github.ixre.fze.commons.util.sql

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class InjectValidatorTest{
    @Test
    fun test() {
        var s = "11and20\"Begin\":0,\"Over\":500}"
        var mp= mutableMapOf<String,String>();
       /* mp.put("api","sms.saveTemplate");
        mp.put("key","83yqA365mzl141901");
        mp.put("product","mzl");*/
        mp.put("s","{\"ShopCode\":\"479cc4b166324ad6aef43114b6a75e4e\",\"ObjectName\":\"-1\",\"state\":1,\"Sending_object\":\"-1\",\"Sending_time\":0,\"Sending_content\":\"尊敬的会员您好！店祝您生日快乐！\"}");
       /* mp.put("storeCode","479cc4b166324ad6aef43114b6a75e4e");
        mp.put("version","2.4.0");
        mp.put("sign","3412c34c1e0ed073dd49ded7121c1e9039e4d1d4");
        mp.put("sign_type","");
        mp.put("","sha1");*/

        val buffer = StringBuffer()
        var i = 0
        for ((_, value) in mp.entries) {
            if (i++ > 0) {
                buffer.append("&")
            }
            buffer.append(value)
        }
         s = buffer.toString()
        val b = InjectValidator.test(s);
        println(s)
        if(b){
            println("----------------------false")
        }else{
            println("----------------------true")
        }
    }
}
