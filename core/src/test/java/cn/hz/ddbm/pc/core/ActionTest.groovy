package cn.hz.ddbm.pc.core

import cn.hutool.extra.spring.SpringUtil
import cn.hz.ddbm.pc.core.action.SagaAction
import cn.hz.ddbm.pc.core.utils.InfraUtils
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import spock.lang.*


class ActionTest extends Specification {

    def setup() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext()
        ctx.register(CC.class)
        ctx.refresh()
    }
//
    def "test of"() {
        expect:
        Action.of(actionDsl).beanName() == result
        where:
        actionDsl               | result
        null                    | "none"
        "payAction"             | "payAction"
        "payAction,queryAction" | "payAction,queryAction"
    }

    def "exp"() {
        when:
        int i = 0;
        then:
        println "action".matches("\\w+")
    }

    @ComponentScan("cn.hz.ddbm.pc.core.actions")
    static class CC {
        @Bean
        SpringUtil springUtil() {
            return new SpringUtil()
        }

        @Bean
        InfraUtils infraUtils() {
            return new InfraUtils()
        }
    }

}

