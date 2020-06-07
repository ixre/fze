package net.fze.libs.api.client

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import net.fze.commons.Types
import net.fze.commons.http.ContentTypes
import net.fze.commons.http.HttpClient
import net.fze.commons.http.HttpRequestBuilder
import java.lang.reflect.Type

/**
 * API客户端
 */
class ApiClient {
    private var dataType: String = ""
    private var headerKey: String = "Authorization"
    private var expires: Int = 0
    private var lastTokenUnix: Int = 0
    private var accessToken: String = ""
    private var accessFunc: IAccessToken? = null
    private var apiUrl = ""
    private var key = ""
    private var secret = ""
    private var _timeout = 0
    private val extraParams: MutableMap<String, String> = HashMap()

    constructor(apiUrl: String, key: String, secret: String,timeout: Int){
        this.apiUrl = apiUrl
        this.key = key
        this.secret = secret
        this._timeout = timeout
    }

    fun jsonData():ApiClient{
        this.dataType = "json"
        return this
    }

    /** 使用生成令牌 */
    fun useToken(tokenFunc:IAccessToken,tokenExpires:Int){
        this.accessFunc = tokenFunc
        this.expires = tokenExpires
    }

    /** 设置认证头部键,默认为：Authorization */
    fun setAuthKey(key:String){
        this.headerKey = key
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
    fun request(apiPath: String, method:String, body:ByteArray?,jsonData:Boolean): ByteArray {
        //val data = Types.cloneMap(params)
        //Types.copyMap(extraParams, data)
        //data["\$key"] = key
        // 更新令牌
        var now = Types.time.unix()
        if (now - this.lastTokenUnix > this.expires) {
            this.accessToken = this.accessFunc?.get(this.key, this.secret) ?: ""
            this.lastTokenUnix = now
        }
        // 发送请求
        val b = HttpRequestBuilder().create(this.concat(apiPath), method)
                .setHeader(this.headerKey,this.accessToken)
                .setHeader("api-user",this.key)
                .body(body).timeout(this._timeout)
        // 设置格式
        if(jsonData)b.contentType(ContentTypes.JSON.value)
        // 请求
        return HttpClient.request(b.build())
    }


    fun get(apiPath:String):ByteArray{
        return this.request(apiPath,"GET",null,false)
    }

    fun post(apiPath:String,params: Map<String, String>?): ByteArray{
        val bytes =HttpClient.parseBody(params,ContentTypes.FORM)
        return this.request(apiPath,"POST",bytes,false)
    }

    fun patch(apiPath:String,params: Map<String, String>?):ByteArray{
        val bytes =HttpClient.parseBody(params,ContentTypes.FORM)
        return this.request(apiPath,"PATCH",bytes,false)
    }

    fun put(apiPath:String,params: Map<String, String>?):ByteArray{
        val bytes =HttpClient.parseBody(params,ContentTypes.FORM)
        return this.request(apiPath,"PUT",bytes,false)
    }

    fun delete(apiPath:String,params: Map<String, String>?):ByteArray{
        val bytes =HttpClient.parseBody(params,ContentTypes.FORM)
        return this.request(apiPath,"DELETE",bytes,false)
    }

    fun postJSON(apiPath:String,params: Any): ByteArray{
        val bytes =HttpClient.parseJsonBody(params)
        return this.request(apiPath,"POST",bytes,true)
    }

    fun patchJSON(apiPath:String,params: Any):ByteArray{
        val bytes =HttpClient.parseJsonBody(params)
        return this.request(apiPath,"PATCH",bytes,true)
    }

    fun putJSON(apiPath:String,params: Any):ByteArray{
        val bytes =HttpClient.parseJsonBody(params)
        return this.request(apiPath,"PUT",bytes,false)
    }

    fun deleteJSON(apiPath:String,params: Any):ByteArray{
        val bytes =HttpClient.parseJsonBody(params)
        return this.request(apiPath,"DELETE",bytes,false)
    }

    private fun concat(apiName: String): String {
        var path = apiName
        if(path.isNotEmpty() && path[0]  !='/'){
            path = "/$path"
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
        val ret = String(this.post(apiName, params))
        this.except(ret)
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
        val ret = String(this.post(apiName, params))
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
     * 支持泛型的调用接口
     *
     * @param apiName 接口名称
     * @param params  参数
     * @return 响应
     */
    @Throws(Exception::class)
    fun <T> callByType(apiName: String, params: Map<String, String>?, gt: Type?): T? {
        val ret = String(this.post(apiName, params))
        if (ret.isEmpty()) return null
        this.except(ret)
        return Gson().fromJson(ret, gt)
    }

    /** 处理异常 */
    private fun except(ret: String) {
        val mp = Types.fromJson(ret,Map::class.java)
        val err = mp["err_msg"]
        if(err != null && err != "")throw Exception(err.toString())
    }
}