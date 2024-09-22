package cn.hz.ddbm.pc.fsm;

import cn.hz.ddbm.pc.common.lang.Tetrad;
import cn.hz.ddbm.pc.fsm.actions.MaterialCollectionAction;
import cn.hz.ddbm.pc.fsm.actions.RuleCheckedAction;
import cn.hz.ddbm.pc.fsm.actions.SendBizAction;
import cn.hz.ddbm.pc.newcore.FlowAttrs;
import cn.hz.ddbm.pc.newcore.Plugin;
import cn.hz.ddbm.pc.newcore.StateAttrs;
import cn.hz.ddbm.pc.newcore.config.Coast;
import cn.hz.ddbm.pc.newcore.factory.FSM;
import cn.hz.ddbm.pc.newcore.fsm.FsmAction;
import cn.hz.ddbm.pc.newcore.fsm.Router;
import cn.hz.ddbm.pc.newcore.fsm.routers.ToRouter;
import cn.hz.ddbm.pc.newcore.plugins.FsmDigestPlugin;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    public FlowAttrs flowAttrs() {
        return FlowAttrs.chaosOf();
    }

    @Override
    public Map<String, StateAttrs> stateAttrs() {
        return new HashMap<String, StateAttrs>() {{
            put(IdCard.Init.name(), StateAttrs.ChaosOf());
            put(IdCard.RuleChecked.name(), StateAttrs.ChaosOf());
            put(IdCard.Accepted.name(), StateAttrs.ChaosOf());
            put(IdCard.RuleSyncing.name(), StateAttrs.ChaosOf());
        }};
    }


    @Override
    public List<Plugin> plugins() {
        return Lists.newArrayList(
                new FsmDigestPlugin()
        );
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
                            put("false", IdCard.Su);
                            put("false", IdCard.Fail);
                        }})),
                Tetrad.of(IdCard.RuleSyncing, "push", SendBizAction.class, new Router<>(
                        new HashMap<String, IdCard>() {{
                            put("true", IdCard.RuleChecked);
                            put("false", IdCard.RuleSyncing);
                        }})
                )
        );
    }

}
