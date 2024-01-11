package net.fze.ext.sse;

public class EventMessage {
    /**
     * 数据
     */
    private final String data;
    /**
     * 事件,默认是onmessage事件，可以通过
     */
    private final String event;
    /**
     * 短连接轮训时间
     */
    private final int retry;

    /**
     * Last-Event-Id
     */
    private final String id;

    public EventMessage(String id, String data, int retry) {
        this.id = id;
        this.event = null;
        this.retry = retry;
        this.data = data;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (id != null) {
            sb.append("id: ").append(id.replace("\n", "")).append('\n');
        }

        if (retry > 0) {
            sb.append("retry: ").append(retry).append("\n");
        }
        if (data != null) {
            for (String s : data.split("\n")) {
                sb.append("data: ").append(s).append("\n");
            }
        } else {
            if (event != null) {
                sb.append("event: ").append(event.replace("\n", "")).append("\n");
            }
        }
        // an empty line dispatches the event
        sb.append("\n");
        return sb.toString();
    }
}
