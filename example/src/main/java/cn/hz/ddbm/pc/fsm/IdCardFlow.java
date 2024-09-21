package cn.hz.ddbm.pc.fsm;

import cn.hz.ddbm.pc.common.lang.Tetrad;
import cn.hz.ddbm.pc.fsm.actions.MaterialCollectionAction;
import cn.hz.ddbm.pc.fsm.actions.RuleCheckedAction;
import cn.hz.ddbm.pc.fsm.actions.SendBizAction;
import cn.hz.ddbm.pc.newcore.Plugin;
import cn.hz.ddbm.pc.newcore.config.Coast;
import cn.hz.ddbm.pc.newcore.factory.FSM;
import cn.hz.ddbm.pc.newcore.fsm.FsmAction;
import cn.hz.ddbm.pc.newcore.fsm.Router;
import cn.hz.ddbm.pc.newcore.fsm.routers.ToRouter;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;


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
        return IdCard.Init;
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
    public List<Tetrad<IdCard, String, Class<? extends FsmAction>, Router<IdCard>>> transitions() {
        return Lists.newArrayList(
                Tetrad.of(IdCard.Init, "push", MaterialCollectionAction.class, new ToRouter<>(IdCard.RuleChecked)),
                Tetrad.of(IdCard.RuleChecked, "push", RuleCheckedAction.class, new Router<>(new HashMap<String, IdCard>() {{
                    put("true", IdCard.Accepted);
                    put("false", IdCard.Init);
                }})),
                Tetrad.of(IdCard.Accepted, "push", SendBizAction.class, new Router<>(
                        new HashMap<String, IdCard>() {{
                            put("true", IdCard.RuleSyncing);
                            put("false", IdCard.Accepted);
                            put("false", IdCard.Init);
                        }})),
                Tetrad.of(IdCard.RuleSyncing, "push", SendBizAction.class, new Router<>(
                        new HashMap<String, IdCard>() {{
                            put("true", IdCard.RuleChecked);
                            put("false", IdCard.RuleSyncing);
                        }})
                )
        );
    }


    @Override
    public Class<IdCard> type() {
        return IdCard.class;
    }
}
