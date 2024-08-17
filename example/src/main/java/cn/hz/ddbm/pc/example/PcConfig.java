package cn.hz.ddbm.pc.example;

import cn.hz.ddbm.pc.core.Plugin;
import cn.hz.ddbm.pc.core.Profile;
import cn.hz.ddbm.pc.core.coast.Coasts;
import cn.hz.ddbm.pc.core.router.ExpressionRouter;
import cn.hz.ddbm.pc.core.support.SessionManager;
import cn.hz.ddbm.pc.core.support.StatusManager;
import cn.hz.ddbm.pc.factory.dsl.StateMachineConfig;
import cn.hz.ddbm.pc.test.support.DigestLogPluginMock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PcConfig implements StateMachineConfig<PcState> {
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
    public void transitions(Transitions<PcState> t) {
        t.router(PcState.init, Coasts.EVENT_DEFAULT, "sendAction", "sendRouter")
         //发送异常，不明确是否发送
         .router(PcState.send_failover, Coasts.EVENT_DEFAULT, "sendQueryAction", "sendRouter")
         //已发送，对方处理中
         .router(PcState.sended, Coasts.EVENT_DEFAULT, "sendQueryAction", "sendRouter")
         //校验资料是否缺失&提醒用户  & ==》依然缺，已经补充
         .router(PcState.miss_data, Coasts.EVENT_DEFAULT, "validateAndNotifyUserAction", "notifyRouter")
//                资料就绪状态，可重新发送
         .to(PcState.miss_data_fulled, Coasts.EVENT_DEFAULT, PcState.init);
    }

    @Override
    public List<ExpressionRouter> routers() {
        List<ExpressionRouter> routers = new ArrayList<>();
        routers.add(new ExpressionRouter("sendRouter",
                new ExpressionRouter.NodeExpression("sendRouter", "Math.random() < 0.7"),
                new ExpressionRouter.NodeExpression("su", "Math.random() < 0.7"),
                new ExpressionRouter.NodeExpression("fail", "Math.random() < 0.2"),
                new ExpressionRouter.NodeExpression("error", "Math.random() < 0.1")
        ));
        routers.add(new ExpressionRouter("notifyRouter",
                new ExpressionRouter.NodeExpression("notifyRouter", "Math.random() <0.7"),
                new ExpressionRouter.NodeExpression("su", "Math.random() < 0.7"),
                new ExpressionRouter.NodeExpression("fail", "Math.random() < 0.2"),
                new ExpressionRouter.NodeExpression("error", "Math.random() < 0.1")
        ));
        return routers;
    }



    @Override
    public Map<String, Profile.StepAttrs> stateAttrs() {
        return new HashMap<>();
    }

    @Override
    public Map<String, Profile.ActionAttrs> actionAttrs() {
        return new HashMap<>();
    }

    @Override
    public Profile profile() {
        Profile profile = new Profile(session(),status());
        profile.setRetry(10);
        return profile;
    }


    public String flowId() {
        return "test";
    }

    @Override
    public String describe() {
        return "test";
    }


}
