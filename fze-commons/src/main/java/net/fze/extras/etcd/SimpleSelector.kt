package net.fze.extras.etcd

import java.lang.Thread.sleep
import java.util.*

class SimpleSelector(var servers :Array<String>) : ISelector {
    private var nodes: Vector<Node> = Vector()
    var alg: SelectorAlgorithm = SelectorAlgorithm.Random
    private var last: Int = -1

    init{
        servers.forEachIndexed { index, it ->
            val node = Node()
            node.id = index.toLong()
            node.addr = it
            this.nodes.add(node)
        }
    }

    override fun next(): Node {
        var l = this.nodes.size
        var retryTimes = 0;
        while (l == 0) {
            sleep(1000)
            l = this.nodes.size
            if (retryTimes++ > 5) break;
        }
        if (l == 0) throw Exception("not found nodes")
        if (this.alg == SelectorAlgorithm.RoundRobin) {
            this.last += 1
            if (this.last >= l) {
                this.last = 0
            }
            return this.nodes[this.last]
        }
        // 随机算法
        //i := rand.Int() % len(s.nodes)
        val i = Random(System.nanoTime()).nextInt(l)
        return this.nodes[i]
    }
}