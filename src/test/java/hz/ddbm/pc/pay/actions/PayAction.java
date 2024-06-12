package hz.ddbm.pc.pay.actions;

import hz.ddbm.pc.core.domain.Action;
import hz.ddbm.pc.core.domain.BizContext;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PayAction implements Action , InitializingBean {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public String beanName() {
        return "payAction";
    }

    @Override
    public void execute(BizContext ctx) throws Exception {
        if(Math.random()<0.4){
            throw new Exception();
        }
        if(Math.random()<0.4){
            throw new RuntimeException();
        }
        Map<String,Object> result = new HashMap<>();
        result.put("code",randomCode(Lists.newArrayList("0000","0001")));
        ctx.setActionResult(result);
    }

    private String randomCode(List<String> codes) {
        int random = (int)Math.round(Math.floor(Math.random()*2));
        return codes.get(random);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("xxxx");
    }
}
