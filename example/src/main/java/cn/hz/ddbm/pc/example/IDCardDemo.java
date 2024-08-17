package cn.hz.ddbm.pc.example;

import cn.hz.ddbm.pc.configuration.PcChaosConfiguration;
import cn.hz.ddbm.pc.core.coast.Coasts;
import cn.hz.ddbm.pc.profile.ChaosPcService;
import cn.hz.ddbm.pc.profile.chaos.ChaosRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@ComponentScan("cn.hz.ddbm.pc.example.actions")
@SpringBootTest
@Import({PcChaosConfiguration.class, IDCardDemo.DemoConfig.class})
@RunWith(SpringRunner.class)
public class IDCardDemo {

    @Autowired
    ChaosPcService chaosService;

    /**
     * doc/img_4.png
     */

    @Test
    public void pc() throws Exception {

        String event = Coasts.EVENT_DEFAULT;

        List<ChaosRule> rules = new ArrayList<ChaosRule>() {{
            add(new ChaosRule("true", 0.1, new ArrayList<Class<? extends Throwable>>() {{
                add(RuntimeException.class);
                add(Exception.class);
            }}));
        }};
        try {
            chaosService.execute("test", new ChaosPcService.MockPayLoad(IDCardState.init.name()), event, 100, 10, rules, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static class DemoConfig {
        @Bean
        IDCardFSM pcConfig() {
            return new IDCardFSM();
        }
    }


}
