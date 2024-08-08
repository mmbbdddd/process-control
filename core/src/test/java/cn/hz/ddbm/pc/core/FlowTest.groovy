package cn.hz.ddbm.pc.core


import cn.hz.ddbm.pc.core.coast.Coasts
import org.junit.Assert
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import spock.lang.Specification

class FlowTest extends Specification {
    Flow flow

    void setup() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext()
        ctx.register(TestConfig.class)
        ctx.refresh()

        flow = Flow.devOf("test", "测试流程")
        flow.nodes = [
                "init"     : new Node(Node.Type.START, "init", [] as HashMap),
                "pay"      : new Node(Node.Type.TASK, "pay", [] as HashMap),
                "pay_error": new Node(Node.Type.TASK, "pay_error", [] as HashMap),
                "su"       : new Node(Node.Type.END, "su", [] as HashMap),
                "fail"     : new Node(Node.Type.END, "fail", [] as HashMap),
        ]
        flow.onEventTo("init", Coasts.EVENT_DEFAULT, "testAction", "pay")
        flow.onEventTo("pay", Coasts.EVENT_DEFAULT, "testAction", "pay_error")
        flow.onEventTo("pay_error", Coasts.EVENT_DEFAULT, "testAction", "pay_error")
        flow.onEventTo("pay_error", Coasts.EVENT_DEFAULT, "testAction", "su")
        flow.validate()
    }

    void cleanup() {}

    def "Of"() {
        expect:
        Flow f = Flow.of(name, "", [plugin], [] as HashMap)
        String.format("%s:%s:%s", f.name, f.sessionManager, f.statusManager) == result
        where:
        name | plugin | session | status | result
//        null | null|null|null|null
        "i"  | "log"  | null    | null   | String.format("%s:%s:%s", "i", "redis", "redis")


    }

    def "AddNode"() {}

    def "AddRouter"() {}

    def "OnEventRouter"() {}

    def "OnEventTo"() {}

    def "Execute"() {
        expect:
        FlowEntity date = new MockEntity(
                id: 1,
                flowStatus: flowStatus,
                nodeStatus: nodeStatus
        );
        String event = Coasts.EVENT_DEFAULT;
//        try {
        FlowContext ctx = new FlowContext(flow, date, event, [] as HashMap)
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
