package fsm;

import com.google.common.collect.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class TemplateConvertToMirrorTablesDemo extends TemplateConvertToMirrorTables {

    @Override
    public Boolean match(String productNo, DomainEnum domainEnum) {
        return Objects.equals(productNo,"LX001") ;
    }

    @Override
    public Map<String, Object> cpontextOfTemplates(
        String productNo, Integer version, DomainEnum domainEnum, Object data) {
        Map<String,Object> map = new HashMap<>();
//        map.put("a",data.getA()+1);
//        map.put("b",data.getB()/100);
        map.put("product",productNo);
        map.put("version",version);
        
        return map;
    }

    @Override
    public List<String> getSqlTemplates() {
        return Lists.newArrayList("insert into abc(a,b,product_no,version) value ($a,$b,$product_no,$version)");
    }
}
