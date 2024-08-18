//package cn.hz.ddbm.pc.core.router;
//
//
//import cn.hutool.core.lang.Assert;
//import cn.hz.ddbm.pc.core.FlowContext;
//import cn.hz.ddbm.pc.core.FlowPayload;
//import cn.hz.ddbm.pc.core.Router;
//import cn.hz.ddbm.pc.core.State;
//import lombok.Getter;
//
///**
// * @Description TODO
// * @Author wanglin
// * @Date 2024/8/7 23:52
// * @Version 1.0.0
// **/
//
//
//public class ToRouter<S extends Enum<S>> implements Router<S>, State {
//   @Getter
//    S from;
//    @Getter
//    S to;
//    String routerName;
//
//    public ToRouter(S from, S to) {
//        Assert.notNull(from, "from is null");
//        Assert.notNull(to, "to is null");
//        this.to         = to;
//        this.from       = from;
//        this.routerName = String.format("fromToRouter(%s,%s)", from, to);
//    }
//
//    @Override
//    public String routerName() {
//        return routerName;
//    }
//
//    @Override
//    public S route(FlowContext<S, FlowPayload<S>> ctx) {
//        return to;
//    }
//
//    @Override
//    public S failover(S preNode, FlowContext<S, FlowPayload<S>> ctx) {
//        return from;
//    }
//
//
//    @Override
//    public String toString() {
//        return "{" +
//                "from:'" + from + '\'' +
//                ", to:'" + to + '\'' +
//                '}';
//    }
//
//    @Override
//    public S status() {
//        return to;
//    }
//
//    @Override
//    public Integer getRetry() {
//        return Integer.MAX_VALUE;
//    }
//}
