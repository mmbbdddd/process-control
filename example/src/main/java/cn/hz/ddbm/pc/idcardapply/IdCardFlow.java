package cn.hz.ddbm.pc.idcardapply;

import cn.hz.ddbm.pc.newcore.Plugin;
import cn.hz.ddbm.pc.newcore.config.Coast;
import cn.hz.ddbm.pc.newcore.factory.FSM;
import cn.hz.ddbm.pc.newcore.fsm.FsmFlow;

import java.util.*;


public class IdCardFlow implements FSM<IdCard> {

    @Override
    public String flowId() {
        return "idcard";
    }

    @Override
    public String describe() {
        return "身份证办理";
    }

    @Override
    public IdCard initState() {
        return IdCard.MaterialCollection;
    }

    @Override
    public IdCard suState() {
        return IdCard.Su;
    }

    @Override
    public IdCard failState() {
        return IdCard.Fail;
    }


    @Override
    public List<Plugin> plugins() {
        return Collections.emptyList();
    }

    @Override
    public Coast.SessionType session() {
        return Coast.SessionType.jvm;
    }

    @Override
    public Coast.StatusType status() {
        return Coast.StatusType.jvm;
    }

    @Override
    public void transitions(FsmFlow flow) {
//        flow.local()
//        transitions
//                .state(IdCardFSM.MaterialCollection)
//                .local(Coast.EVENT_DEFAULT, MaterialCollectionAction.class, new ToRouter<>(IdCardFSM.RuleChecked))
//                .endState()
//                .state(IdCardFSM.RuleChecked)
//                .local(Coast.EVENT_DEFAULT, RuleCheckedAction.class, new Router<>(new HashMap<String, IdCardFSM>() {{
//                    put("true", IdCardFSM.Accepted);
//                    put("false", IdCardFSM.MaterialCollection);
//                }}))
//                .endState()
//                .state(IdCardFSM.Accepted)
//                .remote(Coast.EVENT_DEFAULT, SendBizAction.class, new Router<>(
//                        new HashMap<String, IdCardFSM>() {{
//                            put("true", IdCardFSM.RuleSyncing);
//                            put("false", IdCardFSM.Accepted);
//                            put("false", IdCardFSM.MaterialCollection);
//                        }}))
//                .endState()
//                .state(IdCardFSM.RuleSyncing)
//                .remote(Coast.EVENT_DEFAULT, SendBizAction.class, new Router<>(
//                        new HashMap<String, IdCardFSM>() {{
//                            put("true", IdCardFSM.RuleChecked);
//                            put("false", IdCardFSM.RuleSyncing);
//                        }}))
//                .endState()
        ;
    }


    @Override
    public Class<IdCard> type() {
        return IdCard.class;
    }
}
