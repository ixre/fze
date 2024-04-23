package net.fze.lib.api.client.test2.api

import net.fze.util.Types
import net.fze.common.http.HttpClient
import net.fze.lib.api.client.ApiClient
import net.fze.lib.api.client.IAccessToken

class TestClient {
    companion object{
        private var client  : ApiClient
        init {
            val apiKey = "mzlprodapi"
            val apiSecret = "fs2309soa23434098fs"
            val apiUrl = "http://localhost:8080/api"
            client = ApiClient(apiUrl, apiKey, apiSecret,  5000)
            client.useToken(IAccessToken { key, secret ->
                // 根据key和密钥获取访问令牌
                val url = apiUrl + "/access_token?key=${key}&secret=${secret}"
                val bytes = HttpClient.request(url, "GET", "".toByteArray(), 15000)
                val map = Types.fromJson<Map<String,String>>(String(bytes),Map::class.java)
                val errMsg = map["err_msg"]
                if(!errMsg.isNullOrEmpty()){
                    println("[ App][ Error]: 请求令牌失败${errMsg}")
                    return@IAccessToken ""
                }
                val token = "Bearer "+map["access_token"]
                token
            }, 3600)
        }
        fun get():ApiClient{
            return client
        }
    }
}