package net.fze.ext.nats;

import io.nats.client.*;

import java.io.IOException;
import java.time.Duration;

/**
 * Nats客户端
 *
 * @author jarrysix
 */
public class NatsClient {
    /**
     * 创建连接
     *
     * @param address 地址
     * @return 连接
     */
    public Connection createConnection(String address) throws IOException, InterruptedException {
        Options.Builder builder = new Options.Builder()
                .server("nats://$address")
                .pingInterval(Duration.ofSeconds(10))
                .maxReconnects(-1)
                .errorListener(new ErrorListener() {
                    @Override
                    public void errorOccurred(Connection conn, String error) {
                        ErrorListener.super.errorOccurred(conn, error);
                    }
                })
                .connectionListener(new ConnectionListener() {
                    @Override
                    public void connectionEvent(Connection conn, Events type) {
                        System.out.println("[ App][ Mq]: " + type.toString());
                    }
                });
        return Nats.connect(builder.build());
    }
}
