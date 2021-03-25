package net.fze.common;

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
    @SerializedName("Dict")
    public Map<String, String> dict;

    /**
     * 数据字典
     */
    @SerializedName("Data")
    public Object data;

    Result(int code, String msg) {
        this.errCode = code;
        this.errMsg = msg;
        this.dict = new HashMap<>();
    }

    public static Result create(int errCode, String errMsg) {
        return new Result(errCode, errMsg);
    }

    public static Result success(Object data) {
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

    public Result setDict(Map<String, String> dict) {
        this.dict = dict;
        return this;
    }
    public Result setData(Object data){
        this.data = data;
        return this;
    }

    /**
     * 存放数据
     */
    public Result put(String key, String value) {
        if (this.dict == null) {
            this.dict = new HashMap<>();
        }
        this.dict.put(key, value);
        return this;
    }

    /**
     * 获取值
     */
    public String get(String key) {
        if (this.dict.containsKey(key)) {
            return this.dict.get(key);
        }
        return null;
    }
}
