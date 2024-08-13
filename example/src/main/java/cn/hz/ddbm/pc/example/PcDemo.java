package cn.hz.ddbm.pc.example;

import cn.hutool.extra.spring.SpringUtil;
import cn.hz.ddbm.pc.core.support.Container;
import cn.hz.ddbm.pc.core.utils.InfraUtils;
import cn.hz.ddbm.pc.session.memory.MemorySessionManager;
import cn.hz.ddbm.pc.status.memory.MemoryStatusManager;
import cn.hz.ddbm.pc.test.support.ContainerMock;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

public class PcDemo {
    /**
     * doc/img_4.png
     */

    @Test
    public void pc() throws Exception {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(PcDemo.class);
        ctx.refresh();
        ;
        PcConfig pcConfig = new PcConfig();
        System.out.println(pcConfig.build(null));
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
