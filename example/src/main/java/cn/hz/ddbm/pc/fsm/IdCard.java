package cn.hz.ddbm.pc.fsm;

public enum IdCard {
    Init("收集材料中"),
    RuleChecked("规则检查"),
    Accepted("已受理"),
    Su("通过"),
    Fail("不通过"),
    RuleSyncing("规则同步"),
    ;

    String descr;

    IdCard(String descr) {
        this.descr = descr;
    }
}
