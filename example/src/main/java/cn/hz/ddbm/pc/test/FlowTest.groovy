package cn.hz.ddbm.pc.test

import cn.hz.ddbm.pc.core.Flow
import cn.hz.ddbm.pc.core.FlowContext
import cn.hz.ddbm.pc.core.FlowPayload
import cn.hz.ddbm.pc.core.coast.Coasts
import cn.hz.ddbm.pc.core.router.ExpressionRouter
import cn.hz.ddbm.pc.test.support.PayloadMock
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.serializer.SerializerFeature
import org.junit.Assert

class FlowTest extends BaseSpec {


    void cleanup() {}

    def "Of"() {
        expect:
        Flow f = Flow.devOf(name, "测试流程", "init", ["su", "fail"] as Set, ["pay", "pay_error"] as Set)
        String.format("%s:%s:%s", f.name, f.sessionManager.code(), f.statusManager.code()) == result
        where:
        name | session | status | result
//        null | null|null|null|null
        "i"  | null    | null   | String.format("%s:%s:%s", "i", "memory", "memory")


    }

    def "AddNode"() {}

    def "AddRouter"() {
        when:
        Flow f = Flow.devOf("test", "测试流程", "init", ["su", "fail"] as Set, ["pay", "pay_error"] as Set)
        f.router("init",Coasts.EVENT_PAUSE,Coasts.NONE_ACTION,new ExpressionRouter("pay_router",
            new ExpressionRouter.NodeExpression(to:"pay_error",expression: "Math.random() > 0.1"),
            new ExpressionRouter.NodeExpression(to:"su",expression: "Math.random() > 0.6"),
            new ExpressionRouter.NodeExpression(to:"init",expression: "Math.random() > 0.1"),
            new ExpressionRouter.NodeExpression(to:"fail",expression: "Math.random() > 0.2"),
        ))

        then:
//        System.out.println(JSON.toJSONString(f.getFsmTable().getRecords(), SerializerFeature.PrettyFormat))
        System.out.println(Arrays.toString(f.getFsmTable().getRecords()))

    }

    def "OnEventRouter"() {}

    def "OnEventTo"() {}

    def "Execute"() {
        expect:
        FlowPayload date = new PayloadMock(
                id: 1,
                flowStatus: flowStatus,
                nodeStatus: nodeStatus
        );
        String event = Coasts.EVENT_DEFAULT;
//        try {
        FlowContext ctx = new FlowContext(flow, date, event)
        ctx.getStatus().node == result
        flow.execute(ctx)
//        } catch (Exception e) {
////            e.printStackTrace()
//            e.getMessage() == result
//        }
        where:
        flowStatus                 | nodeStatus | result
//        null                       | null       | "flowStatus is null"
//        Flow.STAUS.RUNNABLE.name() | null       | null
        Flow.STAUS.RUNNABLE.name() | "init"     | "init"

    }

    def "StartStep"() {
        when:
        def node = flow.startStep()
        then:
        Assert.assertTrue(node.name == "init")
    }

    def "NodeNames"() {
        when:
        def names = flow.nodeNames()
        then:
        Assert.assertTrue(["init", "pay", "pay_error", "su", "fail"] as Set == names)
    }

    @Override
    void hook() {

    }
}
