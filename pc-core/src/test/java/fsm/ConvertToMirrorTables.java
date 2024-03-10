package fsm;

public interface ConvertToMirrorTables {
    Boolean match(String productNo,DomainEnum domainEnum);
    void convert(String productNo,Integer version,DomainEnum domainEnum,Object data);
}
    