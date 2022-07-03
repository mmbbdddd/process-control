package io.ddbm.pc;

import io.ddbm.pc.exception.InterruptException;
import io.ddbm.pc.session.RedisSession;
import io.ddbm.pc.utils.SpringUtils;
import lombok.Getter;
import org.slf4j.Logger;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Getter
public class FlowContext {
    //    当前工作流数据
    private final FlowRequest request;
    private final Flow        flow;
    private       Task        node;
    private       Event       event;
    private       Session     session;

    public FlowContext(Flow flow, FlowRequest request, String event) throws InterruptException {
        event = StringUtils.isEmpty(event) ? Coast.DEFAULT_EVENT : event;
        Assert.notNull(flow, "flow is null");
        Assert.notNull(request, "request is null");
        this.flow    = flow;
        this.request = request;
        this.node    = flow.nodeOf(request.getStatus());
        if (this.node.type == Task.Type.END) {
            throw InterruptException.nodeIsEnd(this.node.name);
        }
        this.request.setStatus(node.getName());
        this.event = this.node.getEvent(event);
        if (request.getSession() == null) {
            this.session = SpringUtils.getBean(RedisSession.class);
        } else {
            this.session = request.getSession();
        }
    }

    public String chaosNode() {
        //获取当前节点的所有event和maybe
        Task         node     = flow.getNode(request.getStatus());
        List<String> allMaybe = new ArrayList<>();
        for (Event e : node.events.values()) {
            allMaybe.addAll(e.getMaybe());
        }
        //从中随机选一个。
        Double r      = Math.random() * allMaybe.size();
        String result = allMaybe.get(r.intValue());
//        System.out.println("chaos:"+result);
        return result;
    }

    public void setInterrupt(Boolean interrupt, Exception e) {
        session.set(Coast.INTERRUPT, interrupt);
        session.set(Coast.INTERRUPT_MESSAGE, e.getMessage());
    }

    public boolean syncIsStop(Logger logger) {
        Boolean result = null;
        try {
            result = (Boolean) session.get(Coast.INTERRUPT, false)
                    || node.type == Task.Type.END
                    || 10 < (Integer) session.get(Coast.TOTAL_ERROR, 0)
                    || 200 < (Integer) session.get(Coast.TOTAL_COUNT, 0)
                    || event.retry < (Integer) session.get(Coast.EVENT_COUNT(event), 0);
            return result;
        } finally {
            if (null != logger) {
                logger.debug("\t\t  isPause:{},{}", result, session.get(Coast.INTERRUPT_MESSAGE, ""));
                logger.debug("\t\t\t\t\t interrupt:{}", (Boolean) session.get(Coast.INTERRUPT, false));
                logger.debug("\t\t\t\t\t node.type == TaskNode.Type.END:{}", node.type == Task.Type.END);
                logger.debug("\t\t\t\t\t 10 <  (Integer) session.get(Coast.TOTAL_ERROR, 0):{}", 10 < (Integer) session.get(Coast.TOTAL_ERROR, 0));
                logger.debug("\t\t\t\t\t 200 < (Integer) session.get(Coast.TOTAL_COUNT, 0):{}", 200 < (Integer) session.get(Coast.TOTAL_COUNT, 0));
                logger.debug("\t\t\t\t\t event.retry < (Integer) session.get(Coast.EVENT_COUNT(event), 0):{}", event.retry < (Integer) session.get(Coast.EVENT_COUNT(event), 0));
            }
        }
    }

    public boolean asyncIsStop(Logger logger) {
        Boolean result = null;
        try {
            result = (Boolean) session.get(Coast.INTERRUPT, false)
                    || node.type == Task.Type.END
                    || !node.fluent
                    || 10 < (Integer) session.get(Coast.TOTAL_ERROR, 0)
                    || 200 < (Integer) session.get(Coast.TOTAL_COUNT, 0)
                    || event.retry < (Integer) session.get(Coast.EVENT_COUNT(event), 0);
            return result;
        } finally {
            if (null != logger) {
                logger.debug("\t\t  isPause:{},{}", result, session.get(Coast.INTERRUPT_MESSAGE, ""));
                logger.debug("\t\t\t\t\t interrupt:{}", (Boolean) session.get(Coast.INTERRUPT, false));
                logger.debug("\t\t\t\t\t node.type == TaskNode.Type.END:{}", node.type == Task.Type.END);
                logger.debug("\t\t\t\t\t !node.fluent:{}", !node.fluent);
                logger.debug("\t\t\t\t\t 10 <  (Integer) session.get(Coast.TOTAL_ERROR, 0):{}", 10 < (Integer) session.get(Coast.TOTAL_ERROR, 0));
                logger.debug("\t\t\t\t\t 200 < (Integer) session.get(Coast.TOTAL_COUNT, 0):{}", 200 < (Integer) session.get(Coast.TOTAL_COUNT, 0));
                logger.debug("\t\t\t\t\t event.retry < (Integer) session.get(Coast.EVENT_COUNT(event), 0):{}", event.retry < (Integer) session.get(Coast.EVENT_COUNT(event), 0));
            }
        }
    }

    public boolean chaosIsStop() {
        return (Boolean) session.get(Coast.INTERRUPT, false)
                || node.type == Task.Type.END
                || 100 < (Integer) session.get(Coast.EVENT_COUNT(event), 0);

    }

}
