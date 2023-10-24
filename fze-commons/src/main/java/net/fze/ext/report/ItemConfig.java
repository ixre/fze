package net.fze.ext.report;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 导入导出项目配置
 */
@XmlRootElement(name = "ExportItemConfig")
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemConfig {

    /**
     * 字段映射
     */
    @XmlElement(name = "ColumnMapping")
    private String columnMapping = "";

    /**
     * 查询
     */
    @XmlElement(name = "Query")
    private String query = "";

    /**
     * 查询总条数
     */
    @XmlElement(name = "Total")
    private String total = "";

    /**
     * 子查询
     */
    @XmlElement(name = "SubQuery")
    private String subQuery = "";

    @XmlElement(name = "Import")
    private String importSQL = "";

    public String getImportSQL() {
        return importSQL;
    }

    public String getSubQuery() {
        return subQuery;
    }

    public String getTotal() {
        return total;
    }

    public String getQuery() {
        return query;
    }

    public String getColumnMapping() {
        return columnMapping;
    }

    public void setColumnMapping(String s) {
        this.columnMapping = s;
    }
}
