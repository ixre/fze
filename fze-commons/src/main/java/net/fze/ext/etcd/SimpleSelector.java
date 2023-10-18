package net.fze.ext.etcd;

import net.fze.util.TypeConv;

import java.util.Arrays;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;

public class SimpleSelector implements ISelector {
    private String[] servers;
    private Vector<Node> nodes = new Vector();
    private SelectorAlgorithm alg = SelectorAlgorithm.Random;
    private int last = -1;


    public SimpleSelector(String[] servers) {
        this.servers = servers;
        AtomicInteger index = new AtomicInteger();
        Arrays.stream(servers).forEachOrdered((addr) -> {
            long id = TypeConv.toLong(index.getAndIncrement());
            Node node = new Node(id, addr);
            this.nodes.add(node);
        });
    }

    /**
     * 设置算法
     *
     * @param alg
     */
    public void setAlg(SelectorAlgorithm alg) {
        this.alg = alg;
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
        if (l == 0) throw new InterruptedException("not found nodes");
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
}