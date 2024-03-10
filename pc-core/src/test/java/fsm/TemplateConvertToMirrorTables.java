package fsm;

import java.util.List;
import java.util.Map;


public abstract class TemplateConvertToMirrorTables implements ConvertToMirrorTables{
 

    @Override
    public void convert(String productNo, Integer version, DomainEnum domainEnum, Object data) {
        List<String> sqls = renderToSqls(getSqlTemplates(), cpontextOfTemplates(productNo,version,domainEnum,data));
        executeSqls(sqls,getDataSource());
    }

    private void executeSqls(List<String> sqls, Object dataSource) {
        
    }

    private Object getDataSource() {
        return null;
    }

    private List<String> renderToSqls(List<String> sqlTemplates, Map<String, Object> ctx) {
        sqlTemplates.forEach(sqlTemplate->{
            
        });
        return null;
    }

    public abstract Map<String, Object> cpontextOfTemplates(String productNo, Integer version, DomainEnum domainEnum,
                                                            Object data);

    public abstract List<String> getSqlTemplates();
}
