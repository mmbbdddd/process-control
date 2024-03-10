package io.ddbm.pc.actions;

import io.ddbm.pc.Action;
import io.ddbm.pc.support.FlowContext;


public interface SagaAction extends Action {

       void preAction(FlowContext ctx);
}
