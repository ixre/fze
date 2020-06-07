package net.fze.commons.std.api.client;

import net.fze.commons.http.HttpClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.fze.commons.Types;
import net.fze.commons.TypesConv;
import net.fze.commons.std.api.ApiUtils;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * API客户端
 */
public class ApiClient {
    private String _apiUrl;
    private String _key;
    private String _secret;
    private String _version;
    private String _signType;
    private int _timeout;
    private Map<String, String> extraParams = new HashMap<>();

    public ApiClient(String apiUrl, String key, String secret, String version, String signType, int timeout) {
        this._apiUrl = apiUrl;
        this._key = key;
        this._secret = secret;
        this._version = version;
        this._signType = signType;
        this._timeout = timeout;
    }

    public String getApiUrl() {
        return this._apiUrl;
    }

    public String getKey() {
        return this._key;
    }

    public String getSecret() {
        return this._secret;
    }

    public String getSignType() {
        return this._signType;
    }

    /**
     * 添加额外的参数
     *
     * @param key   参数名
     * @param value 值
     */
    public void addParam(String key, String value) {
        this.extraParams.put(key, value);
    }

    private Exception except(String ret) {
        ret = ret.substring(1);
        int i = ret.indexOf('#');
        int code = TypesConv.toInt(ret.substring(0, i));
        String message = ret.substring(i + 1);
        return new Exception(String.format("#%d:%s", code, message));
    }

    /**
     * 请求接口，返回原始内容
     *
     * @param apiName 接口名称
     * @param params  参数
     * @return 响应字节
     */
    public byte[] request(String apiName, Map<String, String> params) throws Exception {
        Map<String, String> data = Types.cloneMap(params);
        Types.copyMap(this.extraParams, data);
        data.put("api", apiName);
        data.put("key", this._key);
        data.put("version", this._version);
        data.put("sign_type", this._signType);
        data.put("sign", ApiUtils.Sign(this._signType, data, this._secret));
        String query = HttpClient.toQuery(data);
        return HttpClient.request(this._apiUrl, "POST", query.getBytes(), this._timeout);
    }

    /**
     * 调用接口
     *
     * @param apiName 接口名称
     * @param params  参数
     * @return 响应
     */
    public String call(String apiName, Map<String, String> params) throws Exception {
        String ret = new String(this.request(apiName, params));
        if (ret.startsWith("#")) {
            throw this.except(ret);
        }
        return ret;
    }


    /**
     * 支持泛型的调用接口
     *
     * @param apiName 接口名称
     * @param params  参数
     * @return 响应
     */
    public <T> T callByClass(String apiName, Map<String, String> params, Class<?> classOfT, Class<?>... classOfArgs) throws Exception {
        String ret = new String(this.request(apiName, params));
        if (ret.length() == 0) return null;
        if (ret.startsWith("#")) {
            throw this.except(ret);
        }
        Type gt;
        if (classOfArgs.length == 0) {
            gt = TypeToken.get(classOfT).getType();
        } else {
            gt = TypeToken.getParameterized(classOfT, classOfArgs).getType();
        }
        return new Gson().fromJson(ret, gt);
    }

    /**
     * 支持泛型的调用接口
     *
     * @param apiName 接口名称
     * @param params  参数
     * @return 响应
     */
    public <T> T callByType(String apiName, Map<String, String> params, Type gt) throws Exception {
        String ret = new String(this.request(apiName, params));
        if (ret.length() == 0) return null;
        if (ret.startsWith("#")) {
            throw this.except(ret);
        }
        return new Gson().fromJson(ret, gt);
    }


}
