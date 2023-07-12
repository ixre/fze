package net.fze.ext.jdbc

/** column flag */
enum class ColumnFlag(var value: Int) {
    PK(1),
    AUTO(2),
    INDEX(4),
    NULLABLE(8),
}

/** 数据列　*/
class Column() {
    /** 名称 */
    var name: String = ""

    /** 标志 */
    var flag: Int = 0

    /** 对应JDBC类型 */
    var dataType: Int = 1

    /** 类型名称 */
    var typeName: String = ""

    /** 长度 */
    var length: Int = 0

    /** 文档注释 */
    var comment: String = ""

    /** 默认值 */
    var defaultValue: String? = ""
}