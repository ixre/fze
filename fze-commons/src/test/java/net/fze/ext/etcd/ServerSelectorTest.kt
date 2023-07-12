package net.fze.ext.etcd

import io.etcd.jetcd.ByteSequence
import io.etcd.jetcd.Client
import org.junit.jupiter.api.Test

internal class ServerSelectorTest {

    private val service = "Go2oService"
    val ttl = 3
    private val client = Client.builder()
        .endpoints("http://localhost:2379")
        //.user(ByteSequence.EMPTY)
        //.password(ByteSequence.EMPTY)
        .build()


    @Test
    operator fun next() {
        val s = ServerSelector(
            "/registry/server/" + this.service,
            this.client
        )
        s.alg = SelectorAlgorithm.RoundRobin
        for (i in 0..30) {
            println(s.next().addr)
            Thread.sleep(5000)
        }
    }

    @Test
    fun testParseNode() {
        val s = "{\"id\":3900579538,\"addr\":\"192.168.0.107:1427\"}"
        val seq = ByteSequence.from(s.toByteArray())
        val now = String(seq.bytes)
        println(now)
        // Types.fromJson(value.toString(), Node::class.java)
    }

    @Test
    fun testGetNode() {
        var etcdServer = System.getenv("GO2O_ETCD_ADDR")
        if (etcdServer.isNullOrEmpty()) etcdServer = "localhost:2379"
        //var arr = rpcServer.split(":")
        // Go2oGrpcFactory.configure(, arr[1].toInt(), true)
        println("[ App][ Info]: etcd server :$etcdServer")
        val client = Client.builder()
            .endpoints("http://${etcdServer}")
            //.user(ByteSequence.EMPTY)
            //.password(ByteSequence.EMPTY)
            .build()
        val s = ServerSelector("/registry/server/Go2oService", client)
        s.alg = SelectorAlgorithm.RoundRobin
        println(s.next().addr)
    }
}
