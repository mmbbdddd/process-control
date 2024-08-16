package cn.hz.ddbm.pc.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "dddd.pc")
@Data
public class PcProperties {
    DefineStyle    defineStyle;
    StatusManager  statusManager;
    SessionManager sessionManager;

    public static class StatusManager {
        Integer cacheSize;
        Integer hours;
    }

    public static class SessionManager {
        Integer cacheSize;
        Integer hours;
    }

    public enum DefineStyle {
        dsl, json, xml
    }
}
