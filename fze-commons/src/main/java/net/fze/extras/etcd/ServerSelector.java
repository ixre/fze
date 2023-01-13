/**
 * Copyright (C) 2007-2020 56X.NET,All rights reserved.
 * <p>
 * name : ServerSelector.kt.bak
 * author : jarrysix (jarrysix#gmail.com)
 * date : 2020-10-17 08:16
 * description :
 * history :
 */
package net.fze.extras.etcd;

import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.kv.GetResponse;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.WatchOption;
import io.etcd.jetcd.watch.WatchEvent;
import net.fze.util.TypeConv;
import net.fze.util.Types;

import java.util.Random;
import java.util.Vector;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static java.lang.Thread.sleep;


/**
 * 服务KEY:/registry/server/Go2oService
 * 节点KEY:/registry/server/Go2oService/1
 */
public class ServerSelector implements ISelector {

    private final String name;
    private final Client client;
    private final GetOption option;
    private final ByteSequence prefix;
    SelectorAlgorithm alg = SelectorAlgorithm.Random;
    private Vector<Node> nodes = new Vector<>();
    private int last = -1;

    public ServerSelector(String name, Client client) throws ExecutionException, InterruptedException, TimeoutException {
        this.name = name;
        this.client = client;
        this.prefix = ByteSequence.from(name.getBytes());
        this.option = GetOption.newBuilder()
                .withSerializable(true)
                .withSortField(GetOption.SortTarget.KEY)
                .isPrefix(true)
                .withPrefix(this.prefix)
                .build();
        this.loadNodes();
        this.watch();
    }

    /**
     * 设置算法
     *
     * @param alg 算法
     */
    public void setAlg(SelectorAlgorithm alg) {
        this.alg = alg;
    }

    //　监听变化,并动态处理节点
    private void watch() {
        WatchOption opt = WatchOption.newBuilder()
                .isPrefix(true)
                .withPrefix(this.prefix)
                .build();

        this.client.getWatchClient().watch(this.prefix, opt, (it) -> {
            for (WatchEvent e : it.getEvents()) {
                try {
                    this.processWatchEvent(e);
                } catch (Throwable ex) {
                    System.out.println(
                            "[ Etcd][ Error]: watch event "
                                    + e.getEventType().toString()
                                    + " error:" + ex.getMessage()
                    );
                }
            }
        });
    }

    private void processWatchEvent(WatchEvent e) {
        // 发现新的服务
        if (e.getEventType() == WatchEvent.EventType.PUT) {
            Node node = this.parseNode(e.getKeyValue().getValue());
            this.addNode(node);
            return;
        }
        // 移出服务
        if (e.getEventType() == WatchEvent.EventType.DELETE) {
            String key = String.valueOf(e.getKeyValue().getKey().getBytes());
            String[] keyArray = key.split("/");
            if (keyArray.length == 0) {
                System.out.println("[ Etcd][ Event]: delete node key is empty");
            } else {
                long nodeId = TypeConv.toLong(keyArray[keyArray.length - 1]);
                this.deleteNode(nodeId);
            }
            return;
        }
    }

    @Override
    public Node next() throws InterruptedException {
        int l = this.nodes.size();
        int retryTimes = 0;
        while (l == 0) {
            sleep(1000);
            l = this.nodes.size();
            if (retryTimes++ > 5) break;
        }
        if (l == 0) throw new InterruptedException("no found any node on " + this.name);
        if (this.alg == SelectorAlgorithm.RoundRobin) {
            this.last += 1;
            if (this.last >= l) {
                this.last = 0;
            }
            return this.nodes.get(this.last);
        }
        // 随机算法
        //i := rand.Int() % len(s.nodes)
        int i = new Random(System.nanoTime()).nextInt(l);
        return this.nodes.get(i);
    }

    private void deleteNode(Long id) {
        Vector<Node> list = new Vector<Node>();
        for (Node v : this.nodes) {
            if (v.getId() != id) {
                list.add(v);
            }
        }
        this.nodes = list;
    }

    private void addNode(Node node) {
        boolean exist = false;
        for (Node v : this.nodes) {
            if (v.getId() == node.getId()) {
                exist = true;
            }
        }
        if (!exist) this.nodes.add(node);
    }

    private void loadNodes() throws TimeoutException, ExecutionException, InterruptedException {
        try {
            CompletableFuture<GetResponse> rsp = this.client.getKVClient().get(this.prefix, this.option);
            rsp.get(10000, TimeUnit.MILLISECONDS).getKvs().forEach((it) -> {
                Node node = this.parseNode(it.getValue());
                this.nodes.add(node);
            });
        } catch (Throwable ex) {
            if (ex instanceof TimeoutException) {
                System.out.println("[ Etcd][ Error]: load nodes timeout in 10s");
                throw new TimeoutException("load nodes timeout in 10s");
            }
            System.out.println("[ Etcd][ Error]: load nodes failed! error : " + ex.getMessage());
            throw ex;
        }
    }

    private Node parseNode(ByteSequence value) {
        return Types.fromJson(String.valueOf(value.getBytes()), Node.class);
    }
}
