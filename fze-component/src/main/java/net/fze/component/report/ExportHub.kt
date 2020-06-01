package net.fze.component.report

/**
 * 导出仓库
 */
class ExportHub(ip: IDbProvider, rootPath: String, cacheFiles: Boolean) {
    private val _manager: ItemManager
    fun fetchData(portal: String, p: Params, page: String, rows: String): DataResult {
        val item = _manager.getItem(portal)
        if (page != "") {
            p.value["pageIndex"] = page
        }
        if (rows != "") {
            p.value["pageSize"] = rows
        }
        return item.getSchemaAndData(p)
    }

    init {
        var rootPath = rootPath
        if (rootPath == "") {
            rootPath = "/query/"
        }
        _manager = ItemManager(ip, rootPath, cacheFiles)
    }
}