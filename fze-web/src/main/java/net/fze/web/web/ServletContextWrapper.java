package net.fze.web.web;

import net.fze.commons.Result;
import net.fze.util.Types;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Servlet上下文包装
 */
public class ServletContextWrapper {
    private static final ServletContextWrapper wrapper = new ServletContextWrapper(null, null);
    private HttpServletRequest req;
    private HttpServletResponse rsp;

    private ServletContextWrapper(HttpServletRequest req, HttpServletResponse rsp) {
        this.req = req;
        this.rsp = rsp;
    }

    /**
     * 创建包含请求响应的上下文
     *
     * @param req 请求
     * @param rsp 响应
     * @return 上下文
     */
    public static ServletContextWrapper create(HttpServletRequest req, HttpServletResponse rsp) {
        return new ServletContextWrapper(req, rsp);
    }

    /**
     * 获取不包含请求响应的上下文
     *
     * @return 上下文
     */
    public static ServletContextWrapper getWrapper() {
        return wrapper;
    }

    /**
     * 返回JSONP结果
     *
     * @param data 数据
     * @return 结果
     */
    public String jsonpResult(Object data) {
        rsp.addHeader("Content-Type", "text/javascript");
        String callback = req.getParameter("callback");
        String jsonText = this.toJson(data);
        return callback + "(" + jsonText + ")";
    }

    /**
     * 将数据转换为JSON
     *
     * @param data 数据
     * @return JSON
     */
    public String toJson(Object data) {
        return Types.toJson(data);
    }

    /**
     * 将JSON转换为对象
     *
     * @param json JSON字符串
     * @param c    类型
     * @return 对象
     */
    public <T> T fromJson(String json, Class<T> c) {
        return Types.fromJson(json, c);
    }

    /**
     * 注册客户端脚本,通常用@ResponseBody
     *
     * @param script 脚本
     * @return 脚本
     */
    public String registerScript(String script) {
        return "<script type=\"text/javascript\">" + script + "</script>";
    }

    /**
     * 错误消息
     */
    public Result errMessage(String message) {
        return Result.create(1, message);
    }

    /**
     * errMessageNet 美助理客户端返回消息
     *
     * @return MessageNet
     */
    public Result errMessage(String message, Map<String, String> data) {
        return Result.create(1, message).setData(data);
    }

    /**
     * 成功消息
     */
    public Result successMessage(String message) {
        return Result.create(0, message);
    }

    /**
     * successMessageNet 美助理客户端返回消息
     *
     * @return MessageNet
     */
    public Result successMessage(String message, Map<String, String> data) {
        return Result.create(0, message).setData(data);
    }

    public String parentScript(String s) {
        return this.registerScript("window.parent." + s);
    }
}
