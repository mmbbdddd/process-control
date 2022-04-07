package io.ddbm.pc.factory;

import io.ddbm.pc.End;
import io.ddbm.pc.Flow;
import io.ddbm.pc.FlowBuilder;
import io.ddbm.pc.Router;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class XmlFlowReader {
    Document           doc;
    FlowBuilder        fb;
    ApplicationContext ctx;


    public XmlFlowReader(ApplicationContext ctx, File file) throws Exception {
        doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
        doc.getDocumentElement().normalize();
        this.ctx = ctx;
    }

    public Flow read() throws Exception {
        Element ele = doc.getDocumentElement();
        return parseFlow(ele);
    }

    private Flow parseFlow(Element flow) throws Exception {
        String name = flow.getAttribute("name");
        fb = new FlowBuilder(name);
        parseFlowChilds(flow.getChildNodes());
        return fb.build(ctx);
    }

    private void parseFlowChilds(NodeList list) {
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
                    parseNode((Element) node);
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
        String name = node.getAttribute("name");
        fb.addEndNode(new End(name));
    }

    private void parseNode(Element node) {
        String    name  = node.getAttribute("name");
        String    retry = node.getAttribute("retry");
        List<CMD> cmds  = parseCmd(node.getChildNodes());
        for (CMD cmd : cmds) {
            fb.onCmd(name, cmd.name, cmd.action, cmd.getRouterType(), cmd.router, cmd.failNode);
        }
    }

    private List<CMD> parseCmd(NodeList list) {
        List<CMD> cmds = new ArrayList<>();
        for (int i = 0; i < list.getLength(); i++) {
            if (list.item(i) instanceof Element node) {
                String name     = node.getAttribute("name");
                String action   = node.getAttribute("action");
                String to       = node.getAttribute("to");
                String failNode = node.getAttribute("failNode");
                String router   = node.getAttribute("router");
                cmds.add(new CMD(name, action, to, failNode, router));
            }
        }
        return cmds;
    }

    private void parseRouters(Element node) {
    }

    private void parseRouter(Element node) {
    }

    private void parseExpression(Element node) {
    }

    class CMD {
        String name;
        String action;
        String to;
        String failNode;
        String router;

        public CMD(String name, String action, String to, String failNode, String router) {
            this.name     = name;
            this.action   = action;
            this.to       = to;
            this.failNode = failNode;
            this.router   = router;
        }

        public Router.Type getRouterType() {
            if (!StringUtils.isEmpty(to)) {
                this.router = to;
                return Router.Type.NAME;
            } else {
                return Router.Type.EXPRESSION;
            }
        }
    }


}
