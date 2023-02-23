package net.fze.extras.report;

import net.fze.util.Strings;

public class ReportHub {
    private IConnProvider provider;
    private String rootPath;
    private boolean cacheFiles;

    public ReportHub(IConnProvider provider, String rootPath, boolean cacheFiles) {
        this.provider = provider;
        this.rootPath = rootPath;
        this.cacheFiles = cacheFiles;

        _manager = new ItemManager(provider, rootPath.equals("") ? "/query/" : rootPath, cacheFiles);
    }

    private ItemManager _manager;

    public DataResult fetchData(String portal, Params p, int page, int size) {
        IReportPortal item = _manager.getItem(portal);
        if (page > 0) {
            p.set("page_index", page);
        }
        if (size > 0) {
            p.set("page_size", size);
        }
        return item.getSchemaAndData(p);
    }
}
