package net.fze.commons.std.api;

import java.util.HashMap;

/**
 * 上下文
 */
public interface Context {
    // 返回接口KEY
    String key();

    // 返回对应用户编号
    int user();

    // 请求
    Request request();

    // 表单数据
    FormData form();

    /**
     * 重新分配用户编号
     *
     * @param userId
     */
    void resign(int userId);

    /**
     * 默认上下文实现
     */
    class DefaultContextImpl implements Context {
        Request h;
        String key;
        int userId;
        FormData form;

        public DefaultContextImpl(Request h, String key, int userId, HashMap<String, Object> data) {
            this.h = h;
            this.key = key;
            this.userId = userId;
            this.form = new FormData(data);
        }

        @Override
        public String key() {
            return this.key;
        }

        @Override
        public int user() {
            return this.userId;
        }

        @Override
        public Request request() {
            return this.h;
        }

        @Override
        public FormData form() {
            return this.form;
        }

        @Override
        public void resign(int userId) {
            this.userId = userId;
        }
    }
}
