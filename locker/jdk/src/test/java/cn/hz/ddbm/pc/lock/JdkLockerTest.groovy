package cn.hz.ddbm.pc.lock

import org.junit.Test
import org.junit.Before
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock
import java.util.function.Function
import static org.mockito.Mockito.*

class JdkLockerTest {
    @Mock
    ConcurrentHashMap<String, ReentrantLock> locks
    @InjectMocks
    JdkLocker jdkLocker

    @Before
    void setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    void testTryLock() {
        when(locks.computeIfAbsent(anyString(), any(Function < ? super String, ? extends ReentrantLock >.class ) ) ).thenReturn(new ReentrantLock(true))
        when(locks.computeIfAbsent(anyString(), any(Function < ? super String, ? extends ReentrantLock >.class ) ) ).thenReturn(new ReentrantLock(true))

        jdkLocker.tryLock("key", 1)


        jdkLocker.releaseLock("key")
    }

    @Test
    void testReleaseLock() {

    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme