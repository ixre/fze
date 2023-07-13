package net.fze.domain;

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

    private final static EventBus defaultInstance = new EventBus("default");
    private final String _name;
    private final EventDispatcher<Tuple2<Boolean, Handler>> dispatcher = new EventDispatcher<>();
    private ExceptionHandler _exceptHandler;

    public EventBus(String name) {
        this._name = name;
    }

    public static EventBus getDefault() {
        return defaultInstance;
    }

    public String getName() {
        return _name;
    }

    /**
     * 订阅事件
     */
    public <T> void subscribe(Class<T> event, Handler<T> h) {
        this.dispatcher.subscribe(event.getName(), Tuple.of(false, h));
    }

    /**
     * 订阅异步事件
     */
    public <T> void asyncSubscribe(Class<T> event, Handler<T> h) {
        this.dispatcher.subscribe(event.getName(), Tuple.of(true, h));
    }

    /**
     * 发布事件
     */
    public <T> void publish(T event) {
        String eventName = event.getClass().getName();
        List<Tuple2<Boolean, Handler<T>>> list = this.dispatcher.gets(eventName);
        try {
            if (list.isEmpty()) {
                throw new Exception("No subscribes for class " + eventName + ", please check class is match?");
            }
            list.forEach((it) -> {
                if (it.getItem1()) {
                    new Thread(()-> it.getItem2().handle(event)).start();
                } else {
                    it.getItem2().handle(event);
                }
            });
        } catch (Throwable ex) {
            if (this._exceptHandler != null)
                this._exceptHandler.process(eventName, event, ex);
            throw new RuntimeException(ex);
        }
    }

    /**
     * 异常处理
     */
    public void except(ExceptionHandler h) {
        this._exceptHandler = h;
    }

    /**
     * 事件处理程序
     */
    public interface Handler<T> {
        void handle(T t);
    }

    /**
     * 异常处理程序
     */
    public interface ExceptionHandler {
        void process(String eventName, Object event, Throwable ex);
    }

    /**
     * 事件分发
     */
    private static class EventDispatcher<T> {
        private final Map<String, ArrayList<T>> _subMap = new HashMap<>();
        private final Object _locker = new Object();

        /**
         * 订阅
         */
        void subscribe(String topic, T h) {
            synchronized (this._locker) {
                if (!this._subMap.containsKey(topic)) {
                    this._subMap.put(topic, new ArrayList<>());
                }
                ArrayList<T> list = this._subMap.get(topic);
                list.add(h);
            }
        }

       <R> List<Tuple2<Boolean, Handler<R>>> gets(String topic) {
            if (this._subMap.containsKey(topic)) {
                return (List<Tuple2<Boolean, Handler<R>>>)this._subMap.get(topic);
            }
            return new ArrayList<>();
        }
    }

}
