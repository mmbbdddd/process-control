package cn.hz.ddbm.pc.container;

import cn.hz.ddbm.pc.container.chaos.ChaosHandler;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;

@Aspect
public class ChaosAspect {
    @Resource
    ChaosHandler chaosHandler;

    @Around("execution(* cn.hz.ddbm.pc.core.Action.action(*))")
    public Object action(ProceedingJoinPoint pjp) throws Throwable {
        Object   target = pjp.getTarget();
        Method   method = null;
        Object[] args   = null;
        chaosHandler.handle(target, method, args);
        return pjp.proceed();
    }

    @Around("within(cn.hz.ddbm.pc.core.Router)")
    public Object router(ProceedingJoinPoint pjp) throws Throwable {
        return pjp.proceed();
    }

    @Around("within(cn.hz.ddbm.pc.core.support.Locker)")
    public Object locker(ProceedingJoinPoint pjp) throws Throwable {
        return pjp.proceed();
    }

    @Around("within(cn.hz.ddbm.pc.core.support.SessionManager)")
    public Object session(ProceedingJoinPoint pjp) throws Throwable {
        return pjp.proceed();
    }

    @Around("within(cn.hz.ddbm.pc.core.support.StatusManager)")
    public Object status(ProceedingJoinPoint pjp) throws Throwable {
        return pjp.proceed();
    }
}
