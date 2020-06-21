package net.fze.extras.report

/**
 * 导出仓库
 */
class ExportHub(ip: IDbProvider, rootPath: String, cacheFiles: Boolean) {
    private val _manager: ItemManager
    fun fetchData(portal: String, p: Params, page: String, rows: String): DataResult {
        val item = _manager.getItem(portal)
        if (page != "") {
            p.value["page_index"] = page
        }
        if (rows != "") {
            p.value["page_size"] = rows
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