package io.ddbm.pc.utils;

import io.ddbm.pc.Event;
import io.ddbm.pc.exception.InterruptException;
import io.ddbm.pc.exception.NoSuchEventException;
import io.ddbm.pc.exception.NoSuchNodeException;
import io.ddbm.pc.exception.NonRunnableException;
import io.ddbm.pc.exception.ParameterException;
import io.ddbm.pc.exception.PauseException;
import io.ddbm.pc.support.FlowContext;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class ExceptionUtils {
    public static List<Class<? extends Throwable>> exceptionTypes = Arrays.asList(InvalidParameterException.class,
        NullPointerException.class, RuntimeException.class, Exception.class, IOException.class,
        InterruptException.class, NoSuchEventException.class, NoSuchNodeException.class, NonRunnableException.class,
        PauseException.class);

    public static Throwable throwExcetion(Class<? extends Throwable> exception, Event event, FlowContext ctx) {
        if (Objects.equals(exception, InvalidParameterException.class)) {
            return null;
        }
        if (Objects.equals(exception, NullPointerException.class)) {
            return new NullPointerException();
        }
        if (Objects.equals(exception, RuntimeException.class)) {
            return new RuntimeException("未预料的异常");
        }
        if (Objects.equals(exception, Exception.class)) {
            return new Exception("未预料的异常");
        }
        if (Objects.equals(exception, IOException.class)) {
            return new IOException("IO异常");
        }
        //        if (Objects.equals(exception, FlowException.class)) {
        //            throw new FlowException("不正确的参数");
        //        }
        if (Objects.equals(exception, InterruptException.class)) {
            return new InterruptException(ctx.getNode(), "中断异常");
        }
        if (Objects.equals(exception, PauseException.class)) {
            return new PauseException(ctx.getFlow().getName(), ctx.getNode(), "暂停异常");
        }
        if (Objects.equals(exception, ParameterException.class)) {
            return new ParameterException(ctx.getFlow().getName(), ctx.getNode(), "参数错误");
        }

        return null;
    }
}
