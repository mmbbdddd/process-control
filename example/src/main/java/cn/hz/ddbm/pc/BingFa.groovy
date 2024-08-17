package cn.hz.ddbm.pc

import cn.hz.ddbm.pc.core.router.ExpressionRouter
import org.junit.Test

import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.locks.ReentrantLock

class BingFa {
    ExecutorService es = Executors.newFixedThreadPool(1)
    @Test
    public void parIo(){
        List<Integer> list = new ArrayList<>();
        for(int i = 0;i<1000000;i++){
            list.add(i)
        }
        ReentrantLock lock = new ReentrantLock();
        list.parallelStream().forEach { CompletableFuture.runAsync (new Runnable() {
            @Override
            void run() {
                mockCallRedis(lock,it)
            }
        },es).whenComplete {println(it)}}
    }


    void mockCallRedis(ReentrantLock lock,int i) {

        try {
            lock.lock()
            //
            Thread.sleep(10)
            if(Math.random()<0.001){
                throw new RuntimeException("integer:"+i)
            }
        }catch (Exception e){
            e.printStackTrace()
        }

    }
}
