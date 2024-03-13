package net.fze.util.concurrent;

import net.fze.ext.storage.IStorageProvider;
import net.fze.ext.storage.MemoryStorageProvider;
import org.junit.jupiter.api.Test;

class TrafficLimiterTest {

    @Test
    void acquire() throws InterruptedException {
        IStorageProvider storage = new MemoryStorageProvider();
        TrafficLimiter limiter = new TrafficLimiter(storage,100,20,5);
        for(int i=0;i<100;i++){
            boolean acquire = limiter.acquire("172.17.0.1", 2);
            if(!acquire){
                System.out.println("你已经被限流");
                Thread.sleep(5000);
            }else{
                System.out.println("拿到访问权");
            }
        }
    }
}