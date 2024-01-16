package net.fze.ext.sse;

import net.fze.util.Strings;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Server-sent-events服务器
 * 参考：<a href="https://github.com/mariomac/jeasse/blob/master/jeasse-servlet3/src/main/java/info/macias/sse/servlet3/ServletEventTarget.java">...</a>
 */
public class ServerEventServer {
    /**
     * 超时时间,0表示不过期。超时后服务端会主动关闭连接，客户端收到事件后可以重复发起
     */
    private int _timeout;

    /**
     * 订阅列表，存储所有主题的订阅请求，每个topic对应一个ArrayList，ArrayList里该topic的所有订阅请求,
     * 应使用CopyOnWriteArrayList，为遇到并发问题
     */
    private final ConcurrentHashMap<String, CopyOnWriteArrayList<AsyncContext>> contextMaps = new ConcurrentHashMap<>();

    /**
     * 上下文监听
     */
    private AsyncListener _listener = null;

    //Logger logger = LoggerFactory.getLogger(ServerEventServer.class);

    public ServerEventServer() {
        this._timeout = 3600 * 1000;
    }

    public ServerEventServer(AsyncListener listener) {
        this();
        this._listener = listener;
    }

    public ServerEventServer(int timeout, AsyncListener listener) {
        this(listener);
        this._timeout = timeout;
    }

    /**
     * 创建SSE连接
     */
    public AsyncContext connect(String topic, HttpServletRequest request) {
        if (Strings.isNullOrEmpty(topic)) {
            throw new RuntimeException("SSE topic can not be empty");
        }

        //支持异步响应,异步这个概念很多地方都有，就像处理文件时，不是一直等待文件读完，而是让它去读，cpu做其它事情，读完通知cpu来处理即可。
        AsyncContext ctx = request.startAsync();
        ctx.setTimeout(this._timeout);

        HttpServletResponse response = (HttpServletResponse) ctx.getResponse();
        //response.setStatus(200);
        //设置响应头ContentType
        response.setContentType("text/event-stream");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Connection", "keep-alive");
        response.setHeader("X-Accel-Buffering", "no");
        //设置响应编码类型
        response.setCharacterEncoding("utf-8");

        // 模拟发送
//        try {
//            PrintWriter pw = response.getWriter();
//            while (true) {
//                if (pw.checkError()) {
//                    System.out.println("客户端断开连接");
//                    break;
//                }
//                Thread.sleep(1000);
//                pw.write("data:行情:" + Math.random() + "\n\n");
//                pw.flush();
//            }
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }

        this.addListener(ctx, topic);
        List<AsyncContext> ctxList = contextMaps.computeIfAbsent(topic, k -> new CopyOnWriteArrayList<>());
        ctxList.add(ctx);
        return ctx;
    }

    private void addListener(AsyncContext ctx, String topic) {
        if (this._listener != null) {
            ctx.addListener(this._listener);
        }
        ctx.addListener(new AsyncListener() {
            /**
             * 移除上下文
             * @param event 异步事件
             */
            private void removeContext(AsyncEvent event) {
                AsyncContext ctx = event.getAsyncContext();
                List<AsyncContext> asyncContexts = contextMaps.get(topic);
                if (asyncContexts != null) {
                    // 移除会话
                    asyncContexts.remove(ctx);
                    if (asyncContexts.isEmpty()) {
                        // 如果主题不包含会话，则删除主题
                        contextMaps.remove(topic);
                    }
                }
            }

            @Override
            public void onComplete(AsyncEvent event) throws IOException {
            }

            @Override
            public void onTimeout(AsyncEvent event) throws IOException {
                removeContext(event);
            }

            @Override
            public void onError(AsyncEvent event) throws IOException {
                removeContext(event);
            }

            @Override
            public void onStartAsync(AsyncEvent event) throws IOException {
                // 当连接建立，线程启动后执行
//                if (handler != null) {
//                    handler.handle(event);
//                }
            }
        });
    }

    /**
     * 推送消息
     *
     * @param topic 主题
     * @param data  数据
     */
    public void push(String topic, String data) {
        this.push(topic, data, null);
    }

    /**
     * 推送消息
     *
     * @param topic  主题
     * @param data   数据
     * @param filter 筛选符合条件的上下文进行推送
     */
    public void push(String topic, String data, IAsyncContextFilter filter) {
        List<AsyncContext> ctxList = contextMaps.get(topic);

        if (ctxList.isEmpty()) return;
        for (AsyncContext ctx : ctxList) {
            // java.util.ConcurrentModificationException: null
            if (ctx == null || (filter != null && !filter.filter(ctx))) {
                //logger.error("开始获取会话1" + (ctxList.isEmpty()) + ";" + ctxList.size());
                // 过滤请求
                continue;
            }
            try {
                // logger.error("开始获取会话2" + (ctxList.isEmpty()) + ";" + ctxList.size());
                PrintWriter out = ctx.getResponse().getWriter();
                out.print(data);
                out.print("\n\n"); // an empty line dispatches the event 结束发送
                out.flush();
            } catch (Throwable e) {
                //e.printStackTrace();
                //java.lang.IllegalStateException: The request associated with the AsyncContext has already completed processing.
                //throw new RuntimeException(e);
            }
        }
    }

    /**
     * 获取主题的所有连接
     *
     * @param topic 主题
     * @return 连接
     */
    public List<AsyncContext> getContexts(String topic) {
        List<AsyncContext> list = new ArrayList<>();
        List<AsyncContext> srcList = this.contextMaps.get(topic);
        if (srcList != null) {
            Collections.copy(list, srcList);
        }
        return list;
    }
}