package net.fze.extras.etcd


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