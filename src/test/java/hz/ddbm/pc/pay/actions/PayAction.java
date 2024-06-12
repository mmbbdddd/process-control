package hz.ddbm.pc.pay.actions;

import hz.ddbm.pc.core.domain.Action;
import hz.ddbm.pc.core.domain.BizContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

public class PayAction implements Action , InitializingBean {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public String beanName() {
        return "payAction";
    }

    @Override
    public void execute(BizContext ctx) throws Exception {

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("xxxx");
    }
}
