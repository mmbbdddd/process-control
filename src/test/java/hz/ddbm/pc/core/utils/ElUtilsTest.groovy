package hz.ddbm.pc.core.utils


import org.junit.Assert
import org.junit.Test

public class ElUtilsTest {
    @Test
    void eval() {
        Assert.assertTrue(ElUtils.eval("1+1", null) == 2)
        Assert.assertTrue(ElUtils.eval("1+1", null) == 2)
        Assert.assertTrue(ElUtils.eval("a+b", [a: 1, b: 1]) == 2)
        Assert.assertTrue(ElUtils.eval("u.a+u.b", [u: [a: 1, b: 1]]) == 2)

    }

    @Test
    void evalBoolean() {
        try {
            Assert.assertTrue(ElUtils.evalBoolean("1+1", null))
        }catch(Exception e){

        }
        Assert.assertTrue(ElUtils.evalBoolean("true", null))
        Assert.assertTrue(ElUtils.evalBoolean("actionResult.code == '0001'", [actionResult: [code:'0001']]))
    }


}
