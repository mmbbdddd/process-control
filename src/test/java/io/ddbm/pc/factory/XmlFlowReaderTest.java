package io.ddbm.pc.factory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.ddbm.pc.Flow;
import io.ddbm.pc.Flows;
import io.ddbm.pc.config.PcConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.ClassPathResource;

public class XmlFlowReaderTest {
    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
    Flow flow ;
    @Before
    public void setup() throws Exception {
        ctx.register(PcConfiguration.class);
        ctx.refresh();

        XmlFlowReader reader = new XmlFlowReader(new ClassPathResource("/flow/simple.xml").getFile());
        flow = reader.read();
    }

    @Test
    public void get() {
        System.out.println(JSON.toJSONString(flow, SerializerFeature.PrettyFormat));
    }



}