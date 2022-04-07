package io.ddbm.pc;


import org.junit.Before;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class FlowTest {
    Flow                               flow;
    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();

    @Before
    public void setup() throws Exception {
        ctx.register(BeanConfig.class);
        ctx.refresh();
        LinkedHashMap<String, String> payRouter = new LinkedHashMap<>();
        payRouter.put("su", "#result[code] =='0000'");
        payRouter.put("fail", "#result[code]  !='0000'");
        LinkedHashMap<String, String> queryRouter = new LinkedHashMap<>();
        payRouter.put("su", "#result[code]  =='0000'");
        payRouter.put("fail", "#result[code]  !='0000'");
        flow = new FlowBuilder("simple", new MockContextService(), new MockFlowRecordRepository())
                .addStartNode("init", "initAction", "prepay")
                .addEndNode(new End("su"))
                .addEndNode(new End("fail"))
                .addEndNode(new End("cancel"))
                .onCmd("prepay", "push", "payAction", Router.Type.EXPRESSION, "payRouter", "unknow")
                .onCmd("unknow", "push", "queryAction", Router.Type.EXPRESSION, "queryRouter", "unknow")
                .addRouter("payRouter", payRouter)
                .addRouter("queryRouter", queryRouter)
                .build(ctx);
    }

    @org.junit.Test
    public void test() throws RouterException {
        Map<String, Object> args = new HashMap<>();
        args.put("name", "wanglin");
        flow.execute((String) null, null, args);
    }

    @org.junit.Test
    public void start() {

    }

    @org.junit.Test
    public void end() {

    }

    @org.junit.Test
    public void horae() {
    }

    @org.junit.Test
    public void getNode() {
    }

}