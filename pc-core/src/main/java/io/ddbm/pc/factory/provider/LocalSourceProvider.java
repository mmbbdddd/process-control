package io.ddbm.pc.factory.provider;

import com.google.common.io.Files;
import io.ddbm.pc.factory.FlowFactory;
import io.ddbm.pc.factory.FlowSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileInputStream;


@Slf4j
public class LocalSourceProvider implements InitializingBean {
    @Override
    public void afterPropertiesSet()
        throws Exception {
        File flowDirectory = new ClassPathResource("/flow").getFile();
        Iterable<File> files = Files.fileTraverser().breadthFirst(flowDirectory);

        files.forEach(file -> {
            try {
                if (file.getName().endsWith(".xml")) {
                    FlowFactory.onFlowSourceEvent(new FlowSource(new FileInputStream(file)));
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("读取流程定义文件异常：", e);
            }
        });
    }
}
