package cn.hz.ddbm.pc.configuration;

import cn.hz.ddbm.pc.core.support.SessionManager;
import cn.hz.ddbm.pc.profile.PcService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;

@ConditionalOnClass({PcService.class})
public class PcChaosConfiguration {
}
