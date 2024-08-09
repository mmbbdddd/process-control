package cn.hz.ddbm.pc;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hz.ddbm.pc.core.Flow;
import cn.hz.ddbm.pc.core.FlowContext;
import cn.hz.ddbm.pc.core.FlowPayload;
import cn.hz.ddbm.pc.core.coast.Coasts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class ChaosService extends PcService {

    ExecutorService          threadPool     = Executors.newFixedThreadPool(20);
    Map<String, ReportTable> reportTableMap = new HashMap<>();

    public <T extends FlowPayload> void execute(String flowName, T payload, String event, Integer times, Integer timeout) {
        Assert.notNull(flowName, "flowName is null");
        Assert.notNull(payload, "FlowPayload is null");
        CountDownLatch cdl = new CountDownLatch(times);
        for (int i = 0; i < times; i++) {
            int finalI = i;
            threadPool.submit(() -> {
                Object result = null;
                try {
//                    独立事件执行
                    FlowContext<T> ctx = standalone(flowName, payload, event);
                    result = ctx;
                } catch (Throwable t) {
                    t.printStackTrace();
                    result = t;
                } finally {
                    cdl.countDown();
//                    统计执行结果
                    statistics(finalI, null, result);
                }
            });
        }
        try {
            cdl.await(timeout, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        printStatisticsReport();
    }

    private void printStatisticsReport() {

    }

    private void statistics(int i, Object o, Object result) {

    }

    private <T extends FlowPayload> FlowContext<T> standalone(String flowName, T payload, String event) {
        event = StrUtil.isBlank(event) ? Coasts.EVENT_DEFAULT : event;
        Flow           flow = flows.get(flowName);
        FlowContext<T> ctx  = new FlowContext<>(flow, payload, event);
        execute(ctx);
        return ctx;
    }

    static class ReportTable {
        List<StatisticsLine> statisticsLines;
    }

    static class StatisticsLine {

    }
}
