package com.github.ixre.fze.commons.std.api;

/**
 * 鉴权方法
 */
public interface CredentialFunc {
    /**
     * 获取凭据
     *
     * @param ctx 上下文
     * @param key 键
     * @return 鉴权信息
     */
    Pair get(Context ctx, String key);

    class Pair {
        private int userId;
        private String secret;

        public Pair(int userId, String secret) {
            this.userId = userId;
            this.secret = secret;
        }

        public int getUserId() {
            return this.userId;
        }

        public String getSecret() {
            return this.secret;
        }
    }
}
