package net.fze.extras.etcd

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
}
