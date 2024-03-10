package io.ddbm.pc;

/**
 * Init ===>Runnig ==> Done
 * ||
 * ||
 * || 可逆
 * PAUSED||ERROR  ====>CANCEL
 */
public enum FlowStatus {
    INIT("已受理"),
    RUNNING("处理中"),
    DONE("已完成"),
    PAUSE_BUG("流程暂停"),       //代码错误，需研发人员处理
    PAUSE_MISTAKE("流程暂停"),    //用户错误，需用户|运营处理
    CANCELED("流程取消"),   //已取消
    ;

    private final String desc;

    FlowStatus(String desc) {
        this.desc = desc;
    }
}
