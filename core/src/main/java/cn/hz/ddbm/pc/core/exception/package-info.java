package cn.hz.ddbm.pc.core.exception;

/**
 * 工作模式是连续的。
 * 1，如果无异常，继续下一个节点
 * 2，如果有异常，判断规则如下
 * 中断流程，除非调度器/人工再次触发： InterruptedFlowException
 * 中断流程，可自动触发：            IOException
 * 中断流程，再次触发不响应：         PauseFlowException/IllegalEntityException/IllegalFunctionException
 */