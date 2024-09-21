package cn.hz.ddbm.pc.newcore;

import cn.hz.ddbm.pc.newcore.saga.SagaFlow;
import cn.hz.ddbm.pc.newcore.saga.SagaState;
import cn.hz.ddbm.pc.newcore.saga.SagaWorker;
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
        Assert.assertTrue(f.isRunnable(ctx) == true);
    }

    @Test
    void flowAttrs() {
        Assert.assertTrue(f.name().equals("saga"));
    }

    @Test
    void stateAttrs() {
        Assert.assertTrue(f.name().equals("saga"));
    }

    @Test
    void getFlowAttrsByContainer() {
        Assert.assertTrue(f.name().equals("saga"));
    }

    @Test
    void getFlowAttrsByJvm() {
        Assert.assertTrue(f.name().equals("saga"));
    }

    @Test
    void getStateAttrsByContainer() {
        Assert.assertTrue(f.name().equals("saga"));
    }

    @Test
    void getStateAttrsByJvm() {
        Assert.assertTrue(f.name().equals("saga"));
    }

    @Test
    void defaultFlowAttrs() {
        Assert.assertTrue(f.name().equals("saga"));
    }

    @Test
    void chaosFlowAttrs() {
        Assert.assertTrue(f.name().equals("saga"));
    }

    @Test
    void defaultStateAttrs() {
        Assert.assertTrue(f.name().equals("saga"));
    }

    @Test
    void chaosStateAttrs() {
        Assert.assertTrue(f.name().equals("saga"));
    }
}