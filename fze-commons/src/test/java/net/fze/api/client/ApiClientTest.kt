package net.fze.api.client

import net.fze.commons.std.http.HttpClient
import org.junit.jupiter.api.Test
import java.util.*
import java.util.function.Predicate

internal class ApiClientTest {
    @Test
    fun testApiClient() {
        var url = "http://localhost:1419/api"
        var key = "10000001"
        var secert = "fs2309soa23434098fs"
        val cli = ApiClient(url, key, secert, IAccessToken { key, secret ->
            val url = url + "/access_token?key=${key}&secret=${secret}"
            val bytes = HttpClient.request(url, "GET", null, 15)
            String(bytes)
        }, 30, 60)
        val params = mutableMapOf<String, String>()
        params["app"] = "33e844ca-e7d0-4c26-96c7-4ac9610c98ad"
        val ret = String(cli.request("app/info", params))
        println("---result:${ret}")
    }
}