package net.fze.commons.http

import net.fze.commons.Types
import net.fze.util.IoUtils
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLDecoder
import java.net.URLEncoder
import java.security.SecureRandom
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager

class HttpUtilsKt {
    companion object{
        /**
         * 发送https请求
         *
         * @return JSONObject(通过JSONObject.get ( key)的方式获取json对象的属性值)
         */
        fun doRequest(req:HttpRequest):ByteArray {
            val prefix: String = req.url.substring(0, 5).toLowerCase()
            if (prefix == "https")return httpsRequest(req)
            return httpRequest(req)
        }


        /**
         * 发送https请求
         *
         * @param url 请求地址
         * @param method     请求方式（GET、POST）
         * @param data       提交的数据
         * @param headers    头部
         * @param timeout    超时时间
         * @return JSONObject(通过JSONObject.get ( key)的方式获取json对象的属性值)
         */
        @Throws(Exception::class)
        private fun httpRequest(req: HttpRequest): ByteArray {
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            val conn = URL(req.url).openConnection() as HttpURLConnection
            prepareConnection(conn, req.headers)
            // 设置请求方式（GET/POST）
            if(req.contentType!= "")req.contentType = req.contentType
            conn.requestMethod = req.method
            if (req.timeout > 0)conn.connectTimeout = req.timeout
            return getResponse(conn, req.body)
        }

        /**
         * 添加HTTP头
         *
         * @param conn    连接
         * @param headers 头部
         * @throws Exception
         */
        @Throws(Exception::class)
        private fun prepareConnection(conn: HttpURLConnection, headers: Map<String, String>?) {
            conn.doOutput = true
            conn.doInput = true
            conn.useCaches = false
            if (headers == null) return
            for ((key, value) in headers) {
                if (Types.emptyOrNull(key) || Types.emptyOrNull(value)) {
                    throw Exception("Headers contain null key or null value")
                }
                conn.setRequestProperty(key, value)
            }
        }


        /**
         * 发送https请求
         *
         * @param requestUrl 请求地址
         * @param method     请求方式（GET、POST）
         * @param data       提交的数据
         * @param headers    头部
         * @param timeout    超时时间
         * @return JSONObject(通过JSONObject.get ( key)的方式获取json对象的属性值)
         */
        @Throws(Exception::class)
        private fun httpsRequest(req: HttpRequest): ByteArray {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            val tm = arrayOf<TrustManager>(TrustAnyTrustManager())
            val sslContext = SSLContext.getInstance("SSL", "SunJSSE")
            sslContext.init(null, tm, SecureRandom())
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            val ssf = sslContext.socketFactory
            val conn = URL(req.url).openConnection() as HttpsURLConnection
            prepareConnection(conn, req.headers)
            conn.sslSocketFactory = ssf
            // 设置请求方式（GET/POST）
            if(req.contentType!= "")req.contentType = req.contentType
            conn.requestMethod = req.method
            if (req.timeout > 0) conn.connectTimeout = req.timeout
            return getResponse(conn, req.body)
        }


        @Throws(IOException::class)
        private fun getResponse(conn: HttpURLConnection, data: ByteArray?): ByteArray {
            return try {
                // 写入数据
                if (data != null && data.isNotEmpty()) {
                    val ost = conn.outputStream
                    ost.write(data)
                    ost.flush()
                    ost.close()
                }
                // 从输入流读取返回内容
                val ist = conn.inputStream
                val ret = IoUtils.streamToByteArray(ist)
                ist.close()
                ret
            } catch (ex: Exception) {
                throw ex
            } finally {
                conn.disconnect()
            }
        }

        /**
         * 将Map转换为查询
         *
         * @param params 参数
         * @return 查询
         */
        fun toQuery(params: Map<String, String>): String {
            var i = 0
            var v: String
            val buf = StringBuilder()
            for ((key, value) in params) {
                if (i++ > 0) {
                    buf.append("&")
                }
                try {
                    v = URLEncoder.encode(value, "utf-8")
                    buf.append(key).append("=").append(v)
                } catch (ex: Throwable) {
                    ex.printStackTrace()
                }
            }
            return buf.toString()
        }

        /**
         * 将查询转换为字典
         */
        fun parseQuery(query: String?): Map<String, String> {
            if (query != null && query != "") {
                try {
                    val pairs = URLDecoder.decode(query, "UTF-8").split("&".toRegex()).toTypedArray()
                    val mp: MutableMap<String, String> = HashMap()
                    for (p in pairs) {
                        val i: Int = p.indexOf('=')
                        if (i != -1) {
                            mp[p.substring(0, i)] = p.substring(i + 1)
                        }
                    }
                    return mp
                } catch (ex: Throwable) {
                    ex.printStackTrace()
                }
            }
            return HashMap()
        }

        fun parseBody(params: Map<String, String>?, cType: ContentTypes): ByteArray? {
            if(params == null || params.isEmpty())return null
            return when(cType){
                ContentTypes.JSON->Types.toJson(params).toByteArray()
                else-> toQuery(params).toByteArray()
            }
        }
    }
}