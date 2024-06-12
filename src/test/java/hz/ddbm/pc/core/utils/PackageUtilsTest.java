package hz.ddbm.pc.core.utils;

import hz.ddbm.pc.core.domain.Action;
import hz.ddbm.pc.core.factory.fsm.Fsm;
import org.junit.Test;

public class PackageUtilsTest {

    @Test
    public void scan() {
        try {
            PackageUtils.scan("hz.ddbm.jfsm", Action.class, Fsm.class).forEach(t -> {

                System.out.println(t);

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}