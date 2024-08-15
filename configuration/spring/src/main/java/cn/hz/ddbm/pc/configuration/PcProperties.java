package cn.hz.ddbm.pc.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "dddd.pc")
public class PcProperties {
    DefineStyle defineStyle = DefineStyle.dsl;

    enum DefineStyle {
        dsl, json, xml
    }
}
