package cn.hz.ddbm.pc.example;

import cn.hz.ddbm.pc.configuration.PcChaosConfiguration;
import cn.hz.ddbm.pc.core.Flow;
import cn.hz.ddbm.pc.core.coast.Coasts;
import cn.hz.ddbm.pc.profile.ChaosPcService;
import cn.hz.ddbm.pc.profile.chaos.ChaosRule;
import cn.hz.ddbm.pc.test.support.PayloadMock;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

import static cn.hz.ddbm.pc.core.log.Logs.flow;

public class PcDemo {

    ChaosPcService chaosService;

    /**
     * doc/img_4.png
     */

    @Test
    public void pc() throws Exception {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(PcDemo.class);
        ctx.register(PcChaosConfiguration.class);
        ctx.refresh();
//        PcService devPcService = ctx.getBean(DevPcService.class);
        chaosService = ctx.getBean(ChaosPcService.class);

        String   event    = Coasts.EVENT_DEFAULT;

        List<ChaosRule> rules = new ArrayList<ChaosRule>() {{
            add(new ChaosRule("true", 0.1, new ArrayList<Class<? extends Throwable>>() {{
                add(RuntimeException.class);
                add(Exception.class);
            }}));
        }};
        try {
            chaosService.execute("test", new PayloadMock(PcState.init.name()), event, 100, 10, rules);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Bean
    PcConfig chaosPcService() {
        return new PcConfig();
    }


}
