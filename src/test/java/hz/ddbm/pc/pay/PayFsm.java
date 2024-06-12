package hz.ddbm.pc.pay;

import hz.ddbm.pc.core.config.Coast;
import hz.ddbm.pc.core.domain.*;
import hz.ddbm.pc.core.factory.fsm.Fsm;

import java.util.HashMap;

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
//定义路由
    private Router payRouter() {
        return new Router.Any("payRouter", new HashMap<String, String>() {{
            put("su", "null != actionResult && actionResult.code == '0000'");
            put("fail", "null != actionResult &&  actionResult.code == '0001'");
            put("pay_unknow", "true");
        }});
    }

}
