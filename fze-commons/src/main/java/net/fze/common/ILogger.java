package net.fze.common;

/**
 * 日志接口
 */
public interface ILogger {
    /**　输出日志 */
    void log(String message);
    /** 输出信息 */
    void info(String message);
    /** 输出警告 */
    void warning(String message);
    /** 输出错误 */
    void error(String message);
    /** 输出错误 */
    void error(String message,Throwable ex);
}
