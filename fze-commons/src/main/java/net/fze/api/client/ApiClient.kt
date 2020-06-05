package net.fze.api.client

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import net.fze.commons.std.Types
import net.fze.commons.std.TypesConv
import net.fze.commons.std.http.HttpClient
import java.lang.reflect.Type

/**
 * API客户端
 */
class ApiClient {
    private val expires: Int
    private var lastTokenUnix: Int = 0
    private var accessToken: String = ""
    private var accessFunc: IAccessToken
    private var apiUrl = ""
    private var key = ""
    private var secret = ""
    private var _timeout = 0
    private val extraParams: MutableMap<String, String> = HashMap()

    constructor(apiUrl: String, key: String, secret: String,a:IAccessToken,tokenExpires:Int,timeout: Int){
        this.apiUrl = apiUrl
        this.key = key
        this.secret = secret
        this._timeout = timeout
        this.accessFunc = a
        this.expires = tokenExpires
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

    private fun except(ret: String): Exception {
        var ret = ret
        ret = ret.substring(1)
        val i = ret.indexOf('#')
        val code = TypesConv.toInt(ret.substring(0, i))
        val message = ret.substring(i + 1)
        return Exception(String.format("#%d:%s", code, message))
    }

    /**
     * 请求接口，返回原始内容
     *
     * @param apiName 接口名称
     * @param params  参数
     * @return 响应字节
     */
    @Throws(Exception::class)
    fun request(apiName: String, params: Map<String, String>?): ByteArray {
        val data = Types.cloneMap(params)
        Types.copyMap(extraParams, data)
        data["user_key"] = key
        val query = HttpClient.toQuery(data)
        var now = Types.time.unix()
        if(now-this.lastTokenUnix > this.expires){
            this.accessToken = this.accessFunc.get(this.key, this.secret)
            this.lastTokenUnix = now
        }
        var headers = mutableMapOf<String,String>()
        headers["Authorization"] = this.accessToken
        return HttpClient.request(this.concat(apiName), "POST",
                query.toByteArray(),
                headers, _timeout)
    }

    private fun concat(apiName: String): String {
        var path = apiName
        if(path.length > 0 && path[0]  !='/'){
            path = "/"+path
        }
        return this.apiUrl + path
    }

    /**
     * 调用接口
     *
     * @param apiName 接口名称
     * @param params  参数
     * @return 响应
     */
    @Throws(Exception::class)
    fun call(apiName: String, params: Map<String, String>?): String {
        val ret = String(request(apiName, params))
        if (ret.startsWith("#")) {
            throw except(ret)
        }
        return ret
    }

    /**
     * 支持泛型的调用接口
     *
     * @param apiName 接口名称
     * @param params  参数
     * @return 响应
     */
    @Throws(Exception::class)
    fun <T> callByClass(apiName: String, params: Map<String, String>?, classOfT: Class<*>?, vararg classOfArgs: Class<*>?): T? {
        val ret = String(request(apiName, params))
        if (ret.length == 0) return null
        if (ret.startsWith("#")) {
            throw except(ret)
        }
        val gt: Type
        gt = if (classOfArgs.size == 0) {
            TypeToken.get(classOfT).type
        } else {
            TypeToken.getParameterized(classOfT, *classOfArgs).type
        }
        return Gson().fromJson(ret, gt)
    }

    /**
     * 支持泛型的调用接口
     *
     * @param apiName 接口名称
     * @param params  参数
     * @return 响应
     */
    @Throws(Exception::class)
    fun <T> callByType(apiName: String, params: Map<String, String>?, gt: Type?): T? {
        val ret = String(request(apiName, params))
        if (ret.length == 0) return null
        if (ret.startsWith("#")) {
            throw except(ret)
        }
        return Gson().fromJson(ret, gt)
    }
}