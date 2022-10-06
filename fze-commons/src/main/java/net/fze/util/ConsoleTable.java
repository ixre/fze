package net.fze.util;

import java.util.ArrayList;
import java.util.List;

public class ConsoleTable {
    private final List<String> titles;
    private int[] columnLen;
    private List<List<String>> stringRows = new ArrayList<>();
    private int colum = 2;
    private static int margin = 2;

    public ConsoleTable(List<String> titles, List<List<Object>> values) {
        this.titles = titles;
        if (this.titles != null && this.titles.size() > 0) {
            stringRows.add(0, this.titles);
        }
        int columnSize = 0;
        if (titles != null && !titles.isEmpty()) {
            columnSize = titles.size();
        }
        // 如果值的长度超过title的长度
        for (List<Object> row : values) {
            int size = row.size();
            if (size > columnSize)
                columnSize = size;
        }
        this.columnLen = new int[columnSize];
        for (List<Object> row : values) {
            List<String> sr = new ArrayList<>();
            for (int i = 0; i < row.size(); i++) {
                String s = TypeConv.toString(row.get(i));
                sr.add(s);
                int len = s.getBytes().length;
                // 更新列长度
                if (this.columnLen[i] < len) {
                    this.columnLen[i] = len;
                }
            }
            this.stringRows.add(sr);
        }
    }

    public String toString() {
        String padding = "  ";
        StringBuilder buf = new StringBuilder();
        int sumlen = 0;
        for (int len : columnLen) {
            sumlen += len;
        }
        boolean haveTitle = this.titles != null && this.titles.size() > 0;
        if (haveTitle) {
            buf.append("|").append(printChar('=', sumlen + padding.length() * colum + (colum - 1))).append("|\n");
        } else {
            buf.append("|").append(printChar('-', sumlen + 4 + (columnLen.length - 1))).append("|\n");
        }

        for (int i = 0; i < stringRows.size(); i++) {
            List row = stringRows.get(i);
            for (int j = 0; j < columnLen.length; j++) {
                // 求得值
                String o = "";
                if (j < row.size())
                    o = row.get(j).toString();
                buf.append("|").append(padding).append(o);
                if (columnLen[j] > o.length()) {
                    buf.append(Strings.repeat("-", columnLen[j] - o.length() - 1));
                }
                buf.append(padding);
                // buf.append(printChar(' ', columnLen[j] - o.getBytes().length + margin));
            }
            buf.append(padding).append("|\n");
            if (haveTitle && i == 0) {
                buf.append("|").append(printChar('=', sumlen + margin * 2 * colum + (colum - 1))).append("|\n");
            } else {
                buf.append("|").append(printChar('-', sumlen + margin * 2 * colum + (colum - 1))).append("|\n");
            }
        }
        return buf.toString();
    }

    private String printChar(char c, int len) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < len; i++) {
            buf.append(c);
        }
        return buf.toString();
    }

    public static void main(String[] args) {
        List<String> titles = Lists.of("学号", "姓名", "个人宣言", "性别");
        List<List<Object>> values = new ArrayList<>();
        values.add(Lists.of("101", "刘好", "困难像弹簧, 你弱他就强", "男"));
        values.add(Lists.of("101", "谢毓婷", "幸福总是这样的突然...", "男"));

        ConsoleTable t = new ConsoleTable(titles, values);
        System.out.println(t.toString());
    }
}
