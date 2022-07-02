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
    private       TaskNode    node;
    private       Event       event;
    private       Session     session;
    private       Boolean     interrupt = false;
    private       String      interruptMessage;

    public FlowContext(Flow flow, FlowRequest request, String event) throws InterruptException {
        event = StringUtils.isEmpty(event) ? Coast.DEFAULT_EVENT : event;
        Assert.notNull(flow, "flow is null");
        Assert.notNull(request, "request is null");
        this.flow    = flow;
        this.request = request;
        this.node    = flow.nodeOf(request.getStatus());
        this.request.setStatus(node.getName());
        this.event = this.node.getEvent(event);
        if (request.getSession() == null) {
            this.session = SpringUtils.getBean(RedisSession.class);
        } else {
            this.session = request.getSession();
        }
        this.interrupt = Boolean.FALSE;
    }

    public String chaosNode() {
        //获取当前节点的所有event和maybe
        TaskNode     node     = flow.getNode(request.getStatus());
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
        this.interrupt        = interrupt;
        this.interruptMessage = e.getMessage();
    }

    public boolean isPause(Logger logger) {
        Boolean result = null;
        try {
            result = interrupt
                    || node.type == TaskNode.Type.END
                    || !node.fluent
                    || 10 < (Integer) session.get(Coast.TOTAL_ERROR, 0)
                    || 200 < (Integer) session.get(Coast.TOTAL_COUNT, 0)
                    || event.retry < (Integer) session.get(Coast.EVENT_COUNT(event), 0);
            return result;
        } finally {
            if (null != logger) {
                logger.debug("\t\t  isPause:{},{}", result, interruptMessage);
                logger.debug("\t\t\t\t\t interrupt:{}", interrupt);
                logger.debug("\t\t\t\t\t node.type == TaskNode.Type.END:{}", node.type == TaskNode.Type.END);
                logger.debug("\t\t\t\t\t !node.fluent:{}", !node.fluent);
                logger.debug("\t\t\t\t\t 10 <  (Integer) session.get(Coast.TOTAL_ERROR, 0):{}", 10 < (Integer) session.get(Coast.TOTAL_ERROR, 0));
                logger.debug("\t\t\t\t\t 200 < (Integer) session.get(Coast.TOTAL_COUNT, 0):{}", 200 < (Integer) session.get(Coast.TOTAL_COUNT, 0));
                logger.debug("\t\t\t\t\t event.retry < (Integer) session.get(Coast.EVENT_COUNT(event), 0):{}", event.retry < (Integer) session.get(Coast.EVENT_COUNT(event), 0));
            }
        }
    }

    public boolean isPause() {
        return interrupt
                || node.type == TaskNode.Type.END
                || 100 < (Integer) session.get(Coast.EVENT_COUNT(event), 0);

    }

}
