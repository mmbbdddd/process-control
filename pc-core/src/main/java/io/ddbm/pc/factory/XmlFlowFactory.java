package io.ddbm.pc.factory;

import io.ddbm.pc.Flow;
import io.ddbm.pc.Flows;
import io.ddbm.pc.config.PcProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;

public class XmlFlowFactory implements InitializingBean {
    @Autowired
    PcProperties properties;


    @Override
    public void afterPropertiesSet() throws Exception {
        registerFlows();
    }

    public void registerFlows() throws Exception {
        File[] files = listFlows();
        for (File file : files) {
            Flow flow = new XmlFlowReader(file).read();
            Flows.set(flow);
        }
    }

    private File[] listFlows() throws IOException {
        File dir = new ClassPathResource(properties.getFlowPath()).getFile();
        return dir.listFiles(pathname -> pathname.getName().endsWith(".xml"));
    }

}
