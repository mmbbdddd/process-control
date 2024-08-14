package cn.hz.ddbm.pc.test

import cn.hz.ddbm.pc.profile.ChaosPcService
import cn.hz.ddbm.pc.core.Flow
import cn.hz.ddbm.pc.core.FlowPayload
import cn.hz.ddbm.pc.core.coast.Coasts
import cn.hz.ddbm.pc.test.support.PayloadMock
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import spock.lang.Specification

public class ChaosServiceTest extends Specification {

    ChaosPcService chaosService = new ChaosPcService();
    Flow flow

    public void setup() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext()
        ctx.register(TestConfig.class)
        ctx.refresh()

        flow = Flow.devOf("test", "测试流程", "init", ["su", "fail"] as Set, ["pay", "pay_error"] as Set)

        flow.to("init", Coasts.EVENT_DEFAULT, Coasts.NONE_ACTION, "pay")
        flow.to("pay", Coasts.EVENT_DEFAULT, Coasts.NONE_ACTION, "pay_error")
        flow.to("pay_error", Coasts.EVENT_DEFAULT, Coasts.NONE_ACTION, "pay_error")
        flow.to("pay_error", Coasts.EVENT_DEFAULT, Coasts.NONE_ACTION, "su")


        chaosService.addFlow(flow)
    }


    def "Execute"() {
        expect:

        FlowPayload date = new PayloadMock("init");
        String event = Coasts.EVENT_DEFAULT;
        chaosService.execute("test", date, event, 100, 10)

        where:
        flowStatus                 | nodeStatus | result
//        null                       | null       | "flowStatus is null"
//        Flow.STAUS.RUNNABLE.name() | null       | null
        Flow.STAUS.RUNNABLE.name() | "init"     | "init"
    }


}