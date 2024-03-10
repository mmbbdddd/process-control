package io.ddbm.pc.factory;

import io.ddbm.pc.Flow;
import io.ddbm.pc.Node;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class FlowFactory {
    static Map<String, Flow> flows = new ConcurrentHashMap<>();

    public static Flow get(String flowName) {
        return flows.get(flowName);
    }

    public static Flow getForChaos(String flowName) {
        Flow flow = flows.get(flowName);
        return flow;
    }

    public static void onFlowSourceEvent(FlowSource flowSource) {
        Flow flow = null;
        try {
            flow = new FlowHanlder(flowSource).getFlow();
            flows.put(flow.getName(), flow);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Set<String> allFlowNames() {
        return flows.keySet();
    }
}


@Slf4j
class FlowHanlder extends DefaultHandler {

    FlowBuilder fb;

    Node node;

    String routerName;

    public FlowHanlder(FlowSource flowSource)
        throws Exception {
        // 创建解析器工厂
        SAXParserFactory spf = SAXParserFactory.newInstance();
        // 创建解析器
        SAXParser sax = spf.newSAXParser();
        // 执行parse()方法
        sax.parse(flowSource.getFlowDefineXmlStream(), this);
    }

    @Override
    public void startDocument()
        throws SAXException {
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
        String to = attributes.getValue("to");
        if (!StringUtils.isEmpty(test) && !StringUtils.isEmpty(to) && this.routerName != null) {
            fb.addCase(routerName, test, to);
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
        String event = attributes.getValue("event");
        String action = attributes.getValue("action");
        String retry = attributes.getValue("retryCount");
        String to = attributes.getValue("to");
        String router = attributes.getValue("router");
        String desc = attributes.getValue("desc");
        fb.on(this.node, event, action, to, router, desc, retry);
    }

    private void initEnd(Attributes attributes) {
        String name = attributes.getValue("name");  
        this.node = fb.addNode(Node.Type.END, name);
        this.routerName = null;
    }

    private void initNode(Attributes attributes) {
        String name = attributes.getValue("name");  
        this.node = fb.addNode(Node.Type.NODE, name);
        this.routerName = null;
    }

    private void initStart(Attributes attributes) {  
        this.node = fb.addNode(Node.Type.START, "start");
        this.routerName = null;
    }

    private void initFlow(Attributes attributes) {
        String flowName = attributes.getValue("name");
        fb = new FlowBuilder(flowName);
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

