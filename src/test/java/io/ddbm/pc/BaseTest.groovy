package io.ddbm.pc

import io.ddbm.pc.config.PcConfiguration
import org.junit.Before
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.FilterType

class BaseTest {
    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();

    @Before
    public void setup() {
        ctx.register(PcConfiguration)
        ctx.register(PcConfig)
        ctx.refresh()

    }

    @Configuration
    @ComponentScan(basePackages = "io.ddbm.pc.simple", includeFilters = [
            @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = Action.class)
    ])
    static class PcConfig {
    }
}
