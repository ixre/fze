package net.fze.extras.etcd;

public interface ISelector {
    Node next() throws InterruptedException;
}
