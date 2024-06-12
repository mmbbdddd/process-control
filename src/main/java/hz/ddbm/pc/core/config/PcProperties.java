package hz.ddbm.pc.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "pc")
public class PcProperties {
    public String  fsmFlowPackage="hz.ddbm.pc.pay";
    public String  xmlFlowResource;
    public Boolean isChaos= false;
}
