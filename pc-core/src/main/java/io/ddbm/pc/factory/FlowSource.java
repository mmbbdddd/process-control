package io.ddbm.pc.factory;

import java.io.InputStream;


public class FlowSource {
    InputStream flowDefineXmlStream;

    public FlowSource(InputStream content) {
        this.flowDefineXmlStream = content;
    }

    public InputStream getFlowDefineXmlStream() {
        return flowDefineXmlStream;
    }
}
