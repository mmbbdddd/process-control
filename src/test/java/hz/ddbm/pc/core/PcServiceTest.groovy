package hz.ddbm.pc.core

import hz.ddbm.pc.core.config.Coast
import hz.ddbm.pc.core.service.PcService
import hz.ddbm.pc.core.utils.ElUtils
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest

public class PcServiceTest extends Specification {
    @Autowired
    PcService pcService;

    @Test
    def "集成测试"() {
        expect: ""
        try {
            ElUtils.eval(expression, ctx) == result;
        } catch (Exception e) {
            e.getMessage() == ex
        }

        where: ""
        expression | ctx               | result | ex
        null       | new HashMap<>()   | null   | 'expression is null'
        "1+1"      | null              | 2      | null
        "1+1"      | new HashMap<>()   | 2      | null
        "a+b"      | [a: 1, b: 1]      | 2      | null
        "u.a+u.b"  | [u: [a: 1, b: 1]] | 2      | null
    }

    @Test
    def "多步测试"() {
        given: ''
        expect: ""
        pcService.executeMore("testFlow", 1, new Object(), Coast.EVENT_PUSH, null)
    }
}
