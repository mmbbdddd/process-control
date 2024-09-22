package cn.hz.ddbm.pc.newcore.utils;

import java.util.Objects;

public class EnvUtils {
    private static final String RUN_MODE   = "pc.run_mode";
    private static final String CHAOS_MODE = "chaos";


    public static Boolean isChaos() {
        String runMode = System.getProperty(RUN_MODE);
        return Objects.equals(runMode, CHAOS_MODE);
    }

    public static void setChaosMode(Boolean isChaosMode) {
        if (isChaosMode) {
            System.setProperty(RUN_MODE, CHAOS_MODE);
        }
    }

}
