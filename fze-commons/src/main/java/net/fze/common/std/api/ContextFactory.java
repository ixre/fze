package net.fze.common.std.api;

import java.util.HashMap;
import java.util.Map;


// 上下文工厂
public interface ContextFactory {
    static ContextFactory build(Map<String, Object> registry) {
        defaultContextFactory f = new defaultContextFactory();
        f.registry = registry;
        return f;
    }

    Context Factory(Request h, String key, int userId);

    class defaultContextFactory implements ContextFactory {
        Map<String, Object> registry;
        boolean trace;

        void setTrace(boolean trace) {
            this.trace = trace;
        }

        @Override
        public Context Factory(Request h, String key, int userId) {
            Context ctx = new Context.DefaultContextImpl(h, key, userId, new HashMap<>());
            if (this.registry != null) {
                this.registry.forEach((k, v) -> ctx.form().set(k, v));
            }
            if (h != null) {
                ctx.form().set("$user_addr", h.getRemoteAddr());
                ctx.form().set("$user_agent", h.getHeader("user-Agent"));
            }
            return ctx;
        }
    }
}
