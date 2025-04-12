package net.fze.common;

import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;
import net.fze.util.Assert;

import java.util.Map;

public class Result {
    public static final Result OK = new Result(0, "");
    /**
     * 错误代码
     */
    @SerializedName("code")
    @Schema(description = "错误代码")
    public int code;
    /**
     * 错误消息
     */
    @SerializedName("message")
    @Schema(description = "错误消息")
    public String message;
    /**
     * 数据
     */
    @SerializedName("data")
    @Schema(description = "数据")
    public Object data;

    Result(int code, String msg) {
        this.code = code;
        this.message = msg;
    }

    public static Result error(int errCode, String errMsg) {
        return new Result(errCode, errMsg);
    }

    public static Result of(Error err) {
        return err != null ? error(1, err.getMessage()) : success();
    }

    public static Result success() {
        return error(0, "");
    }

    public static Result success(Object data) {
        return error(0, "").setData(data);
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public Object getData(){
        return this.data;
    }

    @SuppressWarnings("unchecked")
    public Map<String,Object> asMap(){
        String s = this.data.getClass().getTypeName();
        Assert.checkType(this.data,Map.class);
        return (Map<String,Object>) this.data;
    }

    public Result setData(Object data) {
        this.data = data;
        return this;
    }
}
