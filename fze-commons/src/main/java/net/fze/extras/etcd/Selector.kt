/**
 * Copyright (C) 2007-2020 56X.NET,All rights reserved.
 *
 * name : Selector.kt
 * author : jarrysix (jarrysix#gmail.com)
 * date : 2020-10-17 08:16
 * description :
 * history :
 */
package net.fze.extras.etcd

import io.etcd.jetcd.ByteSequence
import io.etcd.jetcd.Client
import io.etcd.jetcd.options.GetOption
import io.etcd.jetcd.options.WatchOption
import io.etcd.jetcd.watch.WatchEvent
import net.fze.util.Types
import java.lang.Thread.sleep
import java.util.*

class Node {
    var id = 0L
    var addr = ""
}

enum class SelectorAlgorithm(val value: Int) {
    // 随机
    Random(0),

    // 轮询
    RoundRobin(1),
}

interface ISelector {
    fun next(): Node
}

/**
 * 服务KEY:/registry/server/Go2oService
 * 节点KEY:/registry/server/Go2oService/1
 */
class ServerSelector(var name: String, client: Client) : ISelector {
    private var option: GetOption = GetOption.DEFAULT
    private var cli: Client? = client
    private var nodes: Vector<Node> = Vector()
    var alg: SelectorAlgorithm = SelectorAlgorithm.Random
    private var last: Int = -1
    private var prefix: ByteSequence = ByteSequence.EMPTY

    init {
        this.prefix = ByteSequence.from(name.toByteArray())
        this.option = GetOption.newBuilder()
            .withSerializable(true)
            .withSortField(GetOption.SortTarget.KEY)
            .withPrefix(this.prefix)
            .build()
        this.loadNodes()
        this.watch()
    }

    //　监听变化,并动态处理节点
    private fun watch() {
        val opt = WatchOption.newBuilder()
            .withPrefix(this.prefix)
            .build()
        this.cli!!.watchClient.watch(this.prefix, opt) {
            for (e in it.events) {
                try {
                    this.processWatchEvent(e)
                } catch (ex: Throwable) {
                    println(
                        "[ Etcd][ Error]: watch event "
                                + e.eventType.toString()
                                + " error:" + ex.message
                    )
                }
            }
        }
    }

    private fun processWatchEvent(e: WatchEvent) = when (e.eventType) {
        WatchEvent.EventType.PUT -> {
            val node = this.parseNode(e.keyValue.value)
            this.addNode(node)
        }
        WatchEvent.EventType.DELETE -> {
            val key = String(e.keyValue.key.bytes)
            val keyArray = key.split("/")
            if (keyArray.isEmpty()) {
                println("[ Etcd][ Event]: delete node key is empty")
            } else {
                val nodeId = keyArray.last().toLong()
                this.deleteNode(nodeId)
            }
        }
        else -> {
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
        if (l == 0) throw Exception("no nodes found on " + this.name)
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

    private fun deleteNode(id: Long) {
        val list = Vector<Node>()
        for (v in this.nodes) {
            if (v.id != id) {
                list.add(v)
            }
        }
        this.nodes = list
    }

    private fun addNode(node: Node) {
        var exist = false
        for (v in this.nodes) {
            if (v.id == node.id) {
                exist = true
            }
        }
        if (!exist) this.nodes.add(node)
    }

    private fun loadNodes() = try {
        val rsp = this.cli!!.kvClient.get(this.prefix, this.option)
        rsp.get().kvs.forEach {
            val node = this.parseNode(it.value)
            this.nodes.add(node)
        }
    } catch (ex: Throwable) {
        println("[ Etcd][ Error]: load nodes failed! error : " + ex.message)
    }

    private fun parseNode(value: ByteSequence): Node {
        return Types.fromJson(String(value.bytes), Node::class.java)
    }
}
