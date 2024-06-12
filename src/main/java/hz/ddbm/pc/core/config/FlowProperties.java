package hz.ddbm.pc.core.config;

import lombok.Getter;

@Getter
public class FlowProperties {
    String name;
    String statusService;
    String sessionProvider;

    public FlowProperties(String name) {
        this(name, null, Coast.SESSION_SERVICE_REDIS);
    }

    public FlowProperties(String name, String statusService) {
        this(name, statusService, Coast.SESSION_SERVICE_REDIS);
    }

    public FlowProperties(String name, String statusService, String sessionProvider) {
        this.name = name;
        this.statusService = statusService;
        this.sessionProvider = sessionProvider;
    }


}
