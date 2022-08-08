package net.fze.util;

import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


class TypesTest {

    @Test
    void toJson() {
        Map<String,Object> data = new HashMap<String,Object>();
        data.put("age",12);
        data.put("birth",new Date());
        String json = Types.toJson(data);
        System.out.println(json);
    }

    @Test
    void fromJson() {
        String json = "";
        Map<String,Object> data = Types.fromJson(json,Map.class);
        System.out.println(data.get("birth"));
    }
}
