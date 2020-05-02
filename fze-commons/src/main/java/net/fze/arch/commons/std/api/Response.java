package net.fze.arch.commons.std.api;

import com.google.gson.annotations.SerializedName;

/**
 * 接口响应
 */
public class Response {
    /**
     * 内部错误
     */
    public static final Response RInternalError = Response.create(10090, "internal error");
    /**
     * 无权限访问
     */
    public static final Response RAccessDenied = Response.create(10091, "access denied");
    /**
     * 未定义接口
     */
    public static final Response RUndefinedApi = Response.create(10092, "api not defined");
    /**
     * 参数不正确
     */
    public static final Response RIncorrectApiParams =
            Response.create(10093, "incorrect api parameters");
    /**
     * 请求成功
     */
    public static Response ROK = Response.create(0, "");
    /**
     * 代码
     */
    @SerializedName("Code")
    public int code;
    /**
     * 消息
     */
    @SerializedName("Message")
    public String message;
    /**
     * 数据
     */
    @SerializedName("Data")
    public Object data;

    public static Response create(int code, String message) {
        Response r = new Response();
        r.code = code;
        r.message = message;
        return r;
    }

    /**
     * 返回成功的响应
     */
    public static Response success(Object data) {
        return create(0, "").setData(data);
    }

    public Response setData(Object data) {
        this.data = data;
        return this;
    }
}
