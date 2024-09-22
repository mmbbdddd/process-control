package cn.hz.ddbm.pc.newcore;

import cn.hz.ddbm.pc.newcore.saga.SagaFlow;
import cn.hz.ddbm.pc.newcore.saga.SagaState;
import cn.hz.ddbm.pc.newcore.saga.SagaWorker;
import cn.hz.ddbm.pc.newcore.utils.EnvUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ChaosBaseFlowTest {
    SagaFlow f = SagaFlow.of("saga");

    @Test
    void name() {
        Assertions.assertTrue(f.name().equals("saga"));
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
        Assertions.assertTrue(f.isRunnable(ctx) == false);
    }

    @Test
    void flowAttrs() {
        EnvUtils.setChaosMode(true);
        Assertions.assertTrue(f.flowAttrs().equals(FlowAttrs.chaosOf()));
    }

    @Test
    void stateAttrs() {
        EnvUtils.setChaosMode(true);
        SagaState state = new SagaState(0, SagaWorker.Offset.task);
        EnvUtils.setChaosMode(true);
        Assertions.assertTrue(f.stateAttrs(state).equals(StateAttrs.ChaosOf()));
    }
}
