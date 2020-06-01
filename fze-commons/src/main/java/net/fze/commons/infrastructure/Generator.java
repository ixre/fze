package net.fze.commons.infrastructure;
/*
  created for mzl-server [ Geneartor.java ]
  user: liuming (jarrysix@gmail.com)
  date: 25/11/2017 19:09
  description: 
 */

import net.fze.commons.std.LangExtension;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Generator {
    private final LangExtension _lang;

    public Generator(LangExtension lang) {
        this._lang = lang;
    }

    @NotNull
    public String createOrderNo(String prefix) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyMMdd");
        String datePrefix = fmt.format(new Date());
        String randStr = String.valueOf(this._lang.randNumber(6));
        return prefix + datePrefix + randStr;
    }
}
