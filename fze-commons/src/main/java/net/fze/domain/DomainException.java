package net.fze.domain;

/**
 * 领域异常类
 */
public class DomainException extends RuntimeException {
    public DomainException(String message) {
        super(message);
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
