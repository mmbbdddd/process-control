package io.ddbm.pc.factory

import io.ddbm.pc.Flow
import io.ddbm.pc.config.PcConfiguration
import ognl.Ognl
import org.junit.Before
import org.junit.Test
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.core.io.ClassPathResource

public class XmlFlowReaderTest {
    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
    Flow flow;

    @Before
    public void setup() throws Exception {
        ctx.register(PcConfiguration.class);
        ctx.refresh();

        XmlFlowReader reader = new XmlFlowReader(new ClassPathResource("/flow/simple.xml").getFile());
        flow = reader.read();
    }

    @Test
    public void test() {
        var dict = [
                "nodes.init.name"                          : "init",
                "nodes.init.fluent"                        : Boolean.TRUE,
                "nodes.data_process.fluent"                : Boolean.FALSE,
                "nodes.su.name"                            : "su",
                "nodes.data_process.name"                  : "data_process",
                "nodes.data_process.events.next.retry"     : "10",
                "nodes.shenpi.events.next.retry"           : "1",
                "nodes.data_process.events.next.actionName": "dataProcessAction",
                "nodes.data_process.events.next.maybe"     : ["data_process"],
                "nodes.data_process.events.next.desc"      : "",
//                "plugins.catPlugin."                       : "retry"
        ] as Map

        for (Map.Entry<String, Object> entry : dict.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            println(String.format("%s:%s", Ognl.getValue(key, flow), value))
        }
    }


}