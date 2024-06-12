package hz.ddbm.pc.core

import hz.ddbm.pc.core.config.Coast
import hz.ddbm.pc.core.config.NodeProperties
import hz.ddbm.pc.core.domain.Node
import org.assertj.core.util.Lists
import spock.lang.Specification

public class NodeTest extends Specification {

    def "初始化节点"() {
        given:
        Node start = new Node(new NodeProperties(Node.Type.START, "start"), [Coast.TRANSITION_PUSH_NOTHING])
        Node task = new Node(new NodeProperties(Node.Type.TASK, "task"), Lists.emptyList())
        Node end = new Node(new NodeProperties(Node.Type.END, "end"), Lists.emptyList())
        when:
        start.init(null)
        task.init(null)
        end.init(null)
        then:
        EmptyStackException e = noExceptionThrown()
    }
}
