package net.fze.mock;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import net.fze.common.LangExtension;
import net.fze.util.Types;

import java.util.HashMap;
import java.util.Map;

public abstract class MockCase {
    private Injector _injector;
    private Map<String, Object> _data = new HashMap<>();
    private LangExtension _lang = new LangExtension();

    public MockCase(String confPath) {
        if (confPath.equals("")) {
            confPath = "../conf";
        }
        this._injector = Guice.createInjector(this.module());
        this.configure(confPath, this._injector);
        MockApp.initialize(confPath);
        MockApp.setInjector(this._injector);
    }

    public abstract Module module();

    /**
     * 获取服务
     *
     * @param c   服务类型
     * @param <T> 服务
     * @return 服务
     */
    public <T> T getInstance(Class<T> c) {
        return _injector.getInstance(c);
    }

    /**
     * 获取变量
     */
    public Object getVar(String key) {
        if (this._data.containsKey(key)) {
            return this._data.get(key);
        }
        return null;
    }

    /**
     * 存储变量
     *
     * @param key 键
     * @param o   变量
     */
    public void putVar(String key, Object o) {
        this._data.put(key, o);
    }

    public String toJson(Object o) {
        return Types.toJson(o);
    }

    public <T> T fromJson(String data, Class<T> c) {
        return Types.fromJson(data, c);
    }

    public LangExtension lang() {
        return this._lang;
    }

    public abstract void configure(String confPath, Injector ioc);
}
