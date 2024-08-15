package cn.hz.ddbm.pc.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "dddd.pc")
@Data
public class PcProperties {
    DefineStyle         defineStyle;

    public enum DefineStyle {
        dsl, json, xml
    }
}
