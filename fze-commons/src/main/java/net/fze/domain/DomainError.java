package net.fze.domain;

/**
 * 领域错误
 */
public class DomainError extends Error {
    private final String _key;

    public DomainError(String key, String message) {
        super(message);
        this._key = key;
    }

    public String getKey() {
        return _key;
    }
}
