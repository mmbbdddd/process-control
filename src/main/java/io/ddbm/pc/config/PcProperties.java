package io.ddbm.pc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Data
@ConfigurationProperties(
        prefix = "pc"
)
public class PcProperties {
    Boolean chaosMode        = true;
    String  flowPath         = "/flow";
    Integer corePoolSize     = 3;
    Integer maxPoolSize      = 20;
    Integer queueCapacity    = 1000;
    Integer keepAliveSeconds = 3000;
}
