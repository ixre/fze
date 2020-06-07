package net.fze.libs.api.client;

@FunctionalInterface
public interface IAccessToken {
    String get(String key, String secret);
}
