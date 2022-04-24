package io.ddbm.pc.factory;

import org.junit.Before;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;

public class XmlFlowReaderTest {
    XmlFlowReader reader;

    @Before
    public void setup() throws Exception {
        reader = new XmlFlowReader(Mockito.mock(ApplicationContext.class), new ClassPathResource("/flow/simple.xml").getFile());
    }

}