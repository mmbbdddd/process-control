package io.ddbm.pc;

import java.util.HashMap;
import java.util.Map;

public class MockAction implements Action{
    @Override
    public void execute(FlowContext ctx) throws Exception {
        Map<String,Object> result = new HashMap<>();
        result.put("code","0000");
        ctx.setActionResult(result);
    }

}
