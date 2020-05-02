package com.github.ixre.fze.commons.std.api;

import java.util.Map;

// 工厂生成器
public interface FactoryBuilder {
    // 生成下文工厂
    ContextFactory Build(Map<String, Object> registry);
}
