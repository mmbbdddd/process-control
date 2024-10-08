package cn.hz.ddbm.pc.newcore.fsm;

import cn.hutool.core.map.multi.RowKeyTable;
import cn.hutool.extra.spring.SpringUtil;
import cn.hz.ddbm.pc.ProcessorService;
import cn.hz.ddbm.pc.newcore.FlowContext;
import cn.hz.ddbm.pc.newcore.Payload;
import cn.hz.ddbm.pc.newcore.chaos.LocalChaosAction;
import cn.hz.ddbm.pc.newcore.config.PcProperties;
import cn.hz.ddbm.pc.newcore.exception.FlowEndException;
import cn.hz.ddbm.pc.newcore.exception.FlowStatusException;
import cn.hz.ddbm.pc.newcore.fsm.actions.LocalFsmAction;
import cn.hz.ddbm.pc.newcore.fsm.routers.ToRouter;
import cn.hz.ddbm.pc.newcore.utils.EnvUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

public class FsmFlowTest {
    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();

    @BeforeEach
    public void setup() {
        ctx.register(FF.class);
        ctx.refresh();
    }


    @Test
    public void runFsm() {
        EnvUtils.setChaosMode(true);
        FsmFlow p = new FsmFlow("test", IdCard.init, IdCard.su, IdCard.fail);
        p.local(IdCard.init, PrepareAction.class, new ToRouter<>(IdCard.presend));
        p.local(IdCard.presend, PrepareAction.class, new ToRouter<>(IdCard.auditing));
        p.local(IdCard.auditing, PrepareAction.class, new Router(new RowKeyTable<String, IdCard, Double>() {{
            put("result.code == '0000'", IdCard.su, 1.0);
            put("result.code == '0001'", IdCard.fail, 0.1);
            put("result.code == '0002'", IdCard.no_such_order, 0.1);
            put("result.code == '0003'", IdCard.lost_date, 0.1);
        }}));
        p.local(IdCard.no_such_order, PrepareAction.class, new ToRouter<>(IdCard.presend));
        p.local(IdCard.lost_date, PrepareAction.class, new ToRouter<>(IdCard.init));


        FlowContext<FsmState> ctx = new FlowContext<>(p, new Payload<FsmState>() {
            FsmState state = new FsmState(IdCard.init, FsmWorker.Offset.task);


            @Override
            public String getId() {
                return "1";
            }

            @Override
            public FsmState getState() {
                return state;
            }

            @Override
            public void setState(FsmState state) {
                this.state = state;
            }
        });
        try {
            p.execute(ctx);
        } catch (FlowEndException e) {
        } catch (FlowStatusException e) {
        }
    }

    enum IdCard {
        init,
        presend,
        auditing,
        no_such_order,
        lost_date,
        su,
        fail,

    }

    static class FF {
        @Bean
        PcProperties properties() {
            return new PcProperties();
        }

        @Bean
        LocalChaosAction localChaosAction() {
            return new LocalChaosAction();
        }

        @Bean
        SpringUtil springUtil() {
            return new SpringUtil();
        }

        @Bean
        PrepareAction prepareAction() {
            return new PrepareAction();
        }

        @Bean
        ProcessorService procesorService() {
            return new ProcessorService();
        }
    }

    static class PrepareAction implements LocalFsmAction {
        @Override
        public Object doLocalFsm(FlowContext<FsmState> ctx) {
            Integer executeTimes = ctx.getExecuteTimes();
            Integer retryTimes   = ctx.getFlow().stateAttrs(ctx.state).getRetry();
            if (executeTimes > retryTimes) {
                throw new RuntimeException("2");
            }
            Double r = Math.random();
//            if (r < 0.1) {
//                throw new Exception("1");
//            }
            if (r < 0.3) {
                throw new RuntimeException("1");
            }
            return null;
        }
    }
}