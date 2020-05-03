package net.fze.arch.component.report

import java.io.File
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

/** 参数 */
class Params(var value: MutableMap<String, String>) {
    /** 从Map中拷贝数据 */
    fun copy(src: Map<String, String>) {
        for (s in src) {
            if (s.key == "total" || s.key == "rows" || s.key == "params") {
                continue
            }
            this.value[s.key] = s.value.trim()
        }
    }
}

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
interface IDbProvider {
    /** 获取数据库连接 */
    fun getDB(): Connection
}


class DataResult {
    var rows: MutableList<Map<String, Any>>? = null
    var sub: MutableList<Map<String, Any>>? = null
    var total: Int = 0
    var err: String = ""
}


//数据导出入口
interface IDataExportPortal {
    //导出的列名(比如：数据表是因为列，这里我需要列出中文列)
    fun getColumnMapping(): Array<ColumnMapping>

    //获取要导出的数据及表结构
    fun getSchemaAndData(p: Params): DataResult

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
    fun export(rows: List<Map<String, Any>>,
               fields: Array<String>,
               names: Array<String>,
               formatter: List<IExportFormatter>): Array<Byte>
}

/** 数据格式化器 */
interface IExportFormatter {
    /** 格式化字段 */
    fun format(field: String,
               name: String,
               rowNum: Int,
               data: Any?): Any
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


// 导出项目
class ExportItem(db: IDbProvider, key: String, cfg: ItemConfig) : IDataExportPortal {
    var mapping: Array<ColumnMapping>? = null
    private var sqlConfig: ItemConfig = cfg
    private var dbProvider: IDbProvider = db
    private var portalKey: String = key

    override fun getColumnMapping(): Array<ColumnMapping> {
        if (this.mapping == null) {
            this.sqlConfig.columnMapping = this.formatMappingString(this.sqlConfig.columnMapping)
            this.mapping = Utils.parseColumnMapping(this.sqlConfig.columnMapping)
        }
        return this.mapping!!
    }

    /** 去掉空格和换行 */
    private fun formatMappingString(columnMapping: String): String {
        return columnMapping.replace(Regex("\\s|\\n"), "")
    }

    override fun getSchemaAndData(p: Params): DataResult {
        val r = DataResult()
        r.rows = mutableListOf()
        //初始化添加参数
        if (!p.value.containsKey("pageSize")) {
            p.value["pageSize"] = "10000000000"
        }
        if (!p.value.containsKey("pageIndex")) {
            p.value["pageIndex"] = "1"
        }
        // 获取页码和每页加载数量
        val pageIndex = p.value["pageIndex"]!!.toInt()
        val pageSize = p.value["pageSize"]!!.toInt()
        // 设置SQL分页信息
        if (pageIndex > 0) {
            p.value["page_start"] = ((pageIndex - 1) * pageSize).toString()
        } else {
            p.value["page_start"] = "0"
        }
        p.value["page_end"] = (pageIndex * pageSize).toString()
        p.value["page_size"] = pageSize.toString()
        // 创建连接
        val conn = this.dbProvider.getDB()
        //统计总行数
        if (this.sqlConfig.total != "") {
            val sql = Utils.sqlFormat(this.sqlConfig.total, p.value)
            try {
                val stmt = conn.prepareStatement(sql)
                val rs = stmt.executeQuery()
                if (rs.next()) {
                    r.total = rs.getInt(1)
                }
                stmt.close()
                rs.close()
            } catch (ex: Throwable) {
                ex.printStackTrace()
                r.err = "[ Export][ Error] -" + ex.message + "\n" + sql
            }
        }
        if (this.sqlConfig.query != "") {
            r.rows = execQuery(conn, this.sqlConfig.query, p)
        } else {
            r.err = "not contain any query"
        }
        if (this.sqlConfig.subQuery != "") {
            r.sub = this.execQuery(conn, this.sqlConfig.query, p)
        }
        conn.close()
        return r
    }

    private fun execQuery(conn: Connection, query: String, p: Params): MutableList<Map<String, Any>> {
        val list = mutableListOf<Map<String, Any>>()
        var sql = Utils.sqlFormat(query, p.value)
        if (sql == "") return mutableListOf()
        var rs: ResultSet? = null
        var stmt: PreparedStatement? = null
        try {
            // 如果包含了多条SQL,那么执行前面SQL语句，查询最后一条语句返回数据
            val sqlLines = sql.split(";\n")
            val t = sqlLines.size
            if (t > 1) {
                for ((i, line) in sqlLines.withIndex()) {
                    if (i != t - 1) run {
                        val smt = conn.prepareStatement(line)
                        smt.execute()
                        smt.close()
                    }
                }
                sql = sqlLines[t - 1]
            }
            stmt = conn.prepareStatement(sql)
            rs = stmt.executeQuery()
            val meta = rs.metaData
            val colCount = meta.columnCount
            while (rs.next()) {
                val mp = mutableMapOf<String, Any>()
                for (i in 0 until colCount) {
                    mp[meta.getColumnLabel(i + 1)] = rs.getObject(i + 1) ?: ""
                }
                list.add(mp)
            }
        } catch (ex: Throwable) {
            println("[ Export][ Error] -" + ex.message + "\n" + sql)
            ex.printStackTrace()
        } finally {
            rs?.close()
            stmt?.close()
        }
        return list;
    }

    override fun getJsonData(p: Params): String {
        throw Error("not implemented")
    }

    override fun getTotalView(p: Params): Map<String, Any> {
        throw Error("not implemented")
    }

    override fun getExportColumnNames(exportColumnNames: Array<String>): Array<String> {
        val names = mutableListOf<String>()
        val mapping = this.getColumnMapping()
        for (colName in exportColumnNames) {
            for (m in mapping) {
                if (m.field == colName) {
                    names.add(m.name)
                    break
                }
            }
        }
        return names.toTypedArray()
    }

    override fun export(ep: ExportParams, p: IExportProvider, f: IExportFormatter?): Array<Byte> {
        val r = this.getSchemaAndData(ep.params)
        val names = this.getExportColumnNames(ep.exportFields)
        val fmtArray = mutableListOf<IExportFormatter>()
        fmtArray.add(InternalFormatter())
        if (f != null) {
            fmtArray.add(f)
        }
        return p.export(r.rows!!, ep.exportFields, names, fmtArray)
    }
}


/** 导出项管理器 */
class ItemManager {
    // 配置存放路径
    private var rootPath: String
    // 配置扩展名
    private var cfgFileExt: String
    // 数据库连接
    private var dbGetter: IDbProvider
    // 导出项集合
    private var exportItems: MutableMap<String, ExportItem> = mutableMapOf()
    // 缓存配置文件
    private var cacheFiles: Boolean = false

    constructor(db: IDbProvider, rootPath: String, cacheFiles: Boolean) {
        this.rootPath = rootPath
        this.dbGetter = db
        this.cfgFileExt = ".xml"
        this.cacheFiles = cacheFiles
        if (this.rootPath == "") {
            this.rootPath = "/query/"
        }
        if (!this.rootPath.endsWith("/")) {
            this.rootPath = this.rootPath + "/"
        }
    }

    /** 获取导出项 */
    fun getItem(portalKey: String): IDataExportPortal {
        var item = this.exportItems[portalKey]
        if (item == null) {
            item = this.loadExportItem(portalKey)
            if (this.cacheFiles) {
                this.exportItems[portalKey] = item!!
            }
        }
        return item!!
    }

    /**创建导出项,watch：是否监视文件变化 */
    private fun loadExportItem(portalKey: String): ExportItem? {
        val pwd = System.getProperty("user.dir")
        val filePath = arrayOf(pwd, this.rootPath, portalKey, this.cfgFileExt).joinToString("")
        val f = File(filePath)
        if (!f.exists()) {
            throw Error("[ Export][ Error]: no such file; path: $filePath")
        }
        if (f.isDirectory) {
            throw Error("[ Export][ Error]: export item config is a directory; path: $filePath")
        }
        val cfg = Utils.readItemConfigFromXml(filePath)
                ?: throw Error(
                "[ Export][ Error]: can't load export item; path: $filePath")
        return ExportItem(this.dbGetter, portalKey, cfg)
    }
}


