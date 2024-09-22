package cn.hz.ddbm.pc.newcore.chaos;

import cn.hutool.core.util.ObjectUtil;
import cn.hz.ddbm.pc.ProcessorService;
import cn.hz.ddbm.pc.newcore.FlowContext;
import cn.hz.ddbm.pc.newcore.Payload;
import cn.hz.ddbm.pc.newcore.State;
import cn.hz.ddbm.pc.newcore.exception.SessionException;
import cn.hz.ddbm.pc.newcore.log.Logs;
import cn.hz.ddbm.pc.newcore.saga.SagaState;
import lombok.Data;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


public class ChaosService {
    ExecutorService      threadPool = Executors.newFixedThreadPool(20);
    List<StatisticsLine> statisticsLines;

    @Resource
    protected ProcessorService processorService;

    public void chaos(String flowName, MockPayLoad payload, ChaosConfig cc) throws   SessionException,   InterruptedException {
        ChaosAopAspect.chaosConfig = cc;
        statisticsLines = Collections.synchronizedList(new ArrayList<>(cc.executeCount));
        CountDownLatch cdl = new CountDownLatch(cc.executeCount);
        for (int i = 0; i < cc.executeCount; i++) {
            MockPayLoad payloadNum$ = ObjectUtil.clone(payload);
            payloadNum$.id = i + "";
            threadPool.submit(() -> {
                Object result = null;
                try {
                    FlowContext<SagaState> ctx = processorService.getContext(flowName, payloadNum$, "push", true);
                    while (ctx.getFlow().isRunnable(ctx)) {
                        processorService.execute(ctx);
                    }
                    result = ctx;
                } catch (Throwable t) {
                    Logs.error.error("", t);
                    result = t;
                } finally {
                    cdl.countDown();
//                    统计执行结果
                    statistics(payloadNum$.getId(), new Object[]{flowName, payloadNum$}, result);
                }
            });
        }
        try {
            cdl.await(cc.timeout, TimeUnit.SECONDS);
        } catch (java.lang.InterruptedException e) {
            Logs.error.error("", e);
            throw new RuntimeException(e);
        }
        printStatisticsReport();
    }


    private void printStatisticsReport() {
        Map<String, List<StatisticsLine>> groups = statisticsLines.stream()
                                                                  .collect(Collectors.groupingBy(t -> t.result.value));
        Logs.flow.info("混沌测试报告：\\n");
        groups.forEach((key, list) -> {
            Logs.flow.info("{},\t{}", key, list.size());
        });

        statisticsLines.clear();
    }

    private void statistics(Serializable i, Object requestInfo, Object result) {
        statisticsLines.add(new StatisticsLine(i, requestInfo, result));
    }

    @Data
    public static class MockPayLoad<S extends State> implements Payload<S>, Serializable {
        String id;
        S      state;

        public MockPayLoad(S state) {
            this.id    = id + "";
            this.state = state;
        }
    }

}


class StatisticsLine {
    Serializable     index;
    Object           requestInfo;
    StatisticsResult result;

    public StatisticsLine(Serializable i, Object o, Object result) {
        this.index       = i;
        this.requestInfo = o;
        if (result instanceof Throwable) {
            this.result = new StatisticsResult((Throwable) result);
        } else if (result instanceof FlowContext) {
            this.result = new StatisticsResult((FlowContext) result);
        } else {
            this.result = null;
        }
    }
}

class StatisticsResult {
    Boolean isResult;
    String  value;

    public StatisticsResult(Throwable t) {
        this.isResult = false;
        this.value    = t.getClass()
                         .getSimpleName() + ":" + t.getMessage();
    }

    public StatisticsResult(FlowContext ctx) {
        this.isResult = true;
        this.value    = ctx.getState()
                           .code()
                           .toString();
    }

    @Override
    public String toString() {
        return "{" +
                "isResult=" + isResult +
                ", value='" + value + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        StatisticsResult that = (StatisticsResult) object;
        return Objects.equals(isResult, that.isResult) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isResult, value);
    }
}



