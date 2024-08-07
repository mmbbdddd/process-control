package hz.ddbm.pc.core.config;

import hz.ddbm.pc.core.fsm.core.Node;
import lombok.Getter;

@Getter
public class NodeProperties {
    Node.Type type;
    String    name;

    public NodeProperties(Node.Type type, String name) {
        this.type = type;
        this.name = name;
    }
}
