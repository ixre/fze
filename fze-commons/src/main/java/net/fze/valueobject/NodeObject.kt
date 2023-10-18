package net.fze.valueobject

/** 节点对象　*/
class NodeObject {
    var id: String = ""
    var label: String = ""

    /** 是否为叶子,用于懒加载 */
    var isLeaf: Boolean = false
    var children: List<NodeObject> = listOf()
}

interface INodeObject {
    fun getParent(): String = ""
    fun get(): NodeObject?
}
