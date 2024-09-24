package cn.hz.ddbm.pc.newcore.config;

import cn.hz.ddbm.pc.newcore.chaos.*;
import cn.hz.ddbm.pc.newcore.utils.EnvUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;


@EnableAspectJAutoProxy
@Import(ProcessControlConfiguration.class)
public class ChaosConfiguration {

    @Bean
    @Order(value = 1000)
    ChaosService chaosService() {
        EnvUtils.setChaosMode(true);
        return new ChaosService();
    }

//    @Bean
//    ChaosAopAspect chaosAopAspect() {
//        return new ChaosAopAspect();
//    }


    @Bean
    RemoteChaosAction remoteChaosAction() {
        return new RemoteChaosAction();
    }

    @Bean
    LocalChaosAction localChaosAction() {
        return new LocalChaosAction();
    }


}

