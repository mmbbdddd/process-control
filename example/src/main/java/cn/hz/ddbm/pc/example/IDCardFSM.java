package cn.hz.ddbm.pc.example;

import cn.hz.ddbm.pc.core.Node;
import cn.hz.ddbm.pc.core.Plugin;
import cn.hz.ddbm.pc.core.Profile;
import cn.hz.ddbm.pc.core.coast.Coasts;
import cn.hz.ddbm.pc.core.support.SessionManager;
import cn.hz.ddbm.pc.core.support.StatusManager;
import cn.hz.ddbm.pc.core.utils.InfraUtils;
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
        plugins.add(new DigestLogPluginMock());
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
    public Map<IDCardState, Node.Type> nodes() {
        Map<IDCardState, Node.Type> map = new HashMap<>();
        map.put(IDCardState.init, Node.Type.START);
        map.put(IDCardState.sended, Node.Type.TASK);
        map.put(IDCardState.send_failover, Node.Type.TASK);
        map.put(IDCardState.miss_data, Node.Type.TASK);
        map.put(IDCardState.miss_data_fulled, Node.Type.TASK);
        map.put(IDCardState.su, Node.Type.END);
        map.put(IDCardState.fail, Node.Type.END);
        map.put(IDCardState.error, Node.Type.END);
        return map;
    }

    @Override
    public Map<String, Set<IDCardState>> maybeResults(Map<String, Set<IDCardState>> map) {
        map.put("sendRouter", Sets.newSet(IDCardState.init, IDCardState.sended, IDCardState.miss_data, IDCardState.su, IDCardState.fail));
        map.put("notifyRouter", Sets.newSet(IDCardState.init, IDCardState.miss_data, IDCardState.miss_data_fulled));
        return map;
    }

    @Override
    public void transitions(Transitions<IDCardState> t) {
        t.saga(IDCardState.init, Coasts.EVENT_DEFAULT, IDCardState.send_failover, "sendAction", "sendRouter")
         //发送异常，不明确是否发送
         .router(IDCardState.send_failover, Coasts.EVENT_DEFAULT, "sendQueryAction", "sendRouter")
         //已发送，对方处理中
         .router(IDCardState.sended, Coasts.EVENT_DEFAULT, "sendQueryAction", "sendRouter")
         //校验资料是否缺失&提醒用户  & ==》依然缺，已经补充
         .router(IDCardState.miss_data, Coasts.EVENT_DEFAULT, "validateAndNotifyUserAction", "notifyRouter")
//                资料就绪状态，可重新发送
         .to(IDCardState.miss_data_fulled, Coasts.EVENT_DEFAULT, IDCardState.init);
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
        profile.setRetry(5);
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
