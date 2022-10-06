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

    public DataResult fetchData(String portal, Params p, String page, String rows) {
        IReportPortal item = _manager.getItem(portal);
        if (!Strings.isNullOrEmpty(page))
            p.set("page_index", page);
        if (!Strings.isNullOrEmpty(rows))
            p.set("page_size", rows);
        return item.getSchemaAndData(p);
    }
}
