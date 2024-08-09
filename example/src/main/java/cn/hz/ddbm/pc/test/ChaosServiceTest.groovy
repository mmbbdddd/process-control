package cn.hz.ddbm.pc.test


import cn.hz.ddbm.pc.ChaosService
import cn.hz.ddbm.pc.core.Flow
import cn.hz.ddbm.pc.core.FlowPayload
import cn.hz.ddbm.pc.core.Node
import cn.hz.ddbm.pc.core.coast.Coasts
import cn.hz.ddbm.pc.test.support.PayloadMock
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import spock.lang.Specification

public class ChaosServiceTest extends Specification {

    ChaosService chaosService = new ChaosService();
    Flow flow

    public void setup() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext()
        ctx.register(TestConfig.class)
        ctx.refresh()

        flow = Flow.of("test", "测试流程",[],[] as HashMap)
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

        chaosService.flows.put("test",flow)
    }


    def "Execute"() {
        expect:

        FlowPayload date = new PayloadMock(
                id: Math.random().intValue() * 1000,
                flowStatus: flowStatus,
                nodeStatus: nodeStatus
        );
        String event = Coasts.EVENT_DEFAULT;
        chaosService.execute("test", date, event, 100, 10)

        where:
        flowStatus                 | nodeStatus | result
//        null                       | null       | "flowStatus is null"
//        Flow.STAUS.RUNNABLE.name() | null       | null
        Flow.STAUS.RUNNABLE.name() | "init"     | "init"
    }


}