package hz.ddbm.pc.core.factory.xml;

import hz.ddbm.pc.core.domain.Flow;
import hz.ddbm.pc.core.domain.Node;
import hz.ddbm.pc.core.factory.fsm.Fsm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//import com.google.common.io.Files;

@Slf4j
public class XmlFlowFactory {
    public static List<Flow> loadFlows() throws IOException {
        List<Flow> flows         = new ArrayList<>();
        File       flowDirectory = new ClassPathResource("/flow").getFile();
//        Iterable<File> files         = Files.fileTraverser().breadthFirst(flowDirectory);
        Iterator<Path> files = Files.walkFileTree(flowDirectory.toPath(), new SimpleFileVisitor<Path>() {
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }
        }).iterator();

        files.forEachRemaining(file -> {
            try {
                if (!file.toFile().isDirectory() && file.getFileName().endsWith(".xml")) {
                    flows.add(new FlowHanlder(new FileInputStream(file.toFile())).getFlow());
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("读取流程定义文件异常：", e);
            }
        });
        return flows;
    }
}


@Slf4j
class FlowHanlder extends DefaultHandler {

    FlowB fb;

    NodeB node;

    String routerName;

    public FlowHanlder(FileInputStream inputStream)
            throws Exception {
        // 创建解析器工厂
        SAXParserFactory spf = SAXParserFactory.newInstance();
        // 创建解析器
        SAXParser sax = spf.newSAXParser();
        // 执行parse()方法
        sax.parse(inputStream, this);
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
    }

    @Override
    public void endDocument()
            throws SAXException {
        super.endDocument();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
        super.startElement(uri, localName, qName, attributes);

        switch (qName) {
            case "flow": {
                initFlow(attributes);
                break;
            }
            case "start": {
                initStart(attributes);
                break;
            }
            case "node": {
                initNode(attributes);
                break;
            }
            case "end": {
                initEnd(attributes);
                break;
            }
            case "on": {
                initOn(attributes);
                break;
            }
            case "plugins": {
                initPlugins(attributes);
                break;
            }
            case "router": {
                initRouter(attributes);
                break;
            }
            case "case": {
                initCase(attributes);
                break;
            }
        }
    }

    private void initRouter(Attributes attributes) {
        String routerName = attributes.getValue("name");
        if (!StringUtils.isEmpty(routerName)) {
            this.routerName = routerName;
        } else {
            this.routerName = null;
        }
        this.node = null;
    }

    private void initCase(Attributes attributes) {
        String test = attributes.getValue("test");
        String to   = attributes.getValue("to");
        if (!StringUtils.isEmpty(test) && !StringUtils.isEmpty(to) && this.routerName != null) {
            fb.addRouter(routerName, test, to);
        }
        this.node = null;
    }

    private void initPlugins(Attributes attributes) {
        String name = attributes.getValue("name");
        if (!StringUtils.isEmpty(name)) {
            fb.addPlugin(name);
        }
        this.node = null;
        this.routerName = null;
    }

    private void initOn(Attributes attributes) {
        String event  = attributes.getValue("event");
        String action = attributes.getValue("action");
        String retry  = attributes.getValue("retryCount");
        String to     = attributes.getValue("to");
        String router = attributes.getValue("router");
        String desc   = attributes.getValue("desc");
        fb.on(this.node, event, action, to, router, desc, retry);
    }

    private void initEnd(Attributes attributes) {
        String name = attributes.getValue("name");
        this.node = fb.addNode(Node.Type.END, name);
        this.routerName = null;
    }

    private void initNode(Attributes attributes) {
        String name = attributes.getValue("name");
        this.node = fb.addNode(Node.Type.TASK, name);
        this.routerName = null;
    }

    private void initStart(Attributes attributes) {
        this.node = fb.addNode(Node.Type.START, "start");
        this.routerName = null;
    }

    private void initFlow(Attributes attributes) {
        String flowName = attributes.getValue("name");
        fb = new FlowB(flowName);
        this.node = null;
        this.routerName = null;
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        super.endElement(uri, localName, qName);
    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        super.characters(ch, start, length);
    }

    @Override
    public void fatalError(SAXParseException e)
            throws SAXException {
        super.fatalError(e);
    }

    public Flow getFlow() {
        return fb.build();
    }
}

class FlowB{

    public FlowB(String flowName) {

    }

    public void addRouter(String routerName, String test, String to) {

    }

    public Flow build() {
        return null;
    }

    public NodeB addNode(Node.Type task, String name) {
        return null;
    }
}
class NodeB{

}

