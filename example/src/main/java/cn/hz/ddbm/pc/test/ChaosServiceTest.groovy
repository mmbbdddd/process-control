package cn.hz.ddbm.pc.test


import cn.hz.ddbm.pc.ChaosService
import cn.hz.ddbm.pc.core.Flow
import cn.hz.ddbm.pc.core.FlowPayload
import cn.hz.ddbm.pc.core.coast.Coasts
import cn.hz.ddbm.pc.test.support.PayloadMock

public class ChaosServiceTest extends BaseSpec {

     ChaosService chaosService = new ChaosService();
    @Override
    void hook() {
        chaosService.flows.put("test", flow)
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