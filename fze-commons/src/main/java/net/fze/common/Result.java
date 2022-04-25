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
    @SerializedName("Data")
    public Object data;

    Result(int code, String msg) {
        this.errCode = code;
        this.errMsg = msg;
    }

    public static Result error(int errCode, String errMsg) {
        return new Result(errCode, errMsg);
    }
    public static Result success() {
        return error(0, "");
    }

    public int getErrCode() {
        return this.errCode;
    }

    public String getErrMsg() {
        return this.errMsg;
    }

    public Result setData(Object data) {
        this.data = data;
        return this;
    }
}
