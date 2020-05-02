package net.fze.arch.component.report;

import com.github.ixre.fze.commons.report.*;

/**
 * 导出仓库
 */
public class ExportHub {
    private ItemManager _manager;

    public ExportHub(IDbProvider ip, String rootPath, Boolean cacheFiles) {
        if (rootPath.equals("")) {
            rootPath = "/query/";
        }
        this._manager = new ItemManager(ip, rootPath, cacheFiles);
    }

    public DataResult fetchData(String portal, Params p, String page, String rows) {
        IDataExportPortal item = this._manager.getItem(portal);
        if (!page.equals("")) {
            p.getValue().put("pageIndex", page);
        }
        if (!rows.equals("")) {
            p.getValue().put("pageSize", rows);
        }
        return item.getSchemaAndData(p);
    }
}