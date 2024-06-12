package hz.ddbm.pc.core.plugin;

import hz.ddbm.pc.core.domain.BizContext;
import hz.ddbm.pc.core.domain.Plugin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component("logPlugin")
@Slf4j
public class LogPlugin implements Plugin {


    @Override
    public void preAction(String name, BizContext ctx) {

    }

    @Override
    public void postAction(String name, BizContext ctx) {
    }

    @Override
    public void onActionException(String name, String preNode, Exception e, BizContext ctx) { 
    }

    @Override
    public void onActionFinally(String name, BizContext ctx) {

    }

    @Override
    public void postRoute(String beanName, String preNode, BizContext ctx) {
        log.info("{},{},{},{}",ctx.getFlow().getName(),ctx.getId(),preNode,ctx.getNode());
    }

    @Override
    public void onRouteExcetion(String beanName, Exception e, BizContext ctx) {

    }
}
