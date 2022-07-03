package io.ddbm.pc.factory;

import io.ddbm.pc.Flow;
import io.ddbm.pc.TaskNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class XmlFlowReader {
    Document    doc;
    FlowBuilder fb;


    public XmlFlowReader(File file) throws Exception {
        doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
        doc.getDocumentElement().normalize();
    }

    public Flow read() throws Exception {
        Element ele = doc.getDocumentElement();
        return parse(ele);
    }

    public Flow parse(Element flow) throws Exception {
        String name = flow.getAttribute("name");
        fb = new FlowBuilder(name);
        parseFlowChilds(flow.getChildNodes());
        return fb.build();
    }

    public void parseFlowChilds(NodeList list) {
        for (int i = 0; i < list.getLength(); i++) {
            Node   node     = list.item(i);
            String nodeName = node.getNodeName();
            switch (nodeName) {
                case "start":
                    TaskNode start = parseNode((Element) node);
                    start.setType(TaskNode.Type.START);
                    fb.addNode(start);
                    break;
                case "end":
                    TaskNode end = parseNode((Element) node);
                    end.setType(TaskNode.Type.END);
                    fb.addNode(end);
                    break;
                case "node":
                    TaskNode task = parseNode((Element) node);
                    task.setType(TaskNode.Type.TASK);
                    fb.addNode(task);
                    break;
                case "plugins":
                    parsePlugins((Element) node);
                    break;
            }
        }
    }


    public TaskNode parseNode(Element node) {
        String   name   = node.getAttribute("name");
        String   fluent = node.getAttribute("fluent");
        TaskNode task   = new TaskNode(TaskNode.Type.TASK, name, fluent);
        paserEvent(node.getChildNodes(), task);
        return task;
    }

    public void paserEvent(NodeList list, TaskNode task) {
        for (int i = 0; i < list.getLength(); i++) {
            if (list.item(i) instanceof Element) {
                Element node   = (Element) list.item(i);
                String  event  = node.getAttribute("event");
                String  action = node.getAttribute("action");
                String  retry  = node.getAttribute("retry");
                String  maybe  = node.getAttribute("maybe");
                String  desc   = node.getAttribute("desc");
                task.on(event, action, maybe, desc, retry);
            }
        }
    }

    public void parsePlugins(Element node) {
        String name = node.getAttribute("name");
        paserPlugin(node.getChildNodes());
    }


    public void paserPlugin(NodeList list) {
        for (int i = 0; i < list.getLength(); i++) {
            if (list.item(i) instanceof Element) {
                Element node = (Element) list.item(i);
                String  name = node.getAttribute("name");
                fb.addPlugin(name);
            }
        }
    }


}
