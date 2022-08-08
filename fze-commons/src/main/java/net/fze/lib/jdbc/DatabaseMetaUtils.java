package net.fze.lib.jdbc;


import net.fze.util.TypeConv;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseMetaUtils {
    public static List<Column> resolveColumns(Connection connection, String table) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet rs = metaData.getColumns(connection.getCatalog(), "%", table, "%");
        List<Column> columns = new ArrayList<>();
        while (rs.next()) {
            Column col = new Column();
            col.setName(rs.getString(ResultSetColumnLabel.COLUMN_NAME.name()));
            col.setDataType(TypeConv.toInt(rs.getString(ResultSetColumnLabel.DATA_TYPE.name())));
            col.setTypeName(rs.getString(ResultSetColumnLabel.TYPE_NAME.name()));
            col.setLength(TypeConv.toInt(rs.getString(ResultSetColumnLabel.COLUMN_SIZE.name())));
            col.setComment(rs.getString(ResultSetColumnLabel.REMARKS.name()));
            col.setDefaultValue(rs.getString(ResultSetColumnLabel.COLUMN_DEF.name()));
            int flag = 0;
            //ResultSet keys = dbmeta.getPrimaryKeys(catalog, schema, tableName);
//            if (rs.getBoolean("IS_PRIMARY")) {
//                flag = flag | ColumnFlag.PK.getValue();
//            }
            if (rs.getBoolean("IS_NULLABLE")) {
                flag = flag | ColumnFlag.NULLABLE.getValue();
            }
            if (rs.getBoolean("IS_AUTOINCREMENT")) {
                flag = flag | ColumnFlag.AUTO.getValue();
            }
            if (rs.getBoolean("ORDINAL_POSITION")) {
                flag = flag | ColumnFlag.INDEX.getValue();
            }
            col.setFlag(flag);
            columns.add(col);
        }
        rs.close();
        return columns;
    }
}
