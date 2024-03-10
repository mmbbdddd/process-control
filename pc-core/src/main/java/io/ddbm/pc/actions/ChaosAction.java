package io.ddbm.pc.actions;

import io.ddbm.pc.Action;
import io.ddbm.pc.Event;
import io.ddbm.pc.chaos.ChaosService;
import io.ddbm.pc.exception.InterruptException;
import io.ddbm.pc.exception.MockException;
import io.ddbm.pc.exception.NoSuchEventException;
import io.ddbm.pc.exception.NoSuchNodeException;
import io.ddbm.pc.exception.ParameterException;
import io.ddbm.pc.exception.PauseException;
import io.ddbm.pc.support.FlowContext;
import io.ddbm.pc.utils.SpringUtils;


public class ChaosAction implements Action {
    private String name;
    
    public ChaosAction(String name){
        this.name = name+"@chaos";
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public void run(FlowContext ctx) throws InterruptException, PauseException, ParameterException,
                                            NoSuchEventException, NoSuchNodeException {
        Event event = ctx.node().getEvent(ctx.getRequest().getEvent());
        try {
            SpringUtils.getBean(ChaosService.class).mockActionResult(event, ctx);
        } catch (InterruptException|ParameterException|PauseException|NoSuchEventException|NoSuchNodeException e) {
            throw e;
        }catch (Throwable e) {
            throw new MockException(ctx.getFlow().getName(), ctx.getNode(),e);
        } 
    }
 
}
