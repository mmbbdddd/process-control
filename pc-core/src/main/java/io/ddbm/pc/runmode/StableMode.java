package io.ddbm.pc.runmode;

import io.ddbm.pc.FlowStatus;
import io.ddbm.pc.Node;
import io.ddbm.pc.RunMode;
import io.ddbm.pc.config.Coast;
import io.ddbm.pc.exception.InterruptException;
import io.ddbm.pc.exception.MockException;
import io.ddbm.pc.exception.NoSuchNodeException;
import io.ddbm.pc.exception.NoSuchRouterException;
import io.ddbm.pc.exception.ParameterException;
import io.ddbm.pc.exception.PauseException;
import io.ddbm.pc.exception.RouterException;
import io.ddbm.pc.support.FlowContext;
import io.ddbm.pc.utils.Pair;

import java.util.Objects;


public class StableMode extends RunMode {
    public StableMode(String desc) {
        super(desc);
    }

    @Override
    public boolean isRun(FlowContext ctx) {
        //          流程状态为结束，暂停 ，取消
        if (Objects.equals(FlowStatus.DONE, ctx.getStatus().getKey()) || Objects.equals(FlowStatus.PAUSE_BUG,
            ctx.getStatus().getKey()) || Objects.equals(FlowStatus.CANCELED, ctx.getStatus().getKey())) {
            return false;
        }
        //节点不存在，或者节点为结束节点
        try {
            Node node = ctx.node();
            if (Objects.equals(Node.Type.END, node.getType())) {
                return false;
            }
        } catch (NoSuchNodeException e) {
            //todo
            e.printStackTrace();
            return false;
        }
        //        //任务异常，并且异常为1，参数异常|中断异常（需要用户处理） 2，暂停异常
        //        if (null != actionResult && actionResult.isInterrupted()) {
        //            return false;
        //        } 
        return true;

    }

    @Override
    public Pair<FlowStatus, Throwable> actionStateMachine(String event, Throwable e) {
        if (Objects.equals(event, Coast.PAUSE_EVENT)) {
            return Pair.of(FlowStatus.PAUSE_BUG, null);
        } else if (Objects.equals(event, Coast.CANCEL_EVENT)) {
            return Pair.of(FlowStatus.PAUSE_BUG, null);
        } else {
            if (null == e) {
                return Pair.of(FlowStatus.RUNNING, null);
            } else if (e instanceof ParameterException) {
                return Pair.of(FlowStatus.PAUSE_MISTAKE, e);
            } else if (e instanceof PauseException) {
                return Pair.of(FlowStatus.PAUSE_BUG, e);
            } else if (e instanceof InterruptException) {
                return Pair.of(FlowStatus.RUNNING, e);
            } else if (e instanceof NoSuchRouterException) {
                return Pair.of(FlowStatus.PAUSE_BUG, e);
            } else if (e instanceof RouterException) {
                return Pair.of(FlowStatus.PAUSE_BUG, e);
            } else if (e instanceof MockException) {
                return Pair.of(FlowStatus.RUNNING, e.getCause());
            } else if (e instanceof RuntimeException) {
                return Pair.of(FlowStatus.RUNNING, e);
            } else {
                return Pair.of(FlowStatus.RUNNING, e);
            }
        }
    }
}
