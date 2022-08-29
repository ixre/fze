package net.fze.extras.report;

import net.fze.util.Maps;
import org.junit.jupiter.api.Test;

class SqlBuilderTest {

    @Test
    void resolve() {
        String origin = "  SELECT * FROM order\n" +
                "        WHERE {where}\n" +
                " #if {status} \n"+
                "   AND status = {status}\n" +
                " #else\n"+
                "   AND status <> 0"+
                " #endif"+
                "        ORDER BY {order_by}\n" +
                "        LIMIT {page_offset},{page_size}";

       String sql1 =  SqlBuilder.resolve(origin, Maps.of("status","0.0"));
       System.out.println(sql1);
        String sql2 =  SqlBuilder.resolve(origin, Maps.of("status","False1"));
        System.out.println(sql2);
        System.out.println(0 == 0.0F);
    }
}