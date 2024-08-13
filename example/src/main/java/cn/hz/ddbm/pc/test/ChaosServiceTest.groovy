package cn.hz.ddbm.pc.test

import cn.hz.ddbm.pc.ChaosService
import cn.hz.ddbm.pc.core.Flow
import cn.hz.ddbm.pc.core.FlowPayload
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

        flow = Flow.devOf("test", "测试流程", "init", ["su", "fail"] as Set, ["pay", "pay_error"] as Set)

        flow.onEventTo("init", Coasts.EVENT_DEFAULT, "testAction", "pay")
        flow.onEventTo("pay", Coasts.EVENT_DEFAULT, "testAction", "pay_error")
        flow.onEventTo("pay_error", Coasts.EVENT_DEFAULT, "testAction", "pay_error")
        flow.onEventTo("pay_error", Coasts.EVENT_DEFAULT, "testAction", "su")
        flow.validate()

        chaosService.addFlow(flow)
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