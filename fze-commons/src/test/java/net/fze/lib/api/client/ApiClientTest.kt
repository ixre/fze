package net.fze.lib.api.client

import net.fze.common.http.HttpClient
import org.junit.jupiter.api.Test
import java.net.URLEncoder

internal class ApiClientTest {
    @Test
    fun testApiClient() {
        var url = "http://localhost:1419/api"
        var key = "10000001"
        var secert = "fs2309soa23434098fs"
        val cli = ApiClient(url, key, secert, 15000)
        cli.useToken(IAccessToken { key, secret ->
            val url = url + "/access_token?key=${key}&secret=${secret}"
            val bytes = HttpClient.request(url, "GET", null, 15000)
            String(bytes)
        }, 30)
        val params = mutableMapOf<String, String>()
        params["app"] = "33e844ca-e7d0-4c26-96c7-4ac9610c98ad"
        val ret = String(cli.post("app/info", params))
        println("---result:${ret}")
    }

    @Test
    fun testPostJson(){
        val appId = "wxa207b7cf6db2f5db"
        val openId = "oPA5C1KzEkX-7qEbXjGZnf-Vq-6Y"
        val path = "/message/send-wx/${appId}/${openId}/"+ URLEncoder.encode("会员卡帐户变动提醒","UTF-8")
        val mp = mutableMapOf<String,String>()
        mp["first"] = "您已经在本店进行消费"
        mp["keyword1"] = "消费"
        mp["keyword2"] = "2020-10-21 14:00"
        mp["remark"] ="消费金额:$10"
        var bytes = this.client().postJSON(path,mp)
        println(String(bytes))
    }

    private fun client(): ApiClient {
        var url = "http://localhost:8080/api"
        var key = "10000001"
        var secert = "fs2309soa23434098fs"
        val cli = ApiClient(url, key, secert, 15000)
        cli.useToken(IAccessToken { key, secret ->
            val url = url + "/access_token?key=${key}&secret=${secret}"
            val bytes = HttpClient.request(url, "GET", null, 15000)
            String(bytes)
        }, 30)
        return cli
    }
}