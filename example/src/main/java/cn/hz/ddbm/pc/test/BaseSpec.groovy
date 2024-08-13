package cn.hz.ddbm.pc.test

import cn.hz.ddbm.pc.core.Flow
import cn.hz.ddbm.pc.core.Node
import cn.hz.ddbm.pc.core.coast.Coasts
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import spock.lang.Specification

public abstract class BaseSpec extends Specification {
    Flow flow

    public void setup() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext()
        ctx.register(TestConfig.class)
        ctx.refresh()

        flow = Flow.devOf("test", "测试流程")
        flow.nodes = [
                "init"     : new Node(Node.Type.START, "init", [] as HashMap),
                "pay"      : new Node(Node.Type.TASK, "pay", [] as HashMap),
                "pay_error": new Node(Node.Type.TASK, "pay_error", [] as HashMap),
                "su"       : new Node(Node.Type.END, "su", [] as HashMap),
                "fail"     : new Node(Node.Type.END, "fail", [] as HashMap),
        ]
        flow.onEventTo("init", Coasts.EVENT_DEFAULT, "testAction", "pay")
        flow.onEventTo("pay", Coasts.EVENT_DEFAULT, "testAction", "pay_error")
        flow.onEventTo("pay_error", Coasts.EVENT_DEFAULT, "testAction", "pay_error")
        flow.onEventTo("pay_error", Coasts.EVENT_DEFAULT, "testAction", "su")
        flow.validate()

        hook();
    }

    abstract void hook()
}
