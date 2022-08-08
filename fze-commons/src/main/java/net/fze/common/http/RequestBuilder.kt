package net.fze.common.http


enum class ContentTypes(var value: String) {
    NOT(""),
    FORM("application/x-www-form-urlencoded"),
    FILES("multipart/form-data"),
    JSON("application/json")
}

class HttpRequest {
    var contentType: String = ContentTypes.NOT.value
    var timeout: Int = 0
    val headers: MutableMap<String, String> = mutableMapOf()
    var body: ByteArray? = null
    internal var method: String = "GET"
    internal var url: String = ""
}

class HttpRequestBuilder private constructor(url: String, method: String) {
    private var req: HttpRequest = HttpRequest()

    init {
        this.req.url = url
        this.req.method = method
    }

    companion object {
        @JvmStatic
        fun create(url: String, method: String): HttpRequestBuilder {
            return HttpRequestBuilder(url, method);
        }
    }

    fun headers(headers: Map<String, String>?): HttpRequestBuilder {
        if (headers == null) return this
        headers.forEach {
            this.req.headers[it.key] = it.value
        }
        return this
    }

    fun setHeader(key: String, value: String): HttpRequestBuilder {
        this.req.headers[key] = value
        return this
    }

    fun timeout(second: Int): HttpRequestBuilder {
        this.req.timeout = second
        return this
    }

    fun contentType(s: String): HttpRequestBuilder {
        this.req.contentType = s
        return this
    }

    fun body(bytes: ByteArray?): HttpRequestBuilder {
        this.req.body = bytes
        return this
    }

    fun build(): HttpRequest {
        if (this.req.url.isEmpty()) throw IllegalArgumentException("url")
        return this.req
    }
}