package net.fze.lib.api.client;

@FunctionalInterface
public interface IAccessToken {
    String get(String key, String secret);
}
