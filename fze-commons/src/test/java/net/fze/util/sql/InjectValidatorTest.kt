package net.fze.util.sql

import org.junit.jupiter.api.Test

internal class InjectValidatorTest {
    @Test
    fun test() {
        var s = "11and20\"Begin\":0,\"Over\":500}"
        var mp = mutableMapOf<String, String>();
        /* mp.put("api","sms.saveTemplate");
         mp.put("key","83yqA365mzl141901");
         mp.put("product","mzl");*/
        mp["s"] =
            "{\"ShopCode\":\"479cc4b166324ad6aef43114b6a75e4e\",\"ObjectName\":\"-1\",\"state\":1,\"Sending_object\":\"-1\",\"Sending_time\":0,\"Sending_content\":\"尊敬的会员您好！店祝您生日快乐！\"}";
        mp["p2"] = "{\"build\":\"ss*2\"}"
        mp["p3"] =
            "{\"Id\":4288 and *,\"Name\":\"dd1\",\"StoreId\":301,\"TradClass\":0,\"Internal\":0,\"ChargeRate\":0,\"Card2Rate\":0,\"CardRate\":0,\"CreateTime\":1600141734,\"UpdateTime\":1600099200,\"VirtualRate\":0,\"ProdRate\":0,\"MchId\":301,\"RoleId\":37,\"AwardType\":2,\"VirtualRateSpc\":0,\"ProdRateSpc\":0,\"PerfromPer\":1,\"AwardPer\":1,\"RangeSpcPer\":3,\"PerfromGlob\":2,\"AwardGlob\":1,\"RangeSpcGlob\":2,\"ValidTime\":1601481600,\"AwardRanges\":[{\"Id\":0,\"PostId\":0,\"RangeType\":4,\"RangeTop\":1000000,\"_RangeTop\":\"10000\",\"RangeBottom\":0,\"_RangeBottom\":\"0\",\"RangeRate\":200,\"_RangeRate\":\"2\"},{\"Id\":0,\"PostId\":4288,\"RangeType\":4,\"RangeTop\":99999900,\"RangeBottom\":1000000,\"RangeRate\":300,\"_RangeBottom\":\"10000\",\"_RangeTop\":\"999999\",\"_RangeRate\":\"3\"}],\"PerfromItem\":1,\"DecCost\":1,\"ParentId\":0,\"Level\":16,\"__isset_bitfield\":134217727,\"_PerfromPer\":\"业绩平均拆分\",\"_PerfromItem\":\"业绩平均拆分\",\"_AwardPer\":\"全段提成\",\"_AwardGlob\":\"全段提成\",\"_flag1\":false,\"_flag2\":true,\"_flag3\":false,\"_str1\":\"\",\"_str2\":\"\",\"_str3\":\"\",\"_str4\":\"\",\"_str5\":\"\",\"_str6\":\"\",\"_str7\":\"\",\"_str8\":\"\",\"_str9\":\"\",\"_str10\":\"\",\"_str11\":\"\",\"_str12\":\"\"}"
        /* mp.put("storeCode","479cc4b166324ad6aef43114b6a75e4e");
         mp.put("version","2.4.0");
         mp.put("sign","3412c34c1e0ed073dd49ded7121c1e9039e4d1d4");
         mp.put("sign_type","");
         mp.put("","sha1");*/

        val buffer = StringBuffer()
        var i = 0
        for ((_, value) in mp.entries) {
            if (i++ > 0) {
                buffer.append("&")
            }
            buffer.append(value)
        }
        s = buffer.toString()
        val b = InjectValidator.test(s);
        println(s)
        if (b) {
            println("----------------------false")
        } else {
            println("----------------------true")
        }
    }
}
