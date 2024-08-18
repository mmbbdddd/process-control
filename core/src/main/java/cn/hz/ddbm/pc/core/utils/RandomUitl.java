package cn.hz.ddbm.pc.core.utils;

import java.util.Collection;
import java.util.List;

public class RandomUitl {
    public static <S> S random(List<S> list) {
        int size   = list.size();
        int random = Double.valueOf(Math.ceil(Math.random() * size)).intValue();
        return list.get(random);
    }
}
