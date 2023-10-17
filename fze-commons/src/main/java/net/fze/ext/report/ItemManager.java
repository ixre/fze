package net.fze.ext.report;

import net.fze.jdk.jdk8.Lists;
import net.fze.util.Strings;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/** 导出项管理器 */

public class ItemManager {
    // 配置存放路径
    private String rootPath;

    // 配置扩展名
    private final String cfgFileExt;

    // 数据库连接
    private final IConnProvider dbGetter;

    // 导出项集合
    private final Map<String, ReportItem> exportItems;

    // 缓存配置文件
    private final Boolean cacheFiles;

    public ItemManager(IConnProvider db, String rootPath, Boolean cache) {
        this.rootPath = rootPath;
        this.dbGetter = db;
        this.cfgFileExt = ".xml";
        this.cacheFiles = cache;
        this.exportItems = new HashMap<>();
        if (this.rootPath.isEmpty()) {
            this.rootPath = "/query/";
        }
        if (!this.rootPath.endsWith("/")) {
            this.rootPath = this.rootPath + "/";
        }
    }

    /** 获取导出项 */
    public IReportPortal getItem(String path) {
        ReportItem item = this.exportItems.get(path);
        if (item == null) {
            item = this.loadReportItem(path);
            if (this.cacheFiles) {
                this.exportItems.put(path, item);
            }
        }
        return item;
    }

    /** 创建导出项 */
    private ReportItem loadReportItem(String portalKey) {
        String pwd = "";
        if(!this.rootPath.startsWith("classpath:")){
             pwd = System.getProperty("user.dir");
        }
        String filePath = Strings.join(Lists.of(pwd, this.rootPath, portalKey, this.cfgFileExt), "");

        File f = new File(filePath);
        if (!f.exists()) {
            throw new Error("[ Export][ Error]: no such file; path: " + filePath);
        }
        if (f.isDirectory()) {
            throw new Error("[ Export][ Error]: report item config is a directory; path: " + filePath);
        }
        ItemConfig cfg = ReportUtil.readItemConfigFromXml(filePath);
        if (cfg == null) {
            throw new Error(
                    "[ Export][ Error]: can't load report item; path: " + filePath);
        }
        return new ReportItem(this.dbGetter, cfg);
    }
}
