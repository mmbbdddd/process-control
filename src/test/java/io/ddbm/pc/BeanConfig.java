package io.ddbm.pc;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

@Configurable
public class BeanConfig {
    @Bean
    Action initAction(){
        return new MockAction();
    }
    @Bean
    Action payAction(){
        return new MockAction();
    }
    @Bean
    Action queryAction(){
        return new MockAction();
    }
}
