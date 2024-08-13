package cn.hz.ddbm.pc.test

import cn.hz.ddbm.pc.core.Flow
import cn.hz.ddbm.pc.core.coast.Coasts
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import spock.lang.Specification

public abstract class BaseSpec extends Specification {
    Flow flow

    public void setup() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext()
        ctx.register(TestConfig.class)
        ctx.refresh()

        flow = Flow.devOf("test", "测试流程", "init", ["su", "fail"] as Set, ["pay", "pay_error"] as Set)

        flow.to("init", Coasts.EVENT_DEFAULT, Coasts.NONE_ACTION, "pay")
        flow.to("pay", Coasts.EVENT_DEFAULT, Coasts.NONE_ACTION, "pay_error")
        flow.to("pay_error", Coasts.EVENT_DEFAULT, Coasts.NONE_ACTION, "pay_error")
        flow.to("pay_error", Coasts.EVENT_DEFAULT, Coasts.NONE_ACTION, "su")


        hook();
    }

    abstract void hook()
}
