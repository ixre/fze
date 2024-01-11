package net.fze.ext.sse;

import javax.servlet.AsyncEvent;

public interface IAsyncEventHandler {
    void handle(AsyncEvent event);
}
