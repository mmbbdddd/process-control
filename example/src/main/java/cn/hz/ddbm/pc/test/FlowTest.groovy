package cn.hz.ddbm.pc.test

import cn.hz.ddbm.pc.core.Flow
import cn.hz.ddbm.pc.core.FlowContext
import cn.hz.ddbm.pc.core.FlowPayload
import cn.hz.ddbm.pc.core.Node
import cn.hz.ddbm.pc.core.Profile
import cn.hz.ddbm.pc.core.coast.Coasts
import cn.hz.ddbm.pc.core.router.ExpressionRouter
import cn.hz.ddbm.pc.test.support.PayloadMock
import org.junit.Assert
import spock.lang.Specification

class FlowTest extends Specification {


    void cleanup() {}

    def "Of"() {
        expect:
        Flow f = Flow.devOf("test", "测试流程", [
                new Node(Node.Type.START,"init",null,),
                new Node(Node.Type.TASK,"pay",null,),
                new Node(Node.Type.END,"pay_error",null,)
        ] as Set, [])
        String.format("%s:%s:%s", f.name, f.profile.sessionManager, f.profile.statusManager) == result
        where:
        name | session | status | result
//        null | null|null|null|null
        "i"  | null    | null   | String.format("%s:%s:%s", "i", "redis", "redis")


    }

    def "AddNode"() {}

    def "AddRouter"() {
        when:
        Flow f = Flow.devOf("test", "测试流程", [
                new Node(Node.Type.START,"init",null,),
                new Node(Node.Type.TASK,"pay",null,),
                new Node(Node.Type.END,"pay_error",null,)
        ] as Set, [])


        then:
//        System.out.println(JSON.toJSONString(f.getFsmTable().getRecords(), SerializerFeature.PrettyFormat))
        System.out.println(Arrays.toString(f.getFsmTable().getRecords()))

    }

    def "OnEventRouter"() {}

    def "OnEventTo"() {}

    def "Execute"() {
        expect:
        FlowPayload date = new PayloadMock("init");
        String event = Coasts.EVENT_DEFAULT;
//        try {
        FlowContext ctx = new FlowContext(flow, date, event, Profile.defaultOf())
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


}
