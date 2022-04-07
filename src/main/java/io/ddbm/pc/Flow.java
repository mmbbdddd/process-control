package io.ddbm.pc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * 流程定义
 */
public class Flow implements ValueObject {
    Logger logger = LoggerFactory.getLogger(getClass());
    //    缺省指令
    static final String DEFAULT_COMMAND = "push";
    //    流程名
    String                        name;
    //    节点名称：节点
    Map<String, _Node>            nodes;
    Map<String, ExpressionRouter> routers;
    //    开始节点
    _Node                         startNode;
    //    流程数据的持久化接口，需要用户实现
    ContextService                contextService;
    FlowRecordRepository          repository;

    public Flow(String name) {
        Assert.notNull(name, "工作流名称为空");
        this.name    = name;
        this.nodes   = new HashMap<>();
        this.routers = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        for (_Node t : nodes.values()) {
            if (t instanceof Start) {
                startNode = t;
                break;
            }
        }
        Assert.notNull(startNode, "开始节点为空");
    }


    public void execute(String cmd, Serializable id, Map<String, Object> args) throws RouterException {
        logger.info("工作流{}记录{}收到请求{},{}", name, id, cmd, args);
        if (StringUtils.isEmpty(cmd)) {
            cmd = DEFAULT_COMMAND;
        }
        FLowRecord  record      = null;
        FlowContext ctx         = null;
        _Node       currentNode = null;
        if (null == id) {
//            开始节点流程
            record = repository.newRecord(args);
            id     = record.getId();
            logger.info("工作流{}创建记录{},{},{}", name, id, cmd, args);
            currentNode = startNode;
        } else {
//            非开始节点流程
            record = repository.get(id);
            logger.info("工作流{}记录{}查询数据{},{} ", name, id, cmd, record);
            String nodeName = record.translateStatusToNode();
            currentNode = getNode(nodeName);
        }
        ctx = FlowContext.of(id, record, cmd, args, this, currentNode);
        execute(cmd, currentNode, ctx);

    }

    public void execute(String cmd, _Node currentNode, FlowContext ctx) throws RouterException {
        _Node targetNode = currentNode.execute(cmd, ctx);
        contextService.snapshot(ctx);
        if (null != targetNode && !(targetNode instanceof End) && !ctx.isRetry()) {
            execute(cmd, targetNode, ctx);
        }
    }


    public _Node getNode(String node) {
        return nodes.get(node);
    }

    public void addNode(_Node node) {
        node.setFlow(this);
        this.nodes.put(node.name, node);
    }

    public void addRouter(ExpressionRouter router) {
        this.routers.put(router.routerName, router);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flow flow = (Flow) o;
        return Objects.equals(name, flow.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public Router getRouter(String routerName) {
        return routers.get(routerName);
    }
}
