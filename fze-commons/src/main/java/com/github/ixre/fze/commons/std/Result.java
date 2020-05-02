package com.github.ixre.fze.commons.std;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class Result {
    public static final Result OK = new Result(0, "");
    /**
     * 错误代码
     */
    @SerializedName("ErrCode")
    public int errCode;
    /**
     * 错误消息
     */
    @SerializedName("ErrMsg")
    public String errMsg;
    /**
     * 数据字典
     */
    @SerializedName("Data")
    public Map<String, String> data;

    Result(int code, String msg) {
        this.errCode = code;
        this.errMsg = msg;
        this.data = new HashMap<>();
    }

    public static Result create(int errCode, String errMsg) {
        return new Result(errCode, errMsg);
    }

    public static Result success(Map<String, String> data) {
        return create(0, null).setData(data);
    }

    public int getErrCode() {
        return this.errCode;
    }

    public Result setErrCode(int errCode) {
        this.errCode = errCode;
        return this;
    }

    public String getErrMsg() {
        return this.errMsg;
    }

    public Result setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }

    public Result setData(Map<String, String> data) {
        this.data = data;
        return this;
    }

    /**
     * 存放数据
     */
    public Result put(String key, String value) {
        if (this.data == null) {
            this.data = new HashMap<>();
        }
        this.data.put(key, value);
        return this;
    }

    /**
     * 获取值
     */
    public String get(String key) {
        if (this.data.containsKey(key)) {
            return this.data.get(key);
        }
        return null;
    }
}
