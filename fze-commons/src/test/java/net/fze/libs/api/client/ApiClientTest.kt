package net.fze.libs.api.client

import net.fze.commons.http.HttpClient
import org.junit.jupiter.api.Test

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
}