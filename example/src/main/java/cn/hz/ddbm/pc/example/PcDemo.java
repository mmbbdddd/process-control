package cn.hz.ddbm.pc.example;

import cn.hutool.extra.spring.SpringUtil;
import cn.hz.ddbm.pc.core.Flow;
import cn.hz.ddbm.pc.core.FlowContext;
import cn.hz.ddbm.pc.core.coast.Coasts;
import cn.hz.ddbm.pc.core.support.Container;
import cn.hz.ddbm.pc.core.support.ExpressionEngine;
import cn.hz.ddbm.pc.core.support.MetricsTemplate;
import cn.hz.ddbm.pc.core.utils.InfraUtils;
import cn.hz.ddbm.pc.profile.ChaosPcService;
import cn.hz.ddbm.pc.session.memory.MemorySessionManager;
import cn.hz.ddbm.pc.status.memory.MemoryStatusManager;
import cn.hz.ddbm.pc.test.support.ContainerMock;
import cn.hz.ddbm.pc.test.support.ExpressionEngineMock;
import cn.hz.ddbm.pc.test.support.MetricsTemplateMock;
import cn.hz.ddbm.pc.test.support.PayloadMock;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

public class PcDemo {

    ChaosPcService chaosService = new ChaosPcService();

    /**
     * doc/img_4.png
     */

    @Test
    public void pc() throws Exception {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(PcDemo.class);
        ctx.refresh();
        PcConfig pcConfig = new PcConfig();
        Flow     flow     = pcConfig.build(null);
        String   event    = Coasts.EVENT_DEFAULT;
        chaosService.addFlow(flow);

        try {
            chaosService.execute("test", new PayloadMock(flow.getInit().getName()), event, 100, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Bean
    ExpressionEngine expressionEngine() {
        return new ExpressionEngineMock();
    }

    @Bean
    MetricsTemplate metricsTemplate() {
        return new MetricsTemplateMock();
    }

    @Bean
    MemoryStatusManager memoryStatusManager() {
        return new MemoryStatusManager();
    }

    @Bean
    MemorySessionManager memorySessionManager() {
        return new MemorySessionManager();
    }

    @Bean
    SpringUtil springUtil() {
        return new SpringUtil();
    }

    @Bean
    Container container() {
        return new ContainerMock();
    }

    @Bean
    InfraUtils infraUtils(Container container) {
        return new InfraUtils(container);
    }

}
