package cn.hz.ddbm.pc.test

import cn.hz.ddbm.pc.core.Flow
import cn.hz.ddbm.pc.core.FlowContext
import cn.hz.ddbm.pc.core.FlowPayload
import cn.hz.ddbm.pc.core.coast.Coasts
import cn.hz.ddbm.pc.test.support.PayloadMock
import org.junit.Assert

class FlowTest extends BaseSpec {


    void cleanup() {}

    def "Of"() {
        expect:
        Flow f = Flow.devOf(name, "xx", [plugin], [] as HashMap)
        String.format("%s:%s:%s", f.name, f.sessionManager, f.statusManager) == result
        where:
        name  | session | status | result
//        null | null|null|null|null
        "i"   | null    | null   | String.format("%s:%s:%s", "i", "redis", "redis")


    }

    def "AddNode"() {}

    def "AddRouter"() {}

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
