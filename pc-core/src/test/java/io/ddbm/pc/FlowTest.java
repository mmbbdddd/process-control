package io.ddbm.pc;

import com.google.common.collect.Lists;
import io.ddbm.pc.chaos.ChaosFlowRequest;
import io.ddbm.pc.exception.ContextCreateException;
import io.ddbm.pc.exception.NoSuchEventException;
import io.ddbm.pc.exception.NoSuchNodeException;
import io.ddbm.pc.exception.ParameterException;
import io.ddbm.pc.exception.PauseException;
import io.ddbm.pc.exception.SessionException;
import io.ddbm.pc.factory.FlowFactory;
import io.ddbm.pc.plugins.DevModeLogPlugin;
import io.ddbm.pc.status.StatusException;
import io.ddbm.pc.utils.SnowFlake;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.HashMap;


public class FlowTest {
    ProcessControlService flowService;

    @Before
    public void before() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(TestFlowConfiguration.class);
        ctx.refresh();
        flowService = ctx.getBean(ProcessControlService.class);
    }

    @Test
    public void execute() {
        try {
            FlowFactory.get("test").setPlugins(Lists.newArrayList(new DevModeLogPlugin()));
            for (int i = 0; i < 1; i++) {
                flowService.executeFluent(new TestFlowRequest("test", null, "123", null));
            }
        } catch (PauseException e) {
            e.printStackTrace();
        } catch (ContextCreateException e) {
            throw new RuntimeException(e);
        } catch (ParameterException e) {
            throw new RuntimeException(e);
        } catch (StatusException e) {
            throw new RuntimeException(e);
        } catch (SessionException e) {
            throw new RuntimeException(e);
        } catch (NoSuchNodeException e) {
            throw new RuntimeException(e);
        } catch (NoSuchEventException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void test() {
        try {
            SnowFlake snowFlake = new SnowFlake(2, 5);
            flowService.chaos(1, new ChaosFlowRequest("test", snowFlake.nextId(), new HashMap<>()),
                Lists.newArrayList(new DevModeLogPlugin()));
        } catch (Exception e) {
        }

    }

    @Test
    public void chaos() {
        try {
            SnowFlake snowFlake = new SnowFlake(2, 5);
            flowService.chaos(100, new ChaosFlowRequest("test", snowFlake.nextId(), new HashMap<>()));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void nodeOf() {
    }
}