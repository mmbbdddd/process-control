package io.ddbm.pc.factory;

import io.ddbm.pc.Flow;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Map;

public class FlowFactory implements InitializingBean, ApplicationContextAware {
    ClassPathResource  path;
    Map<String, Flow>  flows;
    ApplicationContext ctx;


    public void setPath(String path) {
        this.path = new ClassPathResource(path);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        registerFlows();
    }

    private void registerFlows() throws Exception {
        File[] files = listFlows();
        for (File file : files) {
            doRegisterFlow(new XmlFlowReader(ctx, file));
        }
    }

    private void doRegisterFlow(XmlFlowReader reader) throws Exception {
        Flow flow = reader.read();
        flows.put(flow.getName(), flow);
    }

    private File[] listFlows() throws IOException {
        File dir = path.getFile();
        return dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".xml");
            }
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ctx = applicationContext;
    }
}
