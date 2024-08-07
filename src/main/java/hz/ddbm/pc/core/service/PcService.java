package hz.ddbm.pc.core.service;

import hz.ddbm.pc.core.config.Coast;
import hz.ddbm.pc.core.config.PcProperties;
import hz.ddbm.pc.core.fsm.core.Action;
import hz.ddbm.pc.core.fsm.core.BizContext;
import hz.ddbm.pc.core.fsm.core.Flow;
import hz.ddbm.pc.core.factory.fsm.Fsm;
import hz.ddbm.pc.core.service.session.FlowNodeStatus;
import hz.ddbm.pc.core.utils.InfraUtils;
import hz.ddbm.pc.core.utils.PackageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class PcService implements InitializingBean, ApplicationContextAware {
    @Autowired
    PcProperties  pcProperties;
    @Autowired
    StatusService statusService;
    GenericApplicationContext       applicationContext;
    ConcurrentHashMap<String, Flow> flows = new ConcurrentHashMap<>();


    /**
     * 初始化工作流定义
     */
    private void initFlows() {
        initFsmFlows();
        initXmlFlows();
    }

    /**
     * 执行工作流：运行一次
     *
     * @param flowName
     * @param id
     * @param data
     * @param event
     * @param args
     * @param <T>
     */
    public <T> void executeOnce(String flowName, Serializable id, T data, String event, Map<String, Object> args) {
        Assert.notNull(flowName, "flowName is null");
        Assert.notNull(id, "id is null");
        Assert.notNull(data, "data is null");
        Assert.notNull(event, "event is null");
        Flow flow = flows.get(flowName);
        Assert.notNull(flow, "no such flow define");
        BizContext<T> ctx = getOrCreateContext(flow, id, data, event, args);
        flow.execute(ctx);
    }

    /**
     * 执行工作流：运行直到中断
     *
     * @param flowName
     * @param id
     * @param data
     * @param event
     * @param args
     * @param <T>
     */
    public <T> void executeMore(String flowName, Serializable id, T data, String event, Map<String, Object> args) {
        Assert.notNull(flowName, "flowName is null");
        Assert.notNull(id, "id is null");
        Assert.notNull(data, "data is null");
        Assert.notNull(event, "event is null");
        Flow flow = flows.get(flowName);
        Assert.notNull(flow, "no such flow define");
        BizContext<T> ctx = getOrCreateContext(flow, id, data, event, args);
        executeMore(ctx);
    }

    public <T> void chaos(String flowName) {
        System.setProperty(Coast.IS_CHAOS, "true");

        System.getProperties().remove(Coast.IS_CHAOS);
    }

    private <T> void executeMore(BizContext<T> ctx) {
        try {
            ctx.getFlow().execute(ctx);
            if (ctx.isContinue()) {
                executeMore(ctx);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 构建路由上下文
     */
    public <T> Map<String, Object> buildRouterContext(BizContext<T> ctx) {
        Map<String, Object> rCtx = new HashMap<>();
        rCtx.put("flow", ctx.getFlow());
        rCtx.put("node", ctx.getNode());
        rCtx.put("flowStatus", ctx.getFlowStatus());
        rCtx.put("action", ctx.getTransition().getAction().beanName());
        rCtx.put("actionResult", ctx.getActionResult());
        rCtx.put("actionError", ctx.getActionError());
        return rCtx;
    }


    /////////
    private <T> BizContext<T> getOrCreateContext(Flow flow, Serializable id, T data, String event, Map<String, Object> args) {
        //todo
        FlowNodeStatus status = flow.getStatusService().getStatus(flow, id);
        return new BizContext<T>(flow, id, data, event, args) {{
            if (null != status) {
                setNode(status.getNode());
                setFlowStatus(status.getFlow());
            }
        }};
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initFlows();
    }


    private void initXmlFlows() {

    }

    private void initFsmFlows() {
//        启发
        Set<Class> classesAction = PackageUtils.scan(pcProperties.fsmFlowPackage, Action.class);

        classesAction.forEach(aClass -> {
            try {
                Object obj = aClass.newInstance();
                if (obj instanceof Action) {
                    Action bean = (Action) obj;
                    InfraUtils.registerBean(bean.beanName(), bean);
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });

//        掏出

        Set<Class> classesFsm = PackageUtils.scan(pcProperties.fsmFlowPackage, Fsm.class);
        classesFsm.forEach(aClass -> {
            try {
                Object obj = aClass.newInstance();
                if (obj instanceof Fsm && !obj.getClass().equals(Fsm.class)) {
                    Flow f = ((Fsm) obj).flow();
                    log.info("初始化流程:{}", f.getName());
                    flows.put(f.getName(), f);
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (GenericApplicationContext) applicationContext;
    }


}
