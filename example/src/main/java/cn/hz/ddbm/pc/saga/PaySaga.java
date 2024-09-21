package cn.hz.ddbm.pc.saga;

import cn.hz.ddbm.pc.newcore.Plugin;
import cn.hz.ddbm.pc.newcore.factory.SAGA;
import cn.hz.ddbm.pc.newcore.saga.SagaAction;
import cn.hz.ddbm.pc.saga.actions.SagaFreezeAction;
import cn.hz.ddbm.pc.saga.actions.SagaPayAction;
import cn.hz.ddbm.pc.saga.actions.SagaSendAction;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PaySaga implements SAGA {

    @Override
    public String flowId() {
        return "test";
    }

    @Override
    public List<Class<? extends SagaAction>> pipeline() {
        return new ArrayList<Class<? extends SagaAction>>() {{
            add(SagaFreezeAction.class);
            add(SagaSendAction.class);
            add(SagaPayAction.class);
        }};

    }

    @Override
    public List<Plugin> plugins() {
        return new ArrayList<Plugin>() {{
//            add(new SagaDigestPlugin());
        }};
    }

}
