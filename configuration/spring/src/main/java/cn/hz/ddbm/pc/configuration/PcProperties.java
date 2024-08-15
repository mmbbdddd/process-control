package cn.hz.ddbm.pc.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "dddd.pc")
@Data
public class PcProperties {
    String    format ;
    StatusManager  statusManager;
    SessionManager sessionManager;

    static class StatusManager {
        Boolean redisEnable;
    }

    static class SessionManager {
        Boolean memoryEnable;
    }

    public enum DefineStyle {
        dsl, json, xml
    }
}
