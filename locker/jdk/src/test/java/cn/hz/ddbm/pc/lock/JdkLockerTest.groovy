package cn.hz.ddbm.pc.lock

import org.junit.Test
import org.junit.Before
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.locks.ReentrantLock
import java.util.function.Function
import static org.mockito.Mockito.*

class JdkLockerTest {
    @InjectMocks
    JdkLocker jdkLocker

    ExecutorService es = Executors.newFixedThreadPool(3);

    @Before
    void setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    void testTryLock() {
        CountDownLatch c = new CountDownLatch(1000)
        1000.times {
            es.submit {
                try {
                    double d = Math.random();
                    jdkLocker.tryLock("key" + d, 1)
                    jdkLocker.releaseLock("key" +d)
                    c.countDown()
                } catch (Exception e) {
                    e.printStackTrace()
                }
            }
        }
        c.await()

    }

    @Test
    void testReleaseLock() {

    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme