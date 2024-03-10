package io.ddbm.pc;

import io.ddbm.pc.exception.ContextCreateException;
import io.ddbm.pc.exception.NoSuchEventException;
import io.ddbm.pc.exception.NoSuchNodeException;
import io.ddbm.pc.exception.ParameterException;
import io.ddbm.pc.exception.PauseException;
import io.ddbm.pc.exception.SessionException;
import io.ddbm.pc.status.StatusException;
import io.ddbm.pc.utils.SpringUtils;

import java.io.Serializable;


public interface FlowService<T> {

    String flowName();
    //    void submit(T data);

    /**
     * 任务调度
     * 1，查询数据库中所有待执行任务明细
     * 2，发起onEvent事件执行
     */
    void onScheduleEvent();

    /**
     * 单笔交易执行
     *
     * @param request
     */
    default void onEvent(FlowRequest<T> request) {
        try {
            SpringUtils.getBean(ProcessControlService.class).executeFluent(request);
        } catch (ContextCreateException e) {
            //todo 创建上下文异常，需要报送&告警
            throw new RuntimeException(e);
        } catch (ParameterException e) {
            //参数异常，需要用户处理
            //            throw new RuntimeException(e);
        } catch (PauseException e) {
            //暂停异常，无需处理
            //            throw new RuntimeException(e);
        } catch (StatusException e) {
            //            状态provider异常，需要报送&告警
            throw new RuntimeException(e);
        } catch (SessionException e) {
            //            回话provider异常，需要报送&告警
            throw new RuntimeException(e);
        } catch (NoSuchNodeException e) {
            //            程序bug，需要报送&告警
            throw new RuntimeException(e);
        } catch (NoSuchEventException e) {
            //            程序bug，或者入参错误，需要报送&告警
            throw new RuntimeException(e);
        }
    }

    default void pause(Serializable dateId) {
        SpringUtils.getBean(ProcessControlService.class).pause(flowName(), dateId);
    }

    default void recover(Serializable dateId) {
        SpringUtils.getBean(ProcessControlService.class).recover(flowName(), dateId);
    }

    default void cancel(Serializable dateId) {
        SpringUtils.getBean(ProcessControlService.class).cancel(flowName(), dateId);
    }

    //    List<ErrorRecord<T>> allBugs(String flowName, Integer num, TimeUnit timeUnit);
    //    List<ErrorRecord<T>> allMistakes(String flowName, Integer num, TimeUnit timeUnit); 
}
