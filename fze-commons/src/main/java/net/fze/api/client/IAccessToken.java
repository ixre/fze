package net.fze.api.client;

@FunctionalInterface
public interface IAccessToken {
    String get(String key, String secret);
}
