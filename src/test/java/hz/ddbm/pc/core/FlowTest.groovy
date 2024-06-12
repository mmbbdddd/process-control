package hz.ddbm.pc.core


import hz.ddbm.pc.core.config.FlowProperties
import hz.ddbm.pc.core.config.NodeProperties
import hz.ddbm.pc.core.config.PcConfiguration
import hz.ddbm.pc.core.domain.Flow
import hz.ddbm.pc.core.domain.Node
import hz.ddbm.pc.core.utils.InfraUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import spock.lang.Specification

@SpringBootTest
@Import(PcConfiguration.class)
public class FlowTest extends Specification {

    @Autowired
    InfraUtils infraUtils;

    def "初始化流程"() {
        given:
        Flow flow = new Flow(
                new FlowProperties(
                        "testFlow"
                ),
                [
                        new Node(new NodeProperties(Node.Type.START, "start"), []),
                        new Node(new NodeProperties(Node.Type.END, "end"), [])
                ],
                Collections.emptyList(), Collections.emptyList()
        )
        when:
        flow.init()
        then:
        EmptyStackException e = noExceptionThrown()
    }
}
