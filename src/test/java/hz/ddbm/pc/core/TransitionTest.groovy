package hz.ddbm.pc.core

import hz.ddbm.pc.core.config.Coast
import hz.ddbm.pc.core.config.NodeProperties
import hz.ddbm.pc.core.domain.Node
import hz.ddbm.pc.core.domain.Transition
import spock.lang.Specification

public class TransitionTest extends Specification {

    def "初始化节点"() {
        given:
        Transition transition = Coast.TRANSITION_PUSH_NOTHING;
        when:
        transition.init(new Node(new NodeProperties(Node.Type.START, "start"), [transition]), null)
        then:
        EmptyStackException e = noExceptionThrown()
    }
}
