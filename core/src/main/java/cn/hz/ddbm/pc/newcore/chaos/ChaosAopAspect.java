package cn.hz.ddbm.pc.newcore.chaos;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import javax.annotation.Resource;

/**
 * 向组件注入混沌
 * 1，action
 * 2，锁
 * 3，会话
 * 4，状态
 * 5，事件溯源
 */
@Aspect
public class ChaosAopAspect {

    static ChaosConfig chaosConfig;

    @Around("execution(* cn.hz.ddbm.pc.newcore.infra.LockManager.*(..))")
    public Object locker(ProceedingJoinPoint pjp) throws Throwable {
        chaosConfig.infraChaos();
        return pjp.proceed();
    }

    @Around("execution(* cn.hz.ddbm.pc.newcore.infra.SessionManager.*(..))")
    public Object session(ProceedingJoinPoint pjp) throws Throwable {
        chaosConfig.infraChaos();
        return pjp.proceed();
    }

    @Around("execution(* cn.hz.ddbm.pc.newcore.infra.StatusManager.*(..))")
    public Object status(ProceedingJoinPoint pjp) throws Throwable {
        chaosConfig.infraChaos();
        return pjp.proceed();
    }
}
