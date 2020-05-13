package net.fze.web.http;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

class FreeMakerLoader {

    private static Configuration cfg;
    private static String cfgSuffix;

    static void configure(String tpPath, String suffix) {
        cfg = new Configuration();
        cfgSuffix = suffix;
        try {
            cfg.setDirectoryForTemplateLoading(new File(tpPath));
        } catch (Throwable ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    @NotNull
    public static Template getTemplate(@NotNull String name) throws IOException {
        return cfg.getTemplate(name + cfgSuffix);
    }
}
