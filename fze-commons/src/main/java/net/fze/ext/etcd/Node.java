package net.fze.ext.etcd;

/** 节点 */
public class Node {
    private final long id;
    private final String addr;

    public Node(long id,String addr){
        this.id = id;
        this.addr = addr;
    }
    public long getId() {
        return id;
    }

    public String getAddr() {
        return addr;
    }
}
