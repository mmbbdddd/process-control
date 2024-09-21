package cn.hz.ddbm.pc.newcore.saga;

public interface SagaAction {
      enum QueryResult {
        none, exception, su, fail
    }
}
