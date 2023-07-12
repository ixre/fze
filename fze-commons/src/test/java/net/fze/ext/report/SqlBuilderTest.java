package net.fze.ext.report;

import net.fze.jdk.jdk8.Maps;
import org.junit.jupiter.api.Test;

import java.util.Map;

class SqlBuilderTest {

    @Test
    void resolve() {
        String origin = "SELECT COUNT(1) FROM wal_wallet_log l \n" +
                "\t\tWHERE l.wallet_id = {wallet_id}\\n AND title LIKE '%{keyword}%'\n" +
                "\t\t#if { kind>0 }\n" +
                "\t\t\tAND kind = {kind}\n" +
                "\t\t#else\n" +
                "\t\t\tAND kind = 'else'\n" +
                "\t\t#fi\n" +
                "\t\t#if { kind = 0 }\n" +
                "\t\t\tAND kind = 0 + {kind}\n" +
                "\t\t#fi\n" +
                "\t\t#if {trade_no == A111}\n" +
                "\t\tAND\t(trade_no ='A111')\";\n" +
                "\t\t#fi\n" +
                "\t\t#fi\n" +
                "\t\t#if {trade_no}\n" +
                "\t\tAND\t(trade_no IS NULL OR outer_no LIKE '%{trade_no}%')\";\n" +
                "\t\t#fi\n" +
                "\t\t#if {check} AND (check = 1) #fi\n" +
                "\n" +
                "\t\t#if {unchecked} AND (uncheck = {kind}) #fi";

        Map<String, Object> data = Maps.of("wallet_id", 0,
                "keyword", "提现",
                "kind", 0,
                "trade_no", "A111",
                "check", false,
                "unchecked", true);
        String sql1 = SqlBuilder.resolve(origin, data);
        System.out.println(sql1);
        String sql2 = SqlBuilder.resolve(origin, Maps.of("status", "False1"));
        System.out.println(sql2);
        System.out.println(0 == 0.0F);
    }
}