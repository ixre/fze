package net.fze.common.std.api;

import com.google.gson.Gson;
import net.fze.common.Result;
import net.fze.util.DigestEncode;
import net.fze.util.TypeConv;

import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* API工具类 */
public class ApiUtils {
    // 签名
    public static String Sign(String signType, Map<String, String> r, String secret) {
        String data = SortedParamsString(r, secret);
        switch (signType) {
            case "md5":
                return DigestEncode.encode("md5", data);
            case "sha1":
                return DigestEncode.encode("sha1", data);
        }
        return "";
    }

    /**
     * 将字典转为字节
     */
    public static String SortedParamsString(Map<String, String> r, String secret) {
        String[] keys = new String[r.size()];
        int i = 0;
        for (String k : r.keySet()) {
            keys[i++] = k;
        }
        i = 0;
        Arrays.sort(keys);
        StringBuffer b = new StringBuffer();
        for (String key : keys) {
            if (key.equals("sign") || key.equals("sign_type")) {
                continue;
            }
            if (i++ > 0) {
                b.append("&");
            }
            try {
                String v = r.get(key);
                if (v == null) {
                    throw new Exception("参数值为空:" + key);
                }
                b.append(key).append("=").append(URLDecoder.decode(v, "utf-8"));
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
        b.append(secret);
        return b.toString();
    }

    /**
     * 解码接口响应为消息
     *
     * @param bytes 字节，格式：如{"ErrCode":1,"ErrMsg":"操作失败"}
     * @return 消息
     */
    private static Result decodeResult(byte[] bytes) {
        if (bytes != null && bytes.length != 0) {
            String rspText = new String(bytes);
            Map<String, Object> rspData = new HashMap<>();
            rspData = new Gson().fromJson(rspText, rspData.getClass());
            if (rspData.containsKey("ErrCode")) {
                int errCode = TypeConv.toInt(rspData.get("ErrCode"));
                Object errMsg = rspData.get("ErrMsg");
                Result msg = Result.create(errCode, "");
                if (errMsg != null) {
                    msg.setErrMsg(String.valueOf(errMsg));
                }
                return msg;
            }
        }
        return Result.create(1, "error response format");
    }

    /**
     * 编码接口消息
     *
     * @param msg 消息
     * @return 字符
     */
    public static String encodeResult(Result msg) {
        return String.format("{\"ErrCode\":\"%d\",\"ErrMsg\":\"%s\"}", msg.getErrCode(), msg.getErrMsg());
    }

    /* 比较版本 */
    public static int compareVersion(String v1, String v2) {
        return intVersion(v1) - intVersion(v2);
    }

    public static int intVersion(String s) {
        List<String> list = TypeConv.arrAsList(s.split("\\."));
        if (list.size() == 2) {
            list.add("0");
        }
        String[] arr = new String[]{list.get(0), list.get(1), list.get(2)};
        for (int i = 0; i < arr.length; i++) {
            String v = arr[i];
            int l = arr[i].length();
            if (l < 3) {
                StringBuilder sb = new StringBuilder();
                for (int x = 0; x < 3 - l; x++) {
                    sb.append("0");
                }
                sb.append(v);
                arr[i] = sb.toString();
            }
        }
        return TypeConv.toInt(String.join("", arr));
    }
}
