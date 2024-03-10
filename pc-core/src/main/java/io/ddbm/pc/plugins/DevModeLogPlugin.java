package io.ddbm.pc.plugins;
 
import io.ddbm.pc.Action;
import io.ddbm.pc.FlowRequest;
import io.ddbm.pc.FlowStatus;
import io.ddbm.pc.Plugin;
import io.ddbm.pc.Router;
import io.ddbm.pc.exception.ContextCreateException;
import io.ddbm.pc.exception.InterruptException;
import io.ddbm.pc.exception.NoSuchEventException;
import io.ddbm.pc.exception.NoSuchNodeException;
import io.ddbm.pc.exception.ParameterException;
import io.ddbm.pc.exception.PauseException;
import io.ddbm.pc.exception.SessionException;
import io.ddbm.pc.status.StatusException;
import io.ddbm.pc.support.FlowContext;
import io.ddbm.pc.utils.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DevModeLogPlugin implements Plugin {
    Logger logger = LoggerFactory.getLogger("dev");

    @Override
    public void preAction(Action action, FlowContext ctx) {

    }

    @Override
    public void postAction(Action action, FlowContext ctx) {

    }

    @Override
    public void onPauseException(Action action, PauseException e, FlowContext ctx) {
        String flowName = ctx.getFlow().getName();
        FlowRequest<?> request = ctx.getRequest();
        logger.error("{},{}:", flowName, request.getDateId(), e);
    }

    @Override
    public void onInterruptException(Action action, InterruptException e, FlowContext ctx) {
        String flowName = ctx.getFlow().getName();
        FlowRequest<?> request = ctx.getRequest();
        logger.error("{},{}:", flowName, request.getDateId(), e);
    }

    @Override
    public void onArgumentException(Action action, ParameterException e, FlowContext ctx) {
        String flowName = ctx.getFlow().getName();
        FlowRequest<?> request = ctx.getRequest();
        logger.error("{},{}:", flowName, request.getDateId(), e);
    }

    @Override
    public void onRuntimeException(Action action, RuntimeException e, FlowContext ctx) {
        String flowName = ctx.getFlow().getName();
        FlowRequest<?> request = ctx.getRequest();
        logger.error("{},{}:", flowName, request.getDateId(), e);
    }

    @Override
    public void onThrowable(Action action, Throwable e, FlowContext ctx) {
        String flowName = ctx.getFlow().getName();
        FlowRequest<?> request = ctx.getRequest();
        logger.error("{},{}:", flowName, request.getDateId(), e);
    }

    @Override
    public void onStatusTransition(
        Action action, Pair<FlowStatus, String> sourceStatus, Pair<FlowStatus, String> targetStatus, FlowContext ctx) {
        String flowName = ctx.getFlow().getName();
        FlowRequest<?> request = ctx.getRequest();
        logger.info("订单状态迁移：{},{},{},{},{},{},{}", flowName, request.getDateId(), sourceStatus, request.getEvent(),
            action.name(),
            targetStatus, ctx.getActionError() != null ? ctx.getActionError() : "");
    }

 

    @Override
    public void onStateUpdate(FlowContext ctx) {

    }

    @Override
    public void onPluginFlowException(Throwable t, FlowContext ctx) {
        String flowName = ctx.getFlow().getName();
        FlowRequest<?> request = ctx.getRequest();
        logger.error("{},{}:", flowName, request.getDateId(), t);
    }

    @Override
    public void onPluginIoException(Throwable e, FlowRequest<?> request) {
        logger.error("{},{}:", request.getFlowName(), request.getDateId(), e);
    }

    @Override
    public void onOtherIoException(Exception e, FlowRequest<?> request) {
        logger.error("{},{}:", request.getFlowName(), request.getDateId(), e);
    }

    @Override
    public void onStatusException(StatusException e, FlowRequest<?> request) {
        logger.error("{},{}:", request.getFlowName(), request.getDateId(), e);
    }

    @Override
    public void onSessionException(SessionException e, FlowRequest<?> request) {
        logger.error("{},{}:", request.getFlowName(), request.getDateId(), e);
    }

    @Override
    public void onContextCreateException(ContextCreateException e, FlowRequest<?> request) {
        logger.error("{},{}:", request.getFlowName(), request.getDateId(), e);
    }

    @Override
    public void onNoSuchNodeException(NoSuchNodeException e, FlowContext ctx) {
        String flowName = ctx.getFlow().getName();
        FlowRequest<?> request = ctx.getRequest();
        logger.error("{},{}:", flowName, request.getDateId(), e);
    }

    @Override
    public void onNoSuchEventException(NoSuchEventException e, FlowContext ctx) {
        String flowName = ctx.getFlow().getName();
        FlowRequest<?> request = ctx.getRequest();
        logger.error("{},{}:", flowName, request.getDateId(), e);
    }
}
