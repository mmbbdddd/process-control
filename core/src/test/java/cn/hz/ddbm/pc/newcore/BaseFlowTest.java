package cn.hz.ddbm.pc.newcore;

import cn.hz.ddbm.pc.newcore.saga.SagaFlow;
import cn.hz.ddbm.pc.newcore.saga.SagaState;
import cn.hz.ddbm.pc.newcore.saga.SagaWorker;
import cn.hz.ddbm.pc.newcore.utils.EnvUtils;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

class BaseFlowTest {
    SagaFlow f = SagaFlow.of("saga");

    @Test
    void name() {
        Assert.assertTrue(f.name().equals("saga"));
    }

    @Test
    void execute() {
    }

    @Test
    void isRunnable() {

        FlowContext ctx = new FlowContext(f, new Payload<SagaState>() {
            SagaState s = new SagaState(0, SagaWorker.Offset.task);

            @Override
            public String getId() {
                return "1";
            }

            @Override
            public SagaState getState() {
                return s;
            }

            @Override
            public void setState(SagaState state) {
                this.s = state;
            }
        });
        Assert.assertTrue(f.isRunnable(ctx) == false);
    }

    @Test
    void flowAttrs() {
        Assert.assertTrue(f.flowAttrs().equals(FlowAttrs.defaultOf()));
        EnvUtils.setChaosMode(true);
        Assert.assertTrue(f.flowAttrs().equals(FlowAttrs.chaosOf()));
        EnvUtils.setChaosMode(false);
    }

    @Test
    void stateAttrs() {
        EnvUtils.setChaosMode(false);
        SagaState state = new SagaState(0, SagaWorker.Offset.task);
        //junit是多线程执行测试的。 会导致这里错误，单个执行没问题。
        Assert.assertTrue(f.stateAttrs(state).equals(StateAttrs.defaultOf()));
        EnvUtils.setChaosMode(true);
        Assert.assertTrue(f.stateAttrs(state).equals(StateAttrs.ChaosOf()));
        EnvUtils.setChaosMode(false);
    }
}
