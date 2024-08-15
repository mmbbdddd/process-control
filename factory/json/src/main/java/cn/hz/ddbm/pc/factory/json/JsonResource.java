package cn.hz.ddbm.pc.factory.json;

import cn.hutool.json.JSONUtil;
import cn.hz.ddbm.pc.core.Flow;
import cn.hz.ddbm.pc.factory.Resource;

public class JsonResource extends Resource {

    String content;

    @Override
    public Flow resolve() {
        return JSONUtil.toBean(content, Flow.class);
    }
}
