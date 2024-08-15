package cn.hz.ddbm.pc.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "dddd.pc")
public class PcProperties {
    DefineStyle defineStyle;
    String      resourceUrl;

    enum DefineStyle {
        dsl, json, xml
    }
}
