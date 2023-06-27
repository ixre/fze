package net.fze.util;

import net.fze.common.ILogger;
import net.fze.domain.DomainException;

import java.util.Date;

/** 简单日志 */
public class Logs implements ILogger {
    private static int _logLevel = Level.ALL;
    private static String _application = "app";

    public static void setLogger(String application, int level) {
        _logLevel = level;
        _application = application;
    }

    // 可以在utils定义log.dart
    static void println(String message,int level, String prefix) {
        if( level != -1 && (_logLevel & level) !=level)return;
        System.out.printf("** %s ** [ %s][ %s]: %s%n",
                Times.format( new Date(),"yyyy-MM-dd HH:mm:ss"),
                _application,
                prefix,
                message);
    }

    /**
     * 　输出日志
     *
     */
    @Override
    public void log(String message) {
        println(message,Level.LOG,"log");
    }

    /**
     * 输出信息
     *
     */
    @Override
    public void info(String message) {
        println(message,Level.INFO,"info");

    }

    /**
     * 输出警告
     *
     */
    @Override
    public void warning(String message) {
        println(message,Level.WARNING,"warn");

    }

    /**
     * 输出错误
     *
     */
    @Override
    public void error(String message) {
        this.error(message,null);
    }

    /**
     * 输出错误
     *
     */
    @Override
    public void error(String message, Throwable ex) {
        println(message,Level.ERROR,"error");
        if(ex != null && !(ex instanceof DomainException)){
            ex.printStackTrace();
        }
    }

    /**
     * 日志级别
     */
    public static class Level {
        public static final int LOG = 1;
        public static final int INFO = 2;
        public static final int WARNING = 4;
        public static final int ERROR = 8;
        public static final int ALL = Level.LOG | Level.INFO | Level.WARNING | Level.ERROR;
    }

}
