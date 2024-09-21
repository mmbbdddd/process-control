package cn.hz.ddbm.pc.fsm;

import cn.hz.ddbm.pc.newcore.Plugin;
import cn.hz.ddbm.pc.newcore.config.Coast;
import cn.hz.ddbm.pc.newcore.factory.FSM;
import cn.hz.ddbm.pc.newcore.fsm.FsmFlow;

import java.util.ArrayList;
import java.util.List;


public class IdCardFsm implements FSM<IdCardState> {

    @Override
    public String flowId() {
        return "test";
    }

    @Override
    public String describe() {
        return "fsm";
    }

    @Override
    public IdCardState initState() {
        return IdCardState.init;
    }

    @Override
    public IdCardState suState() {
        return IdCardState.su;
    }

    @Override
    public IdCardState failState() {
        return IdCardState.fail;
    }

    @Override
    public List<Plugin> plugins() {
        return new ArrayList<Plugin>() {{
//            add(new FsmDigestPlugin());
        }};
    }

    @Override
    public Coast.SessionType session() {
        return null;
    }

    @Override
    public Coast.StatusType status() {
        return null;
    }

    @Override
    public void transitions(FsmFlow flow) {

    }


//    @Override
//    public void transitions(Transitions<IdCardState> transitions) {
//        transitions.state(IdCardState.init)
//                .local(Coast.EVENT_DEFAULT, LocalFsmAction.class, new ToRouter<>(IdCardState.presend))
//                .endState()
//                .state(IdCardState.presend)
//                .local(Coast.EVENT_DEFAULT, LocalFsmAction.class, new ToRouter<>(IdCardState.auditing))
//                .endState()
//                .state(IdCardState.auditing)
//                .local(Coast.EVENT_DEFAULT, LocalFsmAction.class, new Router<>(new RowKeyTable<String, IdCardState, Double>() {{
//                    put("result.code == '0000'", IdCardState.su, 1.0);
//                    put("result.code == '0001'", IdCardState.fail, 0.1);
//                    put("result.code == '0002'", IdCardState.no_such_order, 0.1);
//                    put("result.code == '0003'", IdCardState.lost_date, 0.1);
//                }}))
//                .endState()
//                .state(IdCardState.no_such_order)
//                .local(Coast.EVENT_DEFAULT, LocalFsmAction.class, new ToRouter<>(IdCardState.presend))
//                .endState()
//                .state(IdCardState.lost_date)
//                .local(Coast.EVENT_DEFAULT, LocalFsmAction.class, new ToRouter<>(IdCardState.init))
//                .endState()
//        ;
//    }


    @Override
    public Class<IdCardState> type() {
        return IdCardState.class;
    }
}
