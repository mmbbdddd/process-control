package io.ddbm.pc.utils;

import com.google.common.base.Preconditions;
import io.ddbm.pc.support.FlowContext;
import lombok.NonNull;


public class Throwables {
    /**
     * 转换运行时状态为异常，用异常控制流程的连续执行模式。
     * 1，PauseException，流程暂停，需要用户介入，
     * * -----1.1 用户输入错误
     * * -----1.2 需要用户介入和确认的（如风控短信验证码）
     * * -----等。。。。
     * 2，InterruptException，流程暂停，一段时间重试
     * * -----2.1 系统配置错误
     * * -----2.2 外部服务不可用
     * * -----等。。。。非用户性、数据性的错误
     * 3，RuntimeExcetion，流程停止，不可重试
     * * -----3.1 系统执行次数超过限制
     * * -----3.2 服务承诺超限制（服务时效、法律条款等）
     * * -----3.3 流程终止 StopExcetion
     * * -----等。。。。非用户、系统可抗因素
     *
     * @param ctx
     */
    public static void propagateIfPossible(@NonNull FlowContext ctx) {
        Preconditions.checkNotNull(ctx);

    }
}
