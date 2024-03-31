package net.fze.domain.event;

import net.fze.util.Strings;
import net.fze.util.Times;
import net.fze.util.Types;
import net.fze.util.crypto.CryptoUtils;
import net.fze.util.tuple.Tuple;
import net.fze.util.tuple.Tuple2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 事件总线
 * <p>
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
 *
 * @author jarrysix
 */

public class EventBus {
    /**
     * 默认事件总线
     */
    private final static EventBus DEFAULT_INSTANCE = new EventBus("default");
    private final String name;
    private final EventDispatcher<Tuple2<Boolean, EventHandler>> dispatcher = new EventDispatcher<>();
    private ExceptionHandler exceptHandler;

    // 持久化
    private IEventBusStore store = null;

    public EventBus(String name) {
        this.name = name;
    }

    /**
     * 设置自定义事件处理
     * @param store 事件持久化存储
     */
    public void setStore(IEventBusStore store) {
        this.store = store;
    }

    /**
     * 启用文件事件存储
     */
    public void enableFileStore(){
        this.store = new FileEventStore(".",false);
    }

    public static EventBus getDefault() {
        return DEFAULT_INSTANCE;
    }

    public String getName() {
        return name;
    }

    /**
     * 订阅事件
     */
    public <T> void subscribe(Class<T> event, EventHandler<T> h) {
        this.dispatcher.subscribe(event.getCanonicalName(), Tuple.of(false, h));
    }

    /**
     * 订阅异步事件
     */
    public <T> void asyncSubscribe(Class<T> event, EventHandler<T> h) {
        this.dispatcher.subscribe(event.getCanonicalName(), Tuple.of(true, h));
    }

    public static String getEventId(Object eventInstance){
        Class<?> clazz = eventInstance.getClass();
        String eventClassName = clazz.getCanonicalName();
        // 计算事件ID
        String nonce = String.format("%s-%s", eventClassName, Types.toJson(eventInstance));
        return CryptoUtils.shortMd5(nonce).toLowerCase();
    }
    /**
     * 发布事件
     */
    public <T> void publish(T event) {
        Class<?> clazz = event.getClass();
        String eventClassName = event.getClass().getCanonicalName();
        String eventName = clazz.getSimpleName();
        String eventId = getEventId(event);
        this.storeEvent(eventId,eventName, event);
        // 分发事件
        List<Tuple2<Boolean, EventHandler<T>>> list = this.dispatcher.gets(eventClassName);
        try {
            if (list.isEmpty()) {
                throw new Exception("No subscribes for class " + eventClassName + ", please check class is match?");
            }
            list.forEach((it) -> {
                if (it.getItem1()) {
                    new Thread(() -> it.getItem2().handle(event)).start();
                } else {
                    it.getItem2().handle(event);
                }
            });
        } catch (Throwable ex) {
            if (this.exceptHandler != null) {
                this.exceptHandler.process(eventClassName, event, ex);
            }
            this.captureException(eventId,eventName, ex);
            throw new RuntimeException(ex);
        }
    }

    private <T> void storeEvent(String eventId,String eventName, T event) {
        if (this.store != null) {
            this.store.storeEvent(eventId,eventName, event);
        }
    }

    private void captureException(String eventId, String eventName, Throwable ex) {
        if (this.store != null) {
            this.store.captureException(eventId,eventName, ex);
        }
    }

    /**
     * 异常处理
     */
    public void except(ExceptionHandler h) {
        this.exceptHandler = h;
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
        private final Map<String, ArrayList<T>> subMap = new HashMap<>();
        private final Object locker = new Object();

        /**
         * 订阅
         */
        void subscribe(String topic, T h) {
            synchronized (this.locker) {
                if (!this.subMap.containsKey(topic)) {
                    this.subMap.put(topic, new ArrayList<>());
                }
                ArrayList<T> list = this.subMap.get(topic);
                list.add(h);
            }
        }

        <R> List<Tuple2<Boolean, EventHandler<R>>> gets(String topic) {
            if (this.subMap.containsKey(topic)) {
                return (List<Tuple2<Boolean, EventHandler<R>>>) this.subMap.get(topic);
            }
            return new ArrayList<>();
        }
    }

}
