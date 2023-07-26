package net.fze.common.std.api;

import net.fze.util.Strings;
import net.fze.util.Types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务实现
 */
public class ServerMux implements Server {
    private boolean trace;
    private boolean cors = false;
    private Map<String, Processor> processors = new HashMap<>();
    private CredentialFunc swap;
    private ContextFactory factory;
    private List<MiddlewareFunc> middleware = new ArrayList<>();
    private List<MiddlewareFunc> afterMiddleware = new ArrayList<>();

    private ServerMux() {
    }

    /**
     * 创建新的服务实例
     *
     * @param factory 工厂
     * @param fn      函数
     * @return 服务
     */
    public static ServerMux New(ContextFactory factory, Boolean cors, CredentialFunc fn) {
        ServerMux mux = new ServerMux();
        mux.factory = factory;
        mux.swap = fn;
        mux.cors = cors;
        return mux;
    }

    @Override
    public void register(String name, Processor p) {
        synchronized (this.processors) {
            if (this.processors.containsKey(name)) {
                throw new Error("processor " + name + " has been resisted!");
            }
            this.processors.put(name, p);
        }
    }

    @Override
    public void use(MiddlewareFunc fn) {
        if (!this.middleware.contains(fn)) {
            this.middleware.add(fn);
        }
    }

    @Override
    public void After(MiddlewareFunc fn) {
        if (!this.afterMiddleware.contains(fn)) {
            this.afterMiddleware.add(fn);
        }
    }

    @Override
    public boolean Trace() {
        this.trace = true;
        ContextFactory.defaultContextFactory f = (ContextFactory.defaultContextFactory) this.factory;
        if (f != null) {
            f.setTrace(true);
        }
        return this.trace;
    }

    @Override
    public void ServeHTTP(ResponseWriter rsp, Request req) {
        if (this.cors) {
            String origin = req.getHeader("Origin");
            if (req.getMethod().equals("OPTIONS")) {
                this.preFlight(rsp, req, origin);
                return;
            }
            rsp.addHeader("Access-Control-Allow-Origin", origin);
        }
        Response[] arr = this.serveFunc(req);
        try {
            this.flushOutputWriter(rsp, arr);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    private void preFlight(ResponseWriter rsp, Request req, String origin) {
        rsp.addHeader("Access-Control-Allow-Origin", origin);
        rsp.addHeader("Access-Control-Allow-Methods", "PUT, GET, POST, DELETE, OPTIONS");
        rsp.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type,Credentials, Accept, Authorization, Access-Control-Allow-Credentials");
        rsp.addHeader("Access-Control-Allow-Credentials", "true");
        rsp.setStatus(200);
        rsp.write(new byte[0]);
    }

    // 输出
    private void flushOutputWriter(ResponseWriter w, Response[] arr) {
        if (arr == null || arr.length == 0) {
            throw new Error("no such response can flush to writer");
        }
        for (Response r : arr) {
            if (r.code > Response.ROK.code) {
                StringBuilder buf = new StringBuilder();
                buf.append("#").append(r.code).append("#")
                        .append(r.message);
                w.addHeader("Content-Type", "text/plain");
                w.write(buf.toString().getBytes());
                return;
            }
        }

        w.addHeader("Content-Type", "application/json");
        //w.setStatus(200);
        if (arr.length > 1) {
            Object[] rspData = new Object[arr.length];
            int i = 0;
            for (Response r : arr) {
                rspData[i++] = r.data;
            }
            w.write(this.marshal(rspData));
        } else {
            w.write(this.marshal(arr[0].data));
        }
    }

    // 序列化结果
    private byte[] marshal(Object rspData) {
        return Types.toJson(rspData).getBytes();
    }

    // 处理请求,如果同时请求多个api,那么api参数用","隔开
    private Response[] serveFunc(Request req) {
        String key = req.getParameter("key");
        Context ctx = this.factory.Factory(req, key, 0);
        CheckPermResult r = this.checkAccessPerm(ctx, key, req);
        if (r.rsp != null) {
            return new Response[]{r.rsp};
        }
        String api = req.getParameter("api");
        if (Strings.isNullOrEmpty(api)) {
            return new Response[]{Response.RUndefinedApi};
        }
        String[] name = api.split(",");
        Response[] arr = new Response[name.length];
        // resign user to api context
        ctx.resign(r.userId);
        // copy form data
        for (String k : req.getParameterMap().keySet()) {
            ctx.form().set(k, req.getParameter(k));
        }
        for (int i = 0; i < name.length; i++) {
            arr[i] = this.call(name[i], ctx);
        }
        return arr;
    }

    private Response call(String apiName, Context ctx) {
        String[] data = apiName.split("\\.");
        if (data.length != 2) {
            return Response.RUndefinedApi;
        }
        // save api name
        ctx.form().set("$api_name", apiName); // 保存接口名称
        // process api
        String entry = data[0].toLowerCase();
        String fn = data[1];
        Processor p = this.processors.get(entry);
        if (p != null) {
            // use middleware
            try {
                for (MiddlewareFunc m : this.middleware) {
                    Error err = m.handle(ctx);
                    if (err != null) {
                        Response r = Response.create(Response.RAccessDenied.code, err.getMessage());
                        return this.response(apiName, ctx, r);
                    }
                }
                return this.response(apiName, ctx, p.request(fn, ctx));
            } catch (Throwable ex) {
                ex.printStackTrace();
                return Response.create(Response.RInternalError.code, ex.getMessage());
            }
        }
        return Response.RUndefinedApi;
    }

    private Response response(String apiName, Context ctx, Response r) {
        if (this.afterMiddleware.size() > 0) {
            ctx.form().set("$api_response", r); // 保存响应
            for (MiddlewareFunc m : this.afterMiddleware) {
                m.handle(ctx);
            }
        }
        return r;
    }

    private CheckPermResult checkAccessPerm(Context ctx, String key, Request req) {
        String sign = req.getParameter("sign");
        String signType = req.getParameter("sign_type");
        // 检查参数
        if (this.empty(key) || this.empty(sign) || this.empty(signType)) {
            return new CheckPermResult(0, Response.RIncorrectApiParams);
        }
        if (!signType.equals("md5") && !signType.equals("sha1")) {
            return new CheckPermResult(0, Response.RIncorrectApiParams);
        }
        CredentialFunc.Pair p = this.swap.get(ctx, key);
        if (p.getUserId() <= 0 || this.empty(p.getSecret())) {
            return new CheckPermResult(p.getUserId(), Response.RAccessDenied);
        }
        Map<String, String> params = req.getParameterMap();
        String resign = ApiUtil.Sign(signType, params, p.getSecret());
        // 检查签名
        if (!resign.equals(sign)) {
            ctx.form().set("$user_id", p.getUserId());
            ctx.form().set("$user_secret", p.getSecret());
            ctx.form().set("$client_sign", sign);
            ctx.form().set("$server_sign", resign);
            return this.responseAccessDenied(params.get("api"), ctx, params, p.getUserId());
        }
        return new CheckPermResult(p.getUserId(), null);
    }

    private boolean empty(String s) {
        return s == null || s.equals("");
    }

    // response access denied
    private CheckPermResult responseAccessDenied(String api, Context ctx, Map<String, String> params, int userId) {
        if (!this.trace) {
            return new CheckPermResult(userId, Response.RAccessDenied);
        }
        // resign user
        ctx.resign(userId);
        // copy form data
        FormData cf = ctx.form();
        for (String k : params.keySet()) {
            cf.set(k, params.get(k));
        }
        Response r = this.response(params.get("api"), ctx, Response.RAccessDenied);
        return new CheckPermResult(userId, r);
    }

    class CheckPermResult {
        Response rsp;
        int userId;

        public CheckPermResult(int userId, Response r) {
            this.userId = userId;
            this.rsp = r;
        }
    }
}
