package io.ddbm.pc.factory;

import io.ddbm.pc.Flow;
import io.ddbm.pc.FlowBuilder;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.List;

public class XmlFlowReader implements ApplicationContextAware {
    Document           doc;
    FlowBuilder        fb;
    ApplicationContext ctx;


    public XmlFlowReader(File file) throws Exception {
        doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
        doc.getDocumentElement().normalize();
    }

    public Flow read() throws Exception {
        Element ele = doc.getDocumentElement();
        return parseFlow(ele);
    }

    private Flow parseFlow(Element flow) throws Exception {
        String name = flow.getAttribute("name");
        fb = new FlowBuilder(name);
        parseNode(flow.getChildNodes());
        return fb.build(ctx);
    }

    private void parseNode(NodeList list) {
        for (int i = 0; i < list.getLength(); i++) {
            Node   node     = list.item(i);
            String nodeName = node.getNodeName();
            switch (nodeName) {
                case "start":
                    parseStart((Element) node);
                    break;
                case "end":
                    parseEnd((Element) node);
                    break;
                case "node":
                    parseActionNode((Element) node);
                    break;
                case "router":
                    parseRouters((Element) node);
                    break;
            }
        }
    }

    private void parseStart(Element node) {
        String    name   = node.getAttribute("name");
        List<CMD> cmds   = parseCmd(node.getChildNodes());
        String    action = cmds.get(0).action;
        String    to     = cmds.get(0).to;
        fb.addStartNode(name, action, to);
    }

    private void parseEnd(Element node) {
    }

    private void parseActionNode(Element node) {
    }

    private List<CMD> parseCmd(NodeList list) {
        String actionName = "";
        String to         = "";
        return null;
    }

    private void parseRouters(Element node) {
    }

    private void parseRouter(Element node) {
    }

    private void parseExpression(Element node) {
    }

    class CMD {
        String action;
        String to;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ctx = applicationContext;
    }
}
