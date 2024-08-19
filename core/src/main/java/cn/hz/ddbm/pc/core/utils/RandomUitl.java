package cn.hz.ddbm.pc.core.utils;

import java.util.Collection;
import java.util.List;

public class RandomUitl {
    public static <S> S random(List<S> list) {
        int size   = list.size()*10-1;
        int random = Double.valueOf(Math.random() * size/10).intValue();
        return list.get(random);
    }

//    public static void main(String[] args){
//        int size  =10;
//        for(int i =0;i<100;i++){
//            System.out.println(Double.valueOf(Math.ceil(Math.random() * size)).intValue()>100);
//        }
//    }
}
