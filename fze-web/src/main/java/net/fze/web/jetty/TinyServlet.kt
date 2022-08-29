package net.fze.web.jetty

import freemarker.template.Configuration
import freemarker.template.Template
import net.fze.common.Standard
import net.fze.common.std.Creator
import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.handler.AbstractHandler
import java.io.File
import java.io.IOException
import java.lang.reflect.Method
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 *  Server server = new Server(8080);
 *  // 创建TinyServlet
 *  TinyServletHandler context = new TinyServletHandler().resolve();
 *  //TinyServletHandler context = new TinyServletHandler("com.yq.mzl.api.gw",
 *  //Creator.CLASS).resolve();
 *  // 集成FreeMarker
 *  context.setTemplate("FreeMaker",this.getClass(), "/templates", ".ftl");
 *  // 启动服务
 *  server.start();
 *  server.join();
 */

@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
annotation class Controller

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.FILE)
annotation class RequestMapping(val value: String)

/** 路由解析 */
class RouteResolver {
    class Action(var instance: Any, val method: Method)

    var pkg: String = ""
    var creator: Creator<*>? = null

    constructor() {
        //todo: not implement
//        val definedPackages = Thread.currentThread().contextClassLoader.definedPackages
//        this.pkg = definedPackages[definedPackages.size - 1].name
        this.creator = Creator.CLASS
    }

    constructor(pkg: String, creator: Creator<*>?) {
        this.pkg = pkg
        this.creator = creator ?: Creator.CLASS
    }

    private val actionMap = mutableMapOf<String, Action>()

    /** 解析 */
    fun resolve() {
        this.load(this.pkg, this.creator)
    }

    private fun load(pkg: String, creator: Creator<*>?) {
        val array = Standard.getPkgClasses(pkg) {
            !it.name.endsWith("Test") && (!it.isAnnotation && it.getAnnotationsByType(Controller::class.java)
                .isNotEmpty())
        }
        for (c in array) {
            val constructor = c.getDeclaredConstructor()
            if (constructor != null) {
                val instance = (creator ?: Creator.CLASS).create(c)
                if (instance == null) {
                    println("[ Servlet][ Except]: Class #${c.name}# create instance fail!")
                    continue
                }
                val ca = c.getAnnotationsByType(RequestMapping::class.java)
                this.resolveMethods(instance, ca.firstOrNull(), c!!)
            }
        }
    }

    /** 解析方法 */
    private fun resolveMethods(instance: Any, cm: RequestMapping?, c: Class<*>) {
        for (method in c.methods) {
            val mm = method.getAnnotation(RequestMapping::class.java)
            if (mm != null && this.checkHandler(c, method)) {
                val action = Action(instance, method)
                val urlPattern = this.joinUrl(cm, mm)
                if (this.actionMap.containsKey(urlPattern)) {
                    throw Exception("url pattern: $urlPattern has exists")
                }
                this.actionMap[urlPattern] = action
            }
        }
    }

    /** 验证方法签名 */
    private fun checkHandler(c: Class<*>, method: Method): Boolean {
        if (method.returnType != Error::class.java) {
            println("[ Route][ Warning]: ${c.name}#${method.name} not a handler (Detail:return type not be Error)")
            return false
        }
        val parameters = method.parameters
        if (parameters.size != 1 || parameters[0].type != HttpContext::class.java) {
            println("[ Route][ Warning]: ${c.name}#${method.name} not a handler (Detail:method args not match)")
            return false
        }
        return true
    }

    // 获取所有的action
    private fun actions(): Map<String, Action> = this.actionMap

    private fun joinUrl(cm: RequestMapping?, mm: RequestMapping): String {
        if (cm == null) {
            return mm.value
        }
        return cm.value + mm.value
    }

    override fun toString(): String {
        val buf = StringBuffer()
        for (entry in this.actions()) {
            buf.append("** url pattern :")
            buf.append(entry.key)
            buf.append(" => ")
            buf.append(entry.value.instance.javaClass.name)
            buf.append("#")
            buf.append(entry.value.method.name)
            buf.append("\n")
        }
        return buf.toString()
    }

    /** 获取匹配的Action */
    internal fun takeMatch(pathInfo: String): Action? {
        if (this.actionMap.containsKey(pathInfo)) {
            return this.actionMap[pathInfo]
        }
        return null;
    }
}

/** 路由处理器 */
class TinyServletHandler() : AbstractHandler() {
    private var resolver: RouteResolver = RouteResolver()
    private var locker: Any = Any()
    private var initialized: Boolean = false

    /** [pkgScope]搜索Servlet的包,[creator]指定如何创建Servlet */
    constructor(pkgScope: String, creator: Creator<*>?) : this() {
        this.resolver = RouteResolver(pkgScope, creator)
    }

    /** 解析包里的Servlet */
    fun resolve(): TinyServletHandler {
        this.checkResolve()
        return this
    }

    override fun handle(
        target: String,
        baseRequest: Request,
        request: HttpServletRequest,
        response: HttpServletResponse
    ) {
        this.proxyRequest(request, response)
        val contentType = response.getHeader("Content-Type");
        if (contentType.isNullOrEmpty()) {
            response.setHeader("Content-Type", "text/html;charset=utf-8")
        }
        baseRequest.isHandled = true
    }

    /** 处理请求 */
    private fun proxyRequest(req: HttpServletRequest, rsp: HttpServletResponse) {
        this.checkResolve()
        val act = this.resolver.takeMatch(req.pathInfo)
        if (act == null) {
            rsp.status = 404
            this.notfound(rsp)
        } else {
            val ctx = HttpContext(req, rsp)
            try {
                val err = act.method.invoke(act.instance, ctx)
                if (err != null) {
                    this.internalError(err as Error, rsp)
                }
            } catch (ex: Throwable) {
                ex.printStackTrace()
                this.internalError(ex, rsp)
            }
        }

    }

    private fun notfound(rsp: HttpServletResponse): Error? {
        return Standard.std.tryCatch {
            rsp.status = 404
            rsp.setHeader("Content-Type", "text/plain; charset=utf-8")
            rsp.resetBuffer()
            val stream = rsp.outputStream
            stream.write("HTTP 404: Not found".toByteArray())
            stream.flush()
            null
        }.except {
            it.printStackTrace()
            return@except null;
        }.error()
    }

    private fun internalError(err: Throwable, rsp: HttpServletResponse): Error? {
        rsp.status = 500
        rsp.setHeader("Content-Type", "text/plain; charset=utf-8")
        rsp.resetBuffer()
        val stream = rsp.outputStream
        stream.write("HTTP 500: ${err.message}".toByteArray())
        stream.flush()
        return null
    }

    private fun checkResolve() {
        if (this.initialized) return
        synchronized(locker) {
            this.initialized = true
            this.resolver.resolve()
        }
    }

    /** 设置模板  */
    fun setTemplate(engine: String?, clazz: Class<*>?, tpPath: String, suffix: String) {
        if (engine.isNullOrEmpty() || engine == "FreeMaker") {
            FreeMakerLoader.configure(clazz, tpPath, suffix)
        }
    }

}

/** 处理器 */
interface Handler {
    fun serveHttp(ctx: HttpContext): Error?
}

/** Http上下文 */
class HttpContext() {
    var form: MutableMap<String, String> = mutableMapOf()
    private lateinit var req: HttpServletRequest
    private lateinit var rsp: HttpServletResponse

    constructor(req: HttpServletRequest, rsp: HttpServletResponse) : this() {
        this.req = req
        this.rsp = rsp
        this.parseForm()
    }

    private fun parseForm() {
        this.req.parameterMap.forEach {
            if (it.value.size > 1) {
                this.form[it.key] = it.value.joinToString { "," }
            } else {
                this.form[it.key] = it.value[0]
            }
        }
    }

    fun form(): Map<String, String> {
        if (this.req.method == "POST") {
            return this.form
        }
        return mutableMapOf()
    }

    fun query(): Map<String, String> {
        if (this.req.method == "GET") {
            return this.form
        }
        return mutableMapOf()
    }

    fun response(): HttpServletResponse {
        return this.rsp
    }

    fun request(): HttpServletRequest {
        return this.req
    }

    fun param(k: String): String? {
        return this.req.getParameter(k)
    }

    fun redirect(code: Int, target: String): Error? {
        try {
            this.response().sendRedirect(target)
            this.response().status = code
        } catch (ex: Throwable) {
            return Error(ex.message)
        }
        return null
    }

    fun write(data: ByteArray): Error? {
        val cType = this.rsp.getHeader("Content-Type");
        if (cType == null || cType == "") {
            this.rsp.setHeader("Content-Type", "application/oct-stream")
        }
        this.rsp.outputStream.write(data)
        return null
    }

    fun writeString(s: String): Error? {
        this.rsp.outputStream.write(s.toByteArray())
        return null
    }

    fun render(name: String, data: Any?): Error? {
        try {
            val t = FreeMakerLoader.getTemplate(name)
            t.process(data, this.rsp.outputStream.writer())
            this.rsp.status = 200
        } catch (e: Throwable) {
            e.printStackTrace()
            return Error(e.message)
        }
        return null
    }
}

/** FreeMarker支持 */
internal object FreeMakerLoader {
    private var cfg: Configuration? = null
    private var cfgSuffix: String = ".ftl"

    /** 配置FreeMarker,[clazz]加载的类型，如为空则从文件路径加载，[basePath]模板根目录 */
    fun configure(clazz: Class<*>?, basePath: String, suffix: String) {
        cfg = Configuration(Configuration.VERSION_2_3_28)
        cfgSuffix = suffix
        try {
            if (clazz != null) {
                cfg!!.setClassForTemplateLoading(clazz, basePath)
            } else {
                cfg!!.setDirectoryForTemplateLoading(File(basePath))
            }
            //this.cfg!!.setEncoding(Locale.SIMPLIFIED_CHINESE,"UTF-8")
        } catch (ex: Throwable) {
            ex.printStackTrace()
            System.exit(1)
        }
    }

    @Throws(IOException::class)
    fun getTemplate(name: String): Template {
        return cfg!!.getTemplate(name + cfgSuffix)
    }
}
