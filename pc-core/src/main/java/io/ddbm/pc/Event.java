package io.ddbm.pc;

import io.ddbm.pc.actions.ChaosAction;
import io.ddbm.pc.actions.SagaAction;
import io.ddbm.pc.exception.ImpossibilityException;
import io.ddbm.pc.exception.InterruptException;
import io.ddbm.pc.exception.NoSuchEventException;
import io.ddbm.pc.exception.NoSuchNodeException;
import io.ddbm.pc.exception.NoSuchRouterException;
import io.ddbm.pc.exception.ParameterException;
import io.ddbm.pc.exception.PauseException;
import io.ddbm.pc.exception.RouterException;
import io.ddbm.pc.router.NodeRouter;
import io.ddbm.pc.support.FlowContext;
import io.ddbm.pc.utils.Pair;
import lombok.Getter;
import org.springframework.util.Assert;

import java.util.Objects;


@Getter
public class Event {
   final   Node node;
   final Flow flow;

    String event;

    String retryCount;

    String desc;

    String action;

    String router;

    String to;

    public Event(Flow flow,Node node, String event, String action, String to, String router, String desc,
                 String retryCount) {
        Assert.notNull(event, "event is null");
        Assert.notNull(node, "node is null");
        Assert.notNull(action, "action is null");
        Assert.isTrue(to != null || router != null, "to && router is null");
        this.node = node;
        this.flow = flow;
        this.event = event;
        this.router = router;
        this.action = action;
        this.to = to;
        this.desc = desc;
        this.retryCount = retryCount;

    }

    public void execute(FlowContext ctx)
        throws PauseException, InterruptException, ParameterException, NoSuchNodeException, NoSuchEventException {
        //        执行业务逻辑 
        Pair<FlowStatus, String> sourceStatus = ctx.getStatus();
        ActionRouterAdapter action = getAction(ctx.getRunMode());
        try {

            ctx.setActionError(null);
            ctx.getFlow().preAction(action, ctx);
            String targetStatus = action.execute(ctx);
            ctx.updateNodeStatus(targetStatus);
            ctx.setFlowStatus(FlowStatus.RUNNING);
            ctx.refreshEvent();
            ctx.getFlow().postAction(action, ctx);
            ctx.getFlow().statusTransition(action,sourceStatus, ctx.getStatus(), ctx);
        } catch (Throwable e) {
            //用于不可预料异常，如空指针，系统异常等。，需要开发关注  
            Pair<FlowStatus, Throwable> targetStatus = ctx.getRunMode().actionStateMachine(event, e);
            ctx.setActionError(targetStatus.getValue());
            ctx.setFlowStatus(targetStatus.getKey());
            ctx.getFlow().onActionException(action, targetStatus.getValue(), ctx);
            ctx.getFlow().statusTransition(action,sourceStatus, ctx.getStatus(), ctx);
            throw e;
        }
    }

    public Router getRouterObj(Flow flow)
        throws NoSuchRouterException, ImpossibilityException {
        if (Objects.nonNull(to)) {
            return new NodeRouter(to);
        }
        if (Objects.nonNull(router)) {
            Router routerObj = flow.routers.get(router);
            if (Objects.nonNull(routerObj)) {
                return routerObj;
            } else {
                throw new NoSuchRouterException(router);
            }
        }
        //不可能发生的异常
        throw new ImpossibilityException("to && router is null");
    }

    private ActionRouterAdapter getAction(RunMode runMode) {
        Action actionObj ;
        Router routerObj  = getRouterObj(this.flow);
        if (Objects.equals(runMode, RunMode.CHAOS)) {
            actionObj = new ChaosAction(this.action);
        } else {
            actionObj = Action.dsl(this.action); 
        }
        return new ActionRouterAdapter(actionObj, routerObj);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Event command = (Event)o;
        return Objects.equals(event, command.event) && Objects.equals(node, command.node);
    }

    @Override
    public int hashCode() {
        return Objects.hash(event, node);
    }
}
    class ActionRouterAdapter implements Action {
    protected Action action;

    protected Router router;

    public ActionRouterAdapter(Action action, Router router) {
        this.action = action;
        this.router = router;
    }

    public String execute(FlowContext ctx)
        throws InterruptException, PauseException, ParameterException, NoSuchEventException, NoSuchNodeException {
        try {
            preAction(ctx);
            this.run(ctx);
            return this.router.route(ctx);
        } catch (RouterException e) {
            throw new PauseException(ctx.getFlow().getName(), ctx.getNode(), e.getMessage(), e);
        } finally {
            postAction(ctx);
        }
    }

    @Override
    public void run(FlowContext ctx) throws InterruptException, PauseException, ParameterException, NoSuchEventException, NoSuchNodeException{
        this.action.run(ctx);
    }
 

    public void postAction(FlowContext ctx) {
        //子类可继承&扩展

    }

    public void preAction(FlowContext ctx) {
        //子类可继承&扩展
        if(this.action instanceof SagaAction){
            ((SagaAction)this.action).preAction(ctx);
        }
    }

    public  String name(){
        return this.action.name();
    }

//    static ActionRouterAdapter build(Event event, Action action, Router router) {
//        ActionRouterAdapter actionRouter = null;
//        //判断是否支持幂等
//        if(action.support(IdempotentActionProxy.IdempotentAction.class )){
//            action = new IdempotentActionProxy(action,router);
//        }else {
//            action = new RepeatableActionProxy(action,router);
//        }
//        //判断是否支持事务
//        if(action.support(SagaAction.class)){
//            action = new SagaActionAdapter();
//        }
//        return actionRouter;
//    }

}