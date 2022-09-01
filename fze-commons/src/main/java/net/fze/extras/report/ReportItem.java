package net.fze.extras.report;


import net.fze.util.Lists;
import net.fze.util.Maps;
import net.fze.util.Strings;
import net.fze.util.TypeConv;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;

// 导出项目
public class ReportItem implements IReportPortal {
    private IDbProvider dbProvider;
    private ItemConfig sqlConfig;

    private ColumnMapping[] mapping  = null;
    public ReportItem(IDbProvider db,ItemConfig cfg){
    this.dbProvider = db;
    this.sqlConfig = cfg;
    }





    /** 判断注入 */
    private String check(String sql) throws SQLException {
        if (!ReportUtils.checkInject(sql)) throw new SQLException("sql is dangers");
        return sql;
    }

    @NotNull
    @Override
    public ColumnMapping[] getColumnMapping() {
        if (this.mapping == null) {
            this.sqlConfig.setColumnMapping(this.formatMappingString(this.sqlConfig.getColumnMapping()));
            this.mapping = ReportUtils.parseColumnMapping(this.sqlConfig.getColumnMapping());
        }
        return this.mapping;
    }



    /** 去掉空格和换行 */
    private String formatMappingString(String columnMapping) {
        return Strings.replace(columnMapping,"\\s|\\n",a->"");
    }

    @NotNull
    @Override
    public DataResult getSchemaAndData(@NotNull Params p) {
        DataResult r = new DataResult();
        r.setRows(Lists.of());
        //初始化添加参数
        if (!p.contains("page_size")) {
            p.set("page_size", "10000000000");
        }
        if (!p.contains("page_index")) {
            p.set("page_index", "1");
        }
        // 获取页码和每页加载数量
        int pageIndex = TypeConv.toInt(p.get("page_index"));
        int pageSize = TypeConv.toInt(p.get("page_size"));
        // 设置SQL分页信息
        if (pageIndex > 0) {
            String offset =String.valueOf ((pageIndex - 1) * pageSize);
            p.set("page_offset", offset);
        } else {
            p.set("page_offset", "0");
        }
        p.set("page_end", String.valueOf(pageIndex * pageSize));
        // 创建连接
        Connection conn = this.dbProvider.getDB();
        //统计总行数
        if (this.sqlConfig.getTotal() != "") {
            String sql = ReportUtils.sqlFormat(this.sqlConfig.getTotal(), p.getValue());
            try {
                PreparedStatement stmt = conn.prepareStatement(this.check(sql));
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    r.setTotal(rs.getInt(1));
                }
                stmt.close();
                rs.close();
            } catch (Throwable ex ) {
                ex.printStackTrace();
                r.setErr( "[ Export][ Error] -" + ex.getMessage() + "\n" + sql);
            }
        }
        try {
            if (this.sqlConfig.getQuery() != "") {
                r.setRows( this.execQuery(conn, this.sqlConfig.getQuery(), p));
            } else {
                r.setErr( "not contain any query");
            }
            if (this.sqlConfig.getSubQuery() != "") {
                r.setSub( this.execQuery(conn, this.sqlConfig.getQuery(), p));
            }
            conn.close();
        } catch ( Throwable ex) {
            ex.printStackTrace();
            r.setErr( "[ Export][ Error] -" + ex.getMessage() + "\n" + this.sqlConfig.getQuery());
        }
        return r;
    }

    @NotNull
    @Override
    public String getJsonData(@NotNull Params p) {
        return null;
    }

    @NotNull
    @Override
    public Map<String, Object> getTotalView(@NotNull Params p) {
        return null;
    }

    @NotNull
    @Override
    public String[] getExportColumnNames(@NotNull String[] exportColumnNames) {
        List<String> names = Lists.of();
        ColumnMapping[] mapping = this.getColumnMapping();
        for(int i=0;i<exportColumnNames.length;i++){
            String colName = exportColumnNames[i];
            for(int j = 0;j < mapping.length;j++){
                if (mapping[j].getField() .equals(colName)) {
                    names.add(mapping[j].getName());
                    break;
                }
            }
        }
        return names.toArray(new String[names.size()]);
    }

    @NotNull
    @Override
    public Byte[] export(@NotNull ExportParams ep, @NotNull IExportProvider p, @Nullable IExportFormatter f) {
        DataResult r = this.getSchemaAndData(ep.getParams());
        String[] names = this.getExportColumnNames(ep.getExportFields());
        List<IExportFormatter> fmtArray = Lists.of();
        fmtArray.add(new InternalFormatter());
        if (f != null) {
            fmtArray.add(f);
        }
        return p.export(r.getRows(), ep.getExportFields(), names, fmtArray);
    }

    private List<Map<String, Object>> execQuery(Connection conn, String query, Params p){
        ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        String sql = ReportUtils.sqlFormat(query, p.getValue());
        if (Strings.isNullOrEmpty(sql)) return Lists.of();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        try {
            // 如果包含了多条SQL,那么执行前面SQL语句，查询最后一条语句返回数据
            String[] sqlLines = sql.split(";\n");
            int t = sqlLines.length;
            if (t > 1) {
                int i = 0;
                for(String line :sqlLines){
                    if (i != t - 1) {
                        PreparedStatement smt = conn.prepareStatement(line);
                        smt.execute();
                        smt.close();
                    }
                    i++;
                }
                sql = sqlLines[t - 1];
            }
            stmt = conn.prepareStatement(this.check(sql));
            rs = stmt.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();
            int colCount = meta.getColumnCount();
            while (rs.next()) {
                Map<String,Object> mp = Maps.of();
                for(int i=0;i<colCount;i++){
                    Object v = rs.getObject(i + 1);
                    mp.put(meta.getColumnLabel(i + 1), v!=null?v:"");
                }
                list.add(mp);
            }

        } catch (Throwable ex) {
            ex.printStackTrace();
            System.out.println("[ Export][ Error] -" + ex.getMessage() + "\n" + sql);
        } finally {
            try {
                rs.close();
                stmt.close();
            }catch(Throwable ex){

            }
        }
        return list;
    }
}