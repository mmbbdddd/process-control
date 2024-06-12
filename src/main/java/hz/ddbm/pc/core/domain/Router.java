package hz.ddbm.pc.core.domain;

import hz.ddbm.pc.core.PcService;
import hz.ddbm.pc.core.utils.ElUtils;
import hz.ddbm.pc.core.utils.InfraUtils;
import lombok.Getter;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * 作用 ：节点路由
 * 类型 ：1对1（to），1对多选1(any)，1对多选多（Fork），多对1（Join）
 * dsl：
 * ___none://none
 * ___to://flowstatus
 * ___any://routerName
 * ___fork://routerName
 * ___join://conditionExpression:flowstatus
 */
public abstract class Router {
    private static String resolveProtocolHead(String dsl) {
        String[] splits = dsl.split("://");
        return splits[0];
    }

    private static String resolveProtocolBody(String dsl) {
        String[] splits = dsl.split("://");
        return splits[1];
    }

    @Getter
    String name;

    public Router(String name) {
        this.name = name;
    }

    public abstract <T> String route(BizContext<T> ctx);


    public static Router dsl(String dsl, Flow flow) {
        Assert.notNull(dsl, "router dsl is null");
        String head = Router.resolveProtocolHead(dsl);
        String body = Router.resolveProtocolBody(dsl);
        if (head.equalsIgnoreCase("to")) {
            return new To(body);
        }
        if (head.equalsIgnoreCase("any")) {
            return flow.getRouter(dsl);
        }
        if (head.equalsIgnoreCase("none")) {
            return new None();
        }
        throw new RuntimeException("not support this router dsl protocol");
    }

    @Getter
    public static class None extends Router {

        public None() {
            super("none://none");
        }


        @Override
        public <T> String route(BizContext<T> ctx) {
            return ctx.getNode();
        }
    }

    @Getter
    public static class To extends Router {
        String to;

        public To(String targetNode) {
            super("to://" + targetNode);
            this.to = targetNode;
        }


        @Override
        public <T> String route(BizContext<T> ctx) {
            return to;
        }
    }


    @Getter
    public static class Any extends Router {
        Map<String, String> expressions;

        public Any(String rouerName, Map<String, String> expressions) {
            super("any://" + rouerName);
            this.expressions = expressions;
        }


        @Override
        public <T> String route(BizContext<T> ctx) {
            Map<String, Object> routerContext = InfraUtils.getBean(PcService.class).buildRouterContext(ctx);
            for (Map.Entry<String, String> entry : expressions.entrySet()) {
                String  to         = entry.getKey();
                String  expression = entry.getValue();
                Boolean result     = ElUtils.evalBoolean(expression, routerContext);
                if (result) {
                    return to;
                }
            }
            return ctx.getTransition().getFailToNode();
        }
    }

//    @Getter
//    class Fork extends Router {

//        }
//    }

//    @Getter
//    class Join extends Router {

//    }
}
