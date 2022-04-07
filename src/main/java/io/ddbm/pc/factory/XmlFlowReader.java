package io.ddbm.pc.factory;

import io.ddbm.pc.Flow;
import io.ddbm.pc.FlowBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
        return parseFlow(ele);
    }

    private Flow parseFlow(Element flow) throws Exception {
        String name = flow.getAttribute("name");
        fb = new FlowBuilder(name);
        parseNode((Element) flow.getChildNodes());
        return fb.build(null);
    }

    private Flow parseNode(Element flow) {
        return null;
    }

    private Flow parseStart(Element flow) {
        return null;
    }

    private Flow parseEnd(Element flow) {
        return null;
    }

    private Flow parseActionNode(Element flow) {
        return null;
    }

    private Flow parseCmd(Element flow) {
        return null;
    }

    private Flow parseRouter(Element flow) {
        return null;
    }


}
