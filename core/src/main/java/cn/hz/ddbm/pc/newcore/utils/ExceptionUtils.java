package cn.hz.ddbm.pc.newcore.utils;


import cn.hutool.core.util.ReflectUtil;
import cn.hz.ddbm.pc.newcore.exception.InterruptedException;
import cn.hz.ddbm.pc.newcore.exception.*;

import java.io.IOException;

public class ExceptionUtils {
    public static Throwable unwrap(Throwable e) {
        if (ReflectUtil.getFieldValue(e, "raw") != null && ReflectUtil.getFieldValue(e, "raw") instanceof Exception) {
            e = (Exception) ReflectUtil.getFieldValue(e, "raw");
        }
        return e;
    }

}
