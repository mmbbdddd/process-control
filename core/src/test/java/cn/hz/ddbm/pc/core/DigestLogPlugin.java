package cn.hz.ddbm.pc.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("digestLogPlugin")
public class DigestLogPlugin implements Plugin {
    Logger logger = LoggerFactory.getLogger("digest");
    @Override
    public void preAction(String name, FlowContext ctx) {

    }

    @Override
    public void postAction(String name, FlowContext ctx) {
    }

    @Override
    public void onActionException(String name, String preNode, Exception e, FlowContext ctx) {
    }

    @Override
    public void onActionFinally(String name, FlowContext ctx) {

    }

    @Override
    public void postRoute(String beanName, String preNode, FlowContext ctx) {
        logger.info("{},{},{},{}", ctx.getFlow()
                .getName(), ctx.getId(), preNode, ctx.getStatus()
                .getNode());
    }

    @Override
    public void onRouteExcetion(String beanName, Exception e, FlowContext ctx) {

    }
}
