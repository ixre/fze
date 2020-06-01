package net.fze.web.http;

import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.util.Hashtable;

class HttpRequest2 {
    private final HttpExchange ex;

    public HttpRequest2(HttpExchange ex) {
        this.ex = ex;
    }

    /**
     * 字节数组中的indexOf函数，与String类中的indexOf类似 b 要搜索的字节数组 s 要找的字符串 start 搜索的起始位置
     * 如果找到，返回s的第一个字节在b中的下标，没有则返回-1
     */
    private static int byteIndexOf(byte[] b, String s, int start) {
        return byteIndexOf(b, s.getBytes(), start);
    }

    /**
     * 字节数组中的indexOf函数，与String类中的indexOf类似 b 要搜索的字节数组 s 要找的字节数组 start 搜索的起始位置
     * 如果找到，返回s的第一个字节在b中的下标，没有则返回-1
     */
    private static int byteIndexOf(byte[] b, byte[] s, int start) {
        int i;
        if (s.length == 0) {
            return 0;
        }
        int max = b.length - s.length;
        if (max < 0) {
            return -1;
        }
        if (start > max) {
            return -1;
        }
        if (start < 0) {
            start = 0;
        }
        // 在b中找到s的第一个元素
        search:
        for (i = start; i <= max; i++) {
            if (b[i] == s[0]) {
                // 找到了s中的第一个元素后，比较剩余的部分是否相等
                int k = 1;
                while (k < s.length) {
                    if (b[k + i] != s[k]) {
                        continue search;
                    }
                    k++;
                }
                return i;
            }
        }
        return -1;
    }

    /**
     * 用于从一个字节数组中提取一个字节数组 类似于String 类的substring()
     */
    private static byte[] subBytes(byte[] b, int from, int end) {
        byte[] result = new byte[end - from];
        System.arraycopy(b, from, result, 0, end - from);
        return result;
    }

    /**
     * 用于从一个字节数组中提取一个字符串 类似于String类的substring()
     */
    private static String subBytesString(byte[] b, int from, int end) {
        return new String(subBytes(b, from, end));
    }

    public void doPost() throws IOException {
        final int NONE = 0; // 状态码，表示没有特殊操作
        final int DATAHEADER = 1; // 表示下一行要读到报头信息
        final int FILEDATA = 2; // 表示下面要读的是上传文件和二进制数据
        final int FIELDDATA = 3; // 表示下面要读到表单域的文本值

        // 请求消息实体的总长度(请求消息中除消息头之外的数据长度)
        int totalbytes = Integer.valueOf(this.ex.getRequestHeaders().getFirst("Content-Length"));

        // 容纳请求消息实体的字节数组
        byte[] b = new byte[totalbytes];
        // 请求消息类型
        String contentType = this.ex.getRequestHeaders().getFirst("Content-Type");

        String fieldname = ""; // 表单域的名称
        String fieldvalue = ""; // 表单域的值
        String filename = ""; // 上传的文件名称
        String boundary = ""; // 分界符字符串
        String lastboundary = ""; // 结束分界符字符串

        int fileSize = 0; // 文件长度

        // 容纳表单域的名称/值的哈希表
        Hashtable formfields = new Hashtable();

        // 在消息头类型中找到分界符的定义
        int pos = contentType.indexOf("boundary=");

        String fileID; // 上传的文件ID

        if (pos != -1) {
            pos += "boundary=".length();
            boundary = "--" + contentType.substring(pos); // 解析出分界符
            lastboundary = boundary + "--"; // 得到结束分界符
        }

        int state = NONE; // 起始状态为NONE

        // 得到请求消息的数据输入流
        DataInputStream in = new DataInputStream(this.ex.getRequestBody());
        in.readFully(b); // 根据长度，将消息实体的内容读入字节数组b中
        in.close(); // 关闭数据流
        String reqcontent = new String(b); // 从字节数组中得到表示实体的字符串

        // 从字符串中得到输出缓冲流
        BufferedReader reqbuf = new BufferedReader(new StringReader(reqcontent));

        // 设置循环标志
        boolean flag = true;
        int i = 0;
        while (flag == true) {
            String s = reqbuf.readLine();
            if (s == lastboundary || s == null) break;
            switch (state) {
                case NONE:
                    if (s.startsWith(boundary)) {
                        // 如果读到分界符，则表示下一行一个头信息
                        state = DATAHEADER;
                        i += 1;
                    }
                    break;
                case DATAHEADER:
                    pos = s.indexOf("filename=");
                    // 先判断出这是一个文本表单域的头信息，还是一个上传文件的头信息
                    if (pos == -1) {
                        // 如果是文本表单域的头信息，解析出表单域的名称
                        pos = s.indexOf("name=");
                        pos += "name=".length() + 1; // 1表示后面的"的占位
                        s = s.substring(pos);
                        int l = s.length();
                        s = s.substring(0, l - 1);
                        fieldname = s; // 表单域的名称放入fieldname
                        state = FIELDDATA; // 设置状态码，准备读取表单域的值
                    } else {
                        // 如果是文件数据的头，先存储这一行，用于在字节数组中定位
                        String temp = s;
                        // 先解析出文件名
                        pos = s.indexOf("filename=");
                        pos += "filename=".length() + 1; // 1表示后面的"的占位
                        s = s.substring(pos);
                        int l = s.length();
                        s = s.substring(0, l - 1);
                        pos = s.lastIndexOf("//");
                        s = s.substring(pos + 1);
                        filename = s; // 文件名存入filename
                        // 下面这一部分从字节数组中取出文件的数据
                        pos = byteIndexOf(b, temp, 0); // 定位行

                        // 定位下一行，2 表示一个回车和一个换行占两个字节
                        b = subBytes(b, pos + temp.getBytes().length + 2, b.length);

                        // 再读一行信息，是这一部分数据的Content-type
                        s = reqbuf.readLine();

                        // 设置文件输入流，准备写文件
                        File f = new File("F:/java-workspace/luntan/html/image/" + filename);
                        DataOutputStream fileout = new DataOutputStream(new FileOutputStream(f));

                        // 字节数组再往下一行，4表示两回车换行占4个字节，本行的回车换行2个字节，Content-type的下
                        // 一行是回车换行表示的空行，占2个字节
                        // 得到文件数据的起始位置
                        b = subBytes(b, s.getBytes().length + 4, b.length);
                        pos = byteIndexOf(b, boundary, 0); // 定位文件数据的结尾
                        b = subBytes(b, 0, pos - 1); // 取得文件数据
                        fileout.write(b, 0, b.length - 1); // 将文件数据存盘
                        fileSize = b.length - 1; // 文件长度存入fileSize
                        state = FIELDDATA;
                    }
                    break;
                case FIELDDATA:
                    // 读取表单域的值
                    s = reqbuf.readLine();
                    fieldvalue = s; // 存入fieldvalue
                    formfields.put(fieldname, fieldvalue);
                    state = NONE;
                    break;
                case FILEDATA:
                    // 如果是文件数据不进行分析，直接读过去
                    while ((!s.startsWith(boundary)) && (!s.startsWith(lastboundary))) {
                        s = reqbuf.readLine();
                        if (s.startsWith(boundary)) {
                            state = DATAHEADER;
                        } else {
                            break;
                        }
                    }
                    break;
            }
        }
    /*
    // 指定内容类型，并且可以显示中文
    this.ex.getResponseHeaders().set("Content-Type", "text/html;charset=utf-8");
    PrintWriter out = res.getWriter();
    out.println("<HTML");
    out.println("<HEAD><TITLE>文件上传结果</TITLE></HEAD>");
    out.println("<BODY>");
    out.println("<H1>文件上传结果</H1><hr>");
    out.println(
        "ID为" + formfields.get("FileID") + "的文件" + filename + "已经上传!" + "文件长度为：" + fileSize + "字节");
    out.println("</BODY>");
    out.println("</HTML>");
    */
    }
}
