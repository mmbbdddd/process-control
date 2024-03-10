package io.ddbm.pc;

import io.ddbm.pc.runmode.ChaosMode;
import io.ddbm.pc.runmode.StableMode;
import io.ddbm.pc.support.FlowContext;
import io.ddbm.pc.utils.Pair;


public abstract class RunMode {
    protected String desc;

    public RunMode(String desc) {
        this.desc = desc;
    }

    public abstract boolean isRun(FlowContext ctx);

    public abstract Pair<FlowStatus, Throwable> actionStateMachine(String event, Throwable e);

    public static RunMode CHAOS = new ChaosMode("混沌模式");

    public static RunMode STABLE = new StableMode("稳定模式");

}
