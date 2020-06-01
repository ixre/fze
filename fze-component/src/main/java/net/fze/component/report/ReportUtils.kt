package net.fze.component.report

import net.fze.commons.std.Types
import java.io.File
import javax.xml.bind.JAXBContext

/**
 * 导出工具类
 */
class ReportUtils {
    companion object {
        /** 获取列映射数组 */
        fun readItemConfigFromXml(xmlFilePath: String): ItemConfig? {
            val f = File(xmlFilePath)
            val ctx = JAXBContext.newInstance(ItemConfig::class.java)
            return ctx.createUnmarshaller().unmarshal(f) as ItemConfig
        }
        /** 转换列与字段的映射 */
        fun parseColumnMapping(str: String): Array<ColumnMapping> {
            throw Error("not implement")
            /*
            re, err := regexp.Compile("([^:]+):([^;]*);*\\s*")
            if err != nil {
                return nil
            }
            var matches = re.FindAllStringSubmatch(str, -1)
            if matches == nil {
                return nil
            }
            columnsMapping := make([]ColumnMapping, len(matches))
            for i, v := range matches {
                columnsMapping[i] = ColumnMapping{Field: v[1], Name: v[2]}
            }
            return columnsMapping
            */
        }


        /** 转换参数 */
        fun parseParams(paramMappings: String): Params {
            val params = Params(mutableMapOf())
            if (paramMappings != null && paramMappings.length > 1) {
                if(paramMappings[0] =='{') {
                    val mp = Types.fromJson(paramMappings, Map::class.java);
                    mp.forEach {
                        params.value[it.key.toString()] = it.value.toString()
                    }
                }else {
                    val mapping = paramMappings.replace("%3d", "=")
                    val paramsArr = mapping.split(";")
                    var splitArr: List<String>
                    //添加传入的参数
                    for (v in paramsArr) {
                        splitArr = v.split(":")
                        val l = splitArr[0].length + 1
                        params.value[splitArr[0]] = v.substring(l)
                    }
                }
            }
            return params
        }

        // 格式化sql语句
        fun sqlFormat(sql: String, ht: Map<String, String>): String {
            var formatted = sql
            for (e in ht) {
                formatted = formatted.replace("{" + e.key + "}", e.value)
            }
            return formatted.trim()
        }

        /** 生成时间返回SQL */
        fun timeRangeSQL(range:String,field:String):String {
            if (range == "") return ""
            val arr = ReportParses.parseTimeRange(range)
            if (arr.size == 1) return String.format("%s >= %d", field, arr[0])
            if(arr[1] %3600 == 0)arr[1]+=3600*24-1 // 添加结束时间
            return String.format("%s BETWEEN %d AND %d", field, arr[0], arr[1])
        }
    }
}