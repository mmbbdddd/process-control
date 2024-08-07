package cn.hz.ddbm.pc;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.StrUtil;
import cn.hz.ddbm.pc.core.Flow;
import cn.hz.ddbm.pc.core.FlowContext;
import cn.hz.ddbm.pc.core.FlowPayload;
import cn.hz.ddbm.pc.core.coast.Coasts;
import cn.hz.ddbm.pc.core.log.Logs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class ChaosService extends PcService {

    ExecutorService      threadPool = Executors.newFixedThreadPool(20);
    List<StatisticsLine> statisticsLines;

    public <T extends FlowPayload> void execute(String flowName, T payload, String event, Integer times, Integer timeout) {
        Assert.notNull(flowName, "flowName is null");
        Assert.notNull(payload, "FlowPayload is null");
        CountDownLatch cdl = new CountDownLatch(times);
        statisticsLines = new ArrayList<>(times);
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
                    statistics(finalI, new Object[]{flowName, payload, event}, result);
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
        Map<Pair, List<StatisticsLine>> groups = statisticsLines.stream()
                .collect(Collectors.groupingBy(t -> Pair.of(t.result.type, t.result.value)));
        Logs.flow.info("混沌测试报告：\\n");
        groups.forEach((triple, list) -> {
            Logs.flow.info("{},{},{}", triple.getKey(), triple.getValue(), list.size());
        });

        statisticsLines.clear();
    }

    private void statistics(int i, Object requestInfo, Object result) {
        statisticsLines.add(new StatisticsLine(i, requestInfo, result));
    }

    private <T extends FlowPayload> FlowContext<T> standalone(String flowName, T payload, String event) {
        event = StrUtil.isBlank(event) ? Coasts.EVENT_DEFAULT : event;
        Flow           flow = flows.get(flowName);
        FlowContext<T> ctx  = new FlowContext<>(flow, payload, event);
        execute(ctx);
        return ctx;
    }


    static class StatisticsLine {
        int       index;
        Object    requestInfo;
        TypeValue result;

        public StatisticsLine(int i, Object o, Object result) {
            this.index       = i;
            this.requestInfo = o;
            if (result instanceof Throwable) {
                this.result = new TypeValue((Throwable) result);
            } else if (result instanceof FlowContext) {
                this.result = new TypeValue((FlowContext) result);
            } else {
                this.result = null;
            }
        }
    }

    static class TypeValue {
        String type;
        String value;

        public TypeValue(Throwable t) {
            this.type  = t.getClass().getSimpleName();
            this.value = t.getMessage();
        }

        public TypeValue(FlowContext<?> ctx) {
            this.type  = ctx.getClass().getSimpleName();
            this.value = String.format("%s:%s", ctx.getStatus().getFlow().name(), ctx.getStatus().getNode());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TypeValue typeValue = (TypeValue) o;
            return Objects.equals(type, typeValue.type) && Objects.equals(value, typeValue.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(type, value);
        }

        @Override
        public String toString() {
            return "TypeValue{" +
                    "type='" + type + '\'' +
                    ", value='" + value + '\'' +
                    '}';
        }
    }
}
