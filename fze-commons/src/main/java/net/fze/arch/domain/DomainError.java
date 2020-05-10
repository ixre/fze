package net.fze.arch.domain;

/**
 * 领域错误
 */
public class DomainError extends Error {
    public DomainError(String key, String message) {
        super(message);
    }
}
