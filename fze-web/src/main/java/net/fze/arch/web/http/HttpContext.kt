package net.fze.arch.web.http

import com.sun.net.httpserver.Headers
import com.sun.net.httpserver.HttpExchange
import org.apache.commons.fileupload.FileItem
import java.io.OutputStreamWriter


interface Handler {
    fun serveHttp(ctx: HttpContext): Error?
}

class HttpContext {
    fun header(): Headers {
        return this.ex.requestHeaders
    }

    fun getStatusCode(): Int {
        return this.ex.responseCode;
    }

    fun setStatusCode(code: Int) {
        this.ex.sendResponseHeaders(code, 0)
    }

    private val rsp: HttpResponse

    /** 返回原始上下文 */
    fun raw(): HttpExchange {
        return this.ex
    }

    fun response(): HttpResponse {
        return this.rsp;
    }

    fun request(): HttpRequest {
        return this.req;
    }

    fun queryMap(): Map<String, String> {
        return this.request().queryValues();
    }

    fun queryValue(k: String): String {
        return this.request().queryValue(k);
    }

    fun forms(): Map<String, String> {
        return this.request().forms();
    }

    fun formValue(k: String): String {
        return this.request().formValue(k);
    }

    fun file(k: String): FileItem {
        return this.request().getFile(k);
    }

    fun redirect(code: Int, target: String): Error? {
        return this.response().redirect(code, target);
    }

    fun write(data: ByteArray): Error? {
        return this.response().write(data);
    }

    fun render(name: String, data: Any?): Error? {
        try {
            val t = FreeMakerLoader.getTemplate(name)
            this.rsp.header().set("Content-Type", "text/html;charset=UTF-8")
            this.ex.sendResponseHeaders(200, 0)
            val stream = this.response().writer;
            t.process(data, OutputStreamWriter(stream))
        } catch (e: Throwable) {
            e.printStackTrace()
            return Error(e.message)
        }
        return null;
    }

    private var ex: HttpExchange

    private var req: HttpRequest

    constructor(e: HttpExchange) {
        this.ex = e;
        this.rsp = HttpResponse(ex)
        this.req = HttpRequest(ex)
    }

    fun getCookie(name: String): Any? {
        // some small sanity check
        val cookies = this.header()["Cookie"] ?: return null
        for (cookie in cookies) {
            if (!cookie.contains("JSESSIONID") || !cookie.contains("WILE_E_COYOTE")) {
                // t.sendResponseHeaders(400,0)
            }
        }
        return null
        /*
        // return some cookies so we can check getHeaderField(s)
        val respHeaders = t.getResponseHeaders()
        var values: MutableList<String> = ArrayList()
        values.add("ID=JOEBLOGGS; version=1; Path=$URI_PATH")
        values.add("NEW_JSESSIONID=" + (SESSION_ID + 1) + "; version=1; Path="
                + URI_PATH + "; HttpOnly")
        values.add("NEW_CUSTOMER=WILE_E_COYOTE2; version=1; Path=$URI_PATH")
        respHeaders.put("Set-Cookie", values)

        values = ArrayList()
        values.add("COOKIE2_CUSTOMER=WILE_E_COYOTE2; version=1; Path=$URI_PATH")
        respHeaders.put("Set-Cookie2", values)
        values.add("COOKIE2_JSESSIONID=" + (SESSION_ID + 100)
                + "; version=1; Path=" + URI_PATH + "; HttpOnly")
        respHeaders.put("Set-Cookie2", values)

        t.sendResponseHeaders(200,0)
        t.close()
        */
    }
}