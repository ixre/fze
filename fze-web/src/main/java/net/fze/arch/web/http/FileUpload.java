package net.fze.arch.web.http;

import com.sun.net.httpserver.HttpExchange;
import org.apache.commons.fileupload.RequestContext;

import java.io.IOException;
import java.io.InputStream;

public class FileUpload {
    public static RequestContext createContext(HttpExchange ex) {
        return new Context(ex);
    }

    private static class Context implements RequestContext {

        private HttpExchange ex;

        private Context(HttpExchange ex) {
            this.ex = ex;
        }

        @Override
        public String getCharacterEncoding() {
            return "utf-8";
        }

        @Override
        public String getContentType() {
            return ex.getRequestHeaders().getFirst("Content-Type");
        }

        /**
         * @deprecated
         */
        @Override
        public int getContentLength() {
            return 0;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return ex.getRequestBody();
        }
    }
}
