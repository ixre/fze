package net.fze.lib.api.client.test2.api

import net.fze.util.Types
import org.junit.jupiter.api.Test

internal class UserApiTest {
    /** 测试调用接口 */
    @Test
    fun checkProdUser() {
        val params = mutableMapOf<String,String>()
        params["user_code"] = "c5300815447f44538537a24052b03cdf"
        //var s = HttpClient.toQuery(params).toByteArray()
        //val ret = TestClient.get().request("/user/check","POST",s,ContentTypes.FORM.value)
        val ret = TestClient.get().post("/user/check",params)
        println("result="+String(ret))
        val result = TestClient.get().deserize<Map<String,Any>>(ret,Map::class.java)
        println("result="+ Types.toJson(result!!))
    }

    @Test
    fun queryUserProd() {
//        val params = mutableMapOf<String,String>()
//        params["user_code"] = "c5300815447f44538537a24052b03cdf"
        val map: MutableMap<String, String> = HashMap()
        map["api"] = "shop.getProdApi"
        map["key"] = "yqA365mzl141901"
        map["storeCodes"] = "[\"fa40437b17a44473a30bf8168e005020\",\"85dd1a9f8d654293a9d87ecebcf5b3d9\"]"
        map["sign"] = "efb1491ac26649138f0b12735097a82cf747db9a"
        map["sign_type"] = "sha1"
        //var s = HttpClient.toQuery(params).toByteArray()
        //val ret = TestClient.get().request("/user/check","POST",s,ContentTypes.FORM.value)
        val ret = TestClient.get().post("/user/queryUserProd",map)
        println("result="+String(ret))
        val result = TestClient.get().deserize<Map<String,Any>>(ret,Map::class.java)
        println("result="+ Types.toJson(result!!))
    }
}