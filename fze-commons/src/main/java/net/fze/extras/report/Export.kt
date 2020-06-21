package net.fze.extras.report

import java.io.File
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

/** 参数 */
class Params(internal var value: MutableMap<String, String>) {
    /** 从Map中拷贝数据 */
    fun copy(src: Map<String, String>) {
        src.forEach { s ->
            if (s.key == "total" || s.key == "rows" || s.key == "params") {
                return@forEach
            }
            this.value[s.key] = s.value.trim()
        }
    }
    /** 添加参数 */
    fun set(key: String,value:String){
        this.value[key] = value
    }
    /** 获取参数 */
    fun get(key:String):String?{
        return this.value[key]
    }

    /** 是否包含参数 */
    fun contains(key:String):Boolean{
        return this.value.contains(key)
    }

    /** 删除参数 */
    fun remove(key:String){
        this.value.remove(key)
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
class ExportItem(db: IDbProvider, cfg: ItemConfig) : IDataExportPortal {
    var mapping: Array<ColumnMapping>? = null
    private var sqlConfig: ItemConfig = cfg
    private var dbProvider: IDbProvider = db

    override fun getColumnMapping(): Array<ColumnMapping> {
        if (this.mapping == null) {
            this.sqlConfig.columnMapping = this.formatMappingString(this.sqlConfig.columnMapping)
            this.mapping = ReportUtils.parseColumnMapping(this.sqlConfig.columnMapping)
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
        if (!p.contains("page_size")) {
            p.set("page_size","10000000000")
        }
        if (!p.contains("page_index")) {
            p.set("page_index","1")
        }
        // 获取页码和每页加载数量
        val pageIndex = p.get("page_index")!!.toInt()
        val pageSize = p.get("page_size")!!.toInt()
        // 设置SQL分页信息
        if (pageIndex > 0) {
            val offset = ((pageIndex - 1) * pageSize).toString()
            p.set("page_offset",offset)
            p.set("page_start",offset)  //todo: remove
        } else {
            p.set("page_offset","0")
            p.set("page_start","0") //todo: remove
        }
        p.set("page_over", (pageIndex * pageSize).toString()) //todo: remove
        p.set("page_end", (pageIndex * pageSize).toString())
        // 创建连接
        val conn = this.dbProvider.getDB()
        //统计总行数
        if (this.sqlConfig.total != "") {
            val sql = ReportUtils.sqlFormat(this.sqlConfig.total, p.value)
            try {
                val stmt = conn.prepareStatement(this.check(sql))
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
        try {
            if (this.sqlConfig.query != "") {
                r.rows = this.execQuery(conn, this.sqlConfig.query, p)
            } else {
                r.err = "not contain any query"
            }
            if (this.sqlConfig.subQuery != "") {
                r.sub = this.execQuery(conn, this.sqlConfig.query, p)
            }
        } catch (ex: Throwable) {
            ex.printStackTrace()
            r.err = "[ Export][ Error] -" + ex.message + "\n" + this.sqlConfig.query
        }
        conn.close()
        return r
    }

    private fun execQuery(conn: Connection, query: String, p: Params): MutableList<Map<String, Any>> {
        val list = mutableListOf<Map<String, Any>>()
        var sql = ReportUtils.sqlFormat(query, p.value)
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
            stmt = conn.prepareStatement(this.check(sql))
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
        return list
    }

    /** 判断注入 */
    private fun check(sql: String):String{
        if (ReportUtils.checkInject(sql)) throw SQLException("sql is dangers")
        return sql
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

    constructor(db: IDbProvider, rootPath: String, cache: Boolean) {
        this.rootPath = rootPath
        this.dbGetter = db
        this.cfgFileExt = ".xml"
        this.cacheFiles = cache
        if (this.rootPath == "") {
            this.rootPath = "/query/"
        }
        if (!this.rootPath.endsWith("/")) {
            this.rootPath = this.rootPath + "/"
        }
    }

    /** 获取导出项 */
    fun getItem(path: String): IDataExportPortal {
        var item = this.exportItems[path]
        if (item == null) {
            item = this.loadExportItem(path)
            if (this.cacheFiles) {
                this.exportItems[path] = item!!
            }
        }
        return item!!
    }

    /**创建导出项 */
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
        val cfg = ReportUtils.readItemConfigFromXml(filePath)
                ?: throw Error(
                "[ Export][ Error]: can't load export item; path: $filePath")
        return ExportItem(this.dbGetter, cfg)
    }
}



