package cn.hz.ddbm.pc.example;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.map.multi.Table;
import cn.hz.ddbm.pc.core.Node;
import cn.hz.ddbm.pc.core.Plugin;
import cn.hz.ddbm.pc.core.Profile;
import cn.hz.ddbm.pc.core.coast.Coasts;
import cn.hz.ddbm.pc.core.enums.FlowStatus;
import cn.hz.ddbm.pc.core.support.SessionManager;
import cn.hz.ddbm.pc.core.support.StatusManager;
import cn.hz.ddbm.pc.core.utils.InfraUtils;
import cn.hz.ddbm.pc.example.actions.PayAction;
import cn.hz.ddbm.pc.example.actions.PayQueryAction;
import cn.hz.ddbm.pc.example.actions.SendAction;
import cn.hz.ddbm.pc.example.actions.SendQueryAction;
import cn.hz.ddbm.pc.factory.dsl.FSM;
import cn.hz.ddbm.pc.profile.PcService;
import cn.hz.ddbm.pc.support.DigestLogPluginMock;
import org.mockito.internal.util.collections.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.*;


public class IDCardFSM implements FSM<IDCardState>, InitializingBean {
    Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public List<Plugin> plugins() {
        List<Plugin> plugins = new ArrayList<Plugin>();
//        plugins.add(new DigestLogPluginMock());
//        plugins.add(new PayAction());
//        plugins.add(new PayQueryAction());
//        plugins.add(new SendAction());
//        plugins.add(new SendQueryAction());
        return plugins;
    }

    @Override
    public SessionManager.Type session() {
        return SessionManager.Type.memory;
    }

    @Override
    public StatusManager.Type status() {
        return StatusManager.Type.memory;
    }

    @Override
    public Map<IDCardState, FlowStatus> nodes() {
        Map<IDCardState, FlowStatus> map = new HashMap<>();
        map.put(IDCardState.init, FlowStatus.INIT);
        map.put(IDCardState.payed, FlowStatus.RUNNABLE);
        map.put(IDCardState.sended, FlowStatus.RUNNABLE);
        map.put(IDCardState.payed_failover, FlowStatus.RUNNABLE);
        map.put(IDCardState.sended_failover, FlowStatus.RUNNABLE);
        map.put(IDCardState.su, FlowStatus.FINISH);
        map.put(IDCardState.fail, FlowStatus.FINISH);
        map.put(IDCardState.error, FlowStatus.FINISH);
        return map;
    }

    @Override
    public Table<IDCardState, String, Set<Pair<IDCardState, Double>>> maybeResults(Table<IDCardState, String, Set<Pair<IDCardState, Double>>> table) {
        table.put(IDCardState.init, Coasts.EVENT_DEFAULT, Sets.newSet(
                Pair.of(IDCardState.payed_failover, 0.1),
                Pair.of(IDCardState.payed_failover, 0.9)));
        table.put(IDCardState.payed_failover, Coasts.EVENT_DEFAULT, Sets.newSet(
                Pair.of(IDCardState.payed_failover, 0.1),
                Pair.of(IDCardState.init, 0.1),
                Pair.of(IDCardState.payed, 0.8)
        ));
        table.put(IDCardState.payed, Coasts.EVENT_DEFAULT, Sets.newSet(
                Pair.of(IDCardState.sended_failover, 0.1),
                Pair.of(IDCardState.su, 0.7),
                Pair.of(IDCardState.fail, 0.1),
                Pair.of(IDCardState.sended, 0.1)
        ));
        table.put(IDCardState.sended_failover, Coasts.EVENT_DEFAULT, Sets.newSet(
                Pair.of(IDCardState.sended_failover, 0.1),
                Pair.of(IDCardState.su, 0.7),
                Pair.of(IDCardState.fail, 0.1),
                Pair.of(IDCardState.sended, 0.1)
        ));
        table.put(IDCardState.sended, Coasts.EVENT_DEFAULT, Sets.newSet(
                Pair.of(IDCardState.sended_failover, 0.1),
                Pair.of(IDCardState.su, 0.7),
                Pair.of(IDCardState.fail, 0.1),
                Pair.of(IDCardState.sended, 0.1)
        ));
        return table;
    }

//    @Override
//    public Map<String, Set<IDCardState>> maybeResults(Map<String, Set<IDCardState>> map) {
//        map.put("payRouter", Sets.newSet(IDCardState.payed_failover,IDCardState.init, IDCardState.payed));
//        map.put("sendRouter", Sets.newSet(IDCardState.sended_failover,IDCardState.init,IDCardState.sended, IDCardState.su, IDCardState.fail));
//        return map;
//    }

    @Override
    public void transitions(Transitions<IDCardState> t) {
//        payAction:执行本地扣款
        t.saga(IDCardState.init, Coasts.EVENT_DEFAULT, IDCardState.payed_failover, "payAction", "payRouter")
                //本地扣款容错payQueryAction 扣款结果查询
                .router(IDCardState.payed_failover, Coasts.EVENT_DEFAULT, "payQueryAction", "payRouter")
                //发送异常，不明确是否发送
                .saga(IDCardState.payed, Coasts.EVENT_DEFAULT, IDCardState.sended_failover, "sendAction", "sendRouter")
                .router(IDCardState.sended_failover, Coasts.EVENT_DEFAULT, "sendQueryAction", "sendRouter")
                //sendAction，执行远程发生&sendQueryAction。
                .router(IDCardState.sended, Coasts.EVENT_DEFAULT, "sendQueryAction", "sendRouter");
    }


    @Override
    public Map<IDCardState, Profile.StepAttrs> stateAttrs() {
        return new HashMap<>();
    }

    @Override
    public Map<String, Profile.ActionAttrs> actionAttrs() {
        return new HashMap<>();
    }

    @Override
    public Profile<IDCardState> profile() {
        Profile<IDCardState> profile = new Profile(session(), status());
        profile.setRetry(2);
        return profile;
    }


    public String flowId() {
        return "test";
    }

    @Override
    public String describe() {
        return "test";
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        InfraUtils.getBean(PcService.class).addFlow(build());
    }
}
