package net.fze.common;

import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;
import net.fze.util.Assert;

import java.util.Map;


public class Result<T> {
    public static final Result<?> OK = new Result<>(0, "");
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
    public T data;

    private Result(int code, String msg) {
        this.code = code;
        this.message = msg;
    }

    public static <T> Result<T> error(int errCode, String errMsg) {
        return new Result<>(errCode, errMsg);
    }

    public static <T> Result<T> fail(int errCode, String errMsg) {
        return of(errCode, errMsg);
    }

    public static <T> Result<T> fail(String errMsg) {
        return of(1, errMsg);
    }

    private static <T> Result<T> of(int errCode, String message) {
        return new Result<>(errCode, message);
    }

    public static <T> Result<T> success() {
        return of(0, "");
    }

    public static <T> Result<T> fail() {
        return of(1, "failed");
    }

    public static <T> Result<T> of(Throwable ex) {
        if (ex == null) {
            return success();
        }
        return error(1, ex.getMessage());
    }

    public static <T> Result<T> success(T data) {
        Result<T> objectResult = success();
        objectResult.setData(data);
        return objectResult;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public T getData() {
        return this.data;
    }

    public Result<T> setData(T data) {
        this.data = data;
        return this;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> asMap() {
        String s = this.data.getClass().getTypeName();
        Assert.checkType(this.data, Map.class);
        return (Map<String, Object>) this.data;
    }
}
