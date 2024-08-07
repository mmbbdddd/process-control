package hz.ddbm.pc.pay;

import hz.ddbm.pc.core.config.Coast;
import hz.ddbm.pc.core.factory.fsm.Fsm;
import hz.ddbm.pc.core.fsm.core.Flow;
import hz.ddbm.pc.core.fsm.core.Node;
import hz.ddbm.pc.core.fsm.core.Router;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class PayFsm extends Fsm {
    //    定义工作流
    @Override
    public Flow flow() {
        return new FlowBuilder("testFlow", null, null) {
            {
                addNode(new NodeBuilder(Node.Type.START, "init") {{
                    onEvent(Coast.EVENT_PUSH, "payAction", "any://payRouter", "pay_unknow", "true");
                }});
                addNode(new NodeBuilder(Node.Type.TASK, "pay_unknow") {{
                    onEvent(Coast.EVENT_PUSH, "payQueryAction", "any://payRouter");
                }});
                addNode(new NodeBuilder(Node.Type.END, "su"));
                addNode(new NodeBuilder(Node.Type.END, "fail"));

                addRouter(payRouter());
                addPlugin("logPlugin");
            }
//            回忆

        }.build();
    }

    @Override
    public Map<Serializable, String> statusMapping() {
        return flow().nodeNames().stream().collect(Collectors.toMap(k -> k, k -> k));
    }

    //定义路由
    private Router payRouter() {
        return new Router.Any("payRouter", new HashMap<String, String>() {{
            put("su", "null != actionResult && actionResult.code == '0000'");
            put("fail", "null != actionResult &&  actionResult.code == '0001'");
            put("pay_unknow", "true");
        }});
    }

}
