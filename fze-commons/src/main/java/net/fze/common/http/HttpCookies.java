package net.fze.common.http;

import java.util.HashMap;
import java.util.Map;

public class HttpCookies {
    private final HashMap<String, String> _map;

    public HttpCookies(){
        this._map = new HashMap<>();
    }

    public void put(String key, String value) {
        this._map.put(key,value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String,String> entry : this._map.entrySet()){
            if(sb.length() > 0){
                sb.append("; ");
            }
            sb.append(entry.getKey()).append("=").append(entry.getValue());
        }
        return sb.toString();
    }
}
