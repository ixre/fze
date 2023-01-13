package net.fze.util;

import java.util.Date;

/** 简单日志 */
public class Logs {
    private static int _logLevel = Level.ALL;
    private static String _application = "app";

    public static void setLogger(String application, int level) {
        _logLevel = level;
        _application = application;
    }

    // 可以在utils定义log.dart
    static void println(Object message,int level, String prefix) {
        if( level != -1 && (_logLevel & level) !=level)return;
        System.out.println(String.format("** %s ** [ %s][ %s]: %s",
                Times.format( new Date(),"yyyy-MM-dd HH:mm:ss"),
                _application,
                prefix,
                message)
        );
    }

    public static void log(Object message) {
            println(message, Level.LOG,"log");
    }

    public static void info(Object message) {
        println(message, Level.INFO,"info");
    }

    public static void warning(Object message) {
        println(message, Level.WARNING,"warning");
    }

    public static void error(Object message) {
        println(message, -1,"error");
    }

    /**
     * 日志级别
     */
    public class Level {
        public static final int LOG = 1;
        public static final int INFO = 2;
        public static final int WARNING = 4;
        public static final int ERROR = 8;
        public static final int ALL = Level.LOG | Level.INFO | Level.WARNING | Level.ERROR;
    }

}
