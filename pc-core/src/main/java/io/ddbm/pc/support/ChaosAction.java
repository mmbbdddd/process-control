package io.ddbm.pc.support;

import io.ddbm.pc.Action;
import io.ddbm.pc.FlowContext;
import io.ddbm.pc.exception.InterruptException;
import io.ddbm.pc.exception.PauseException;

public class ChaosAction implements Action {
    @Override
    public void execute(FlowContext ctx) throws PauseException, InterruptException {
        Double d = Math.random();
        if (d < 0.1) {
            throw new PauseException(ctx, "随机出个暂停异常");
        } else if (d < 0.2) {
            throw new InterruptException("随机出个中断异常", ctx.getRequest().getStatus());
        } else if (d < 0.3) {
            throw new RuntimeException("随机出个runtime异常");
        } else if (d < 0.4) {
            ctx.getRequest().setStatus("_randomNode");
        } else {
//                        随机出个目标节点
            String chaoNode = ctx.chaosNode();
            ctx.getRequest().setStatus(chaoNode);
        }
    }
}
