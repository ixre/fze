package net.fze.common;

/**
 * 用户异常
 */
public class UserException extends RuntimeException{
    private final int _status;

    public UserException(int status, String message){
        super(message);
        this._status = status;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

    /**
     * 获取状态
     * @return 状态
     */
    public int getStatus() {
        return _status;
    }
}
