package net.fze.arch.web.http;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

class StaticFile implements Handler {
    private static final HashMap<String, String> fileIMEM =
            new HashMap<String, String>() {
                {
                    put("css", "text/css");
                    put("js", "text/javascript");
                    put("png", "image/png");
                    put("jpg", "image/jpeg");
                    put("git", "image/gif");
                    put("pdf", "application/pdf");
                    put("xls", "application/excel");
                    put("xlsx", "application/excel");
                    put("doc", "application/doc");
                    put("docx", "application/docx");
                }
            };
    private int bufferSize = 4096;
    private String baseDirectory;

    public StaticFile(String directory) {
        this.baseDirectory = directory;
    }

    @Nullable
    @Override
    public Error serveHttp(@NotNull HttpContext ctx) {
        String path = ctx.request().uri().getPath();
        String cType = ctx.header().getFirst("Content-Type");
        if (cType == null || cType.equals("")) {
            cType = this.detectFileType(path);
        }
        ctx.response().header().set("Content-Type", cType);
        // 输出文件内容
        FileInputStream fs = null;
        try {
            File file = new File(this.baseDirectory, path);
            if (file.exists()) {
                ctx.response().status(200);
                fs = new FileInputStream(file);
                byte[] buffer = new byte[this.bufferSize];
                int ch;
                for (; ; ) {
                    ch = fs.read(buffer, 0, buffer.length);
                    ctx.response().getWriter().write(buffer, 0, ch);
                    if (ch < bufferSize) break;
                }
            } else {
                ctx.response().status(404);
                String errorMessage = "HTTP/1.1 404 File Not Found";
                ctx.response().getWriter().write(errorMessage.getBytes());
            }
        } catch (Throwable ex) {
            return new Error(ex.getMessage());
        } finally {
            if (fs != null) {
                try {
                    fs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 检测文件类型
     */
    private String detectFileType(String path) {
        int i = path.lastIndexOf((int) '.');
        if (i != -1) {
            String ext = path.substring(i + 1);
            if (fileIMEM.containsKey(ext)) {
                return fileIMEM.get(ext);
            }
        }
        return "application/octet-stream";
    }
}
