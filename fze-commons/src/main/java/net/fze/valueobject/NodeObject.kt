package net.fze.valueobject

/** 节点对象　*/
class NodeObject {
    var id: String = ""
    var label: String = ""
    var children: List<NodeObject> = listOf()
}

interface INodeObject{
    fun getParent():String = ""
    fun get():NodeObject?
}