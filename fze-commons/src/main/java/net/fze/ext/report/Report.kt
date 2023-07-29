package net.fze.ext.report

import net.fze.common.data.PagingResult
import java.sql.Connection
import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

/** 列映射 */
class ColumnMapping {
    //列的字段
    var field: String = ""

    //列的名称
    var name: String = ""
}

/** 导入导出项目配置 */
@XmlRootElement(name = "ExportItemConfig")
@XmlAccessorType(XmlAccessType.FIELD)
class ItemConfig {
    /** 字段映射 */
    @XmlElement(name = "ColumnMapping")
    var columnMapping: String = ""

    /** 查询 */
    @XmlElement(name = "Query")
    var query: String = ""

    /** 查询总条数 */
    @XmlElement(name = "Total")
    var total: String = ""

    /** 子查询 */
    @XmlElement(name = "SubQuery")
    var subQuery: String = ""

    @XmlElement(name = "Import")
    var import: String = ""
}

//导出参数
class ExportParams {
    /** 参数 */
    var params: Params = Params(mutableMapOf())

    /** 要到导出的列的名称集合 */
    var exportFields: Array<String> = arrayOf()
}

/** 数据库提供者 */
interface IConnProvider {
    /** 获取数据库连接 */
    fun getConn(): Connection
}

//数据导出入口
interface IReportPortal {
    //导出的列名(比如：数据表是因为列，这里我需要列出中文列)
    fun getColumnMapping(): Array<ColumnMapping>

    //获取要导出的数据及表结构
    fun getSchemaAndData(p: Params): PagingResult<Map<String, Any>>

    //获取要导出的数据Json格式
    fun getJsonData(p: Params): String

    //获取统计数据
    fun getTotalView(p: Params): Map<String, Any>

    //根据导出的列名获取列的索引及对应键
    fun getExportColumnNames(exportColumnNames: Array<String>): Array<String>

    //导出数据
    fun export(ep: ExportParams, p: IExportProvider, f: IExportFormatter?): Array<Byte>
}


/** 导出 */
interface IExportProvider {
    /** 导出 */
    fun export(
        rows: List<Map<String, Any>>,
        fields: Array<String>,
        names: Array<String>,
        formatter: List<IExportFormatter>
    ): Array<Byte>
}

/** 数据格式化器 */
interface IExportFormatter {
    /** 格式化字段 */
    fun format(
        field: String,
        name: String,
        rowNum: Int,
        data: Any?
    ): Any
}

/** 内置的格式化器 */
internal class InternalFormatter : IExportFormatter {
    override fun format(field: String, name: String, rowNum: Int, data: Any?): Any {
        if (field == "{row_number}") {
            return (rowNum + 1).toString()
        }
        if (data == null) {
            return ""
        }
        return data
    }
}
