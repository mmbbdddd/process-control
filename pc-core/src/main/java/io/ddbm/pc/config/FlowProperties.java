package io.ddbm.pc.config;

import lombok.Data;


@Data
public class FlowProperties {

    String flowPath = "/flow";

    Integer corePoolSize = 3;

    Integer maxPoolSize = 20;

    Integer queueCapacity = 1000;

    Integer keepAliveSeconds = 3000;
}
