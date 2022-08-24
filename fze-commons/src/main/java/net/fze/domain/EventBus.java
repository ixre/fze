package net.fze.domain;


import kotlinx.coroutines.GlobalScope;
import net.fze.common.Standard;
import net.fze.util.tuple.Tuple;
import net.fze.util.tuple.Tuple2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * val data1 = Test1Event("message from event1")
 * val data2 = Test2Event("message from event2")
 * <p>
 * EventBus.getDefault().subscribe(Test1Event::class.java){
 * println("--- ${it.name}")
 * }
 * EventBus.getDefault().subscribeAsync(Test2Event::class.java) {
 * println("--- ${it.name}")
 * }
 * EventBus.getDefault().publish(data1);
 * EventBus.getDefault().publish(data2);
 * <p>
 * Thread.sleep(10000)
 */

public class EventBus {
    public interface Handler<T> {
        void Run(T t);
    }

    public interface ExceptionHandler {
        void process(String eventName,Object event,Throwable ex);
    }

    private class EventDispatcher<T> {
        private Map<String, ArrayList<T>> _subMap = new HashMap<>();
        private Object _locker = new Object();

        /**
         * 订阅
         */
        int subscribe(String topic, T h) {
            synchronized (this._locker) {
                if (!this._subMap.containsKey(topic)) {
                    this._subMap.put(topic, new ArrayList<>());
                }
                ArrayList<T> list = this._subMap.get(topic);
                list.add(h);
                return list.size();
            }
        }

        List<T> gets(String topic) {
            if (this._subMap.containsKey(topic)) return this._subMap.get(topic);
            return new ArrayList<>();
        }
    }


    private String name;
    private final static EventBus defaultInstance = new EventBus("default");

    public EventBus(String name) {
        this.name = name;
    }

    public static EventBus getDefault() {
        return defaultInstance;
    }
    private EventDispatcher<Tuple2<Boolean, Handler>> dispatcher = new EventDispatcher<>();
    private ExceptionHandler _exceptHandler;

    /**
     * 订阅事件
     */
    public <T> void subscribe(Class<T> event, Handler<T> h) {
        this.dispatcher.subscribe(event.getName(), Tuple.of(false, h));
    }

    /**
     * 订阅异步事件
     */
    public <T> void subscribeAsync(Class<T> event, Handler<T> h) {
        this.dispatcher.subscribe(event.getName(), Tuple.of(true, h));
    }

    /**
     * 发布事件
     */
    public <T> Error publish(T event) {
        String eventName = event.getClass().getName();
        List<Tuple2<Boolean, Handler>> list = this.dispatcher.gets(eventName);
        try {
            if (list.size() == 0) {
                throw new Exception("No subscribes for class " + eventName + ", please check class is match?");
            }
            list.forEach((it) -> {
                if (it.getItem1()) {
                    Standard.std.coroutines2(() -> {
                        it.getItem2().Run(event);
                    });
                } else {
                    it.getItem2().Run(event);
                }
            });
        } catch (Throwable ex) {
            if (this._exceptHandler != null) this._exceptHandler.process(eventName, event, ex);
            ex.printStackTrace();
            return new Error(ex.getMessage());
        }
        return null;
    }

    /**
     * 异常处理
     */
    public void except(ExceptionHandler h) {
        this._exceptHandler = h;
    }

}
