package cn.hz.ddbm.pc.newcore.utils;

import java.util.Objects;

public class EnvUtils {
    private static final String RUN_MODE = "pc.run_mode";


    public static Boolean isChaos() {
        String runMode = System.getProperty(RUN_MODE);
        return Objects.equals(runMode, "true");
    }

    public static void setChaosMode(Boolean isChaosMode) {
        if (null == isChaosMode) {
            isChaosMode = true;
        }
        System.setProperty(RUN_MODE, isChaosMode.toString());
    }
}
