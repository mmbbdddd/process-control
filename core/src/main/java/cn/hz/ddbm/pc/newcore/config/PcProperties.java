package cn.hz.ddbm.pc.newcore.config;

import cn.hz.ddbm.pc.newcore.FlowAttrs;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@ConfigurationProperties(prefix = "pc")
@Component
@Data
public class PcProperties {
    Map<String, FlowAttrs> flowAttrs;
}
