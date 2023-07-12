package net.fze.ext.etcd;

public interface ISelector {
    Node next() throws InterruptedException;
}
