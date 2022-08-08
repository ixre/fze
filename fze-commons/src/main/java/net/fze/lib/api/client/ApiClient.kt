package net.fze.lib.api.client

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import net.fze.common.http.ContentTypes
import net.fze.common.http.HttpClient
import net.fze.common.http.HttpRequestBuilder
import net.fze.util.Times
import net.fze.util.Types
import java.lang.reflect.Type

/**
 * API客户端
 */
class ApiClient {
    private var dataType: String = ""
    private var authHeaderKey: String = "Authorization"
    private var expires: Long = 0
    private var lastTokenUnix: Long = 0
    private var accessToken: String = ""
    private var accessFunc: IAccessToken? = null
    private var apiUrl = ""
    private var key = ""
    private var secret = ""
    private var _timeout = 0
    private val extraParams: MutableMap<String, String> = HashMap()

    constructor(apiUrl: String, key: String, secret: String, timeout: Int) {
        this.apiUrl = apiUrl
        this.key = key
        this.secret = secret
        this._timeout = timeout
    }

    fun jsonData(): ApiClient {
        this.dataType = "json"
        return this
    }

    /** 使用生成令牌 */
    fun useToken(tokenFunc: IAccessToken, tokenExpires: Long) {
        this.accessFunc = tokenFunc
        this.expires = tokenExpires
    }

    /** 设置认证头部键,默认为：Authorization */
    fun setAuthHeaderKey(key: String) {
        this.authHeaderKey = key
    }

    /**
     * 添加额外的参数
     *
     * @param key   参数名
     * @param value 值
     */
    fun addParam(key: String, value: String) {
        extraParams[key] = value
    }


    /**
     * 请求接口，返回原始内容
     *
     * @param apiPath 接口名称
     * @param params  参数
     * @return 响应字节
     */
    @Throws(Exception::class)
    fun request(apiPath: String, method: String, body: ByteArray?, contentType: String?): ByteArray {
        //val data = Types.cloneMap(params)
        //Types.copyMap(extraParams, data)
        //data["\$key"] = key
        // 更新令牌
        var now = Times.unix()
        if (now - this.lastTokenUnix > this.expires) {
            this.accessToken = this.accessFunc?.get(this.key, this.secret) ?: ""
            this.lastTokenUnix = now
        }
        // 发送请求
        val b = HttpRequestBuilder.create(this.concat(apiPath), method)
            .setHeader(this.authHeaderKey, this.accessToken)
            .body(body).timeout(this._timeout)
        // 设置格式
        //application/json
        if (!contentType.isNullOrEmpty()) b.setHeader("Content-Type", contentType)
        // 请求
        return HttpClient.request(b.build())
    }


    fun get(apiPath: String): ByteArray {
        return this.request(apiPath, "GET", null, null)
    }

    fun post(apiPath: String, params: Map<String, String>?): ByteArray {
        val bytes = HttpClient.parseBody(params)
        return this.request(apiPath, "POST", bytes, ContentTypes.FORM.value)
    }

    fun patch(apiPath: String, params: Map<String, String>?): ByteArray {
        val bytes = HttpClient.parseBody(params)
        return this.request(apiPath, "PATCH", bytes, ContentTypes.FORM.value)
    }

    fun put(apiPath: String, params: Map<String, String>?): ByteArray {
        val bytes = HttpClient.parseBody(params)
        return this.request(apiPath, "PUT", bytes, ContentTypes.FORM.value)
    }

    fun delete(apiPath: String, params: Map<String, String>?): ByteArray {
        val bytes = HttpClient.parseBody(params)
        return this.request(apiPath, "DELETE", bytes, null)
    }

    fun postJSON(apiPath: String, params: Any): ByteArray {
        val bytes = HttpClient.parseJsonBody(params)
        return this.request(apiPath, "POST", bytes, ContentTypes.JSON.value)
    }

    fun patchJSON(apiPath: String, params: Any): ByteArray {
        val bytes = HttpClient.parseJsonBody(params)
        return this.request(apiPath, "PATCH", bytes, ContentTypes.JSON.value)
    }

    fun putJSON(apiPath: String, params: Any): ByteArray {
        val bytes = HttpClient.parseJsonBody(params)
        return this.request(apiPath, "PUT", bytes, ContentTypes.JSON.value)
    }

    fun deleteJSON(apiPath: String, params: Any): ByteArray {
        val bytes = HttpClient.parseJsonBody(params)
        return this.request(apiPath, "DELETE", bytes, ContentTypes.JSON.value)
    }

    private fun concat(apiName: String): String {
        var path = apiName
        if (path.isNotEmpty() && path[0] != '/') {
            path = "/$path"
        }
        return this.apiUrl + path
    }


    /**
     * 反序列化结果
     */
    @Throws(Exception::class)
    fun <T> deserizeTypes(bytes: ByteArray, classOfT: Class<*>?, vararg classOfArgs: Class<*>?): T? {
        val ret = String(bytes)
        if (ret.isEmpty()) return null
        this.except(ret)
        val gt: Type = if (classOfArgs.isEmpty()) {
            TypeToken.get(classOfT).type
        } else {
            TypeToken.getParameterized(classOfT, *classOfArgs).type
        }
        return Gson().fromJson(ret, gt)
    }

    /**
     * 反序列化结果
     */
    @Throws(Exception::class)
    fun <T> deserize(bytes: ByteArray, gt: Type?): T? {
        val ret = String(bytes)
        if (ret.isNullOrEmpty()) return null
        this.except(ret)
        return Gson().fromJson(ret, gt)
    }

    /** 处理异常 */
    private fun except(ret: String) {
        val mp = Types.fromJson(ret, Map::class.java)
        val err = mp["err_msg"]
        if (err != null && err != "") throw Exception(err.toString())
    }
}