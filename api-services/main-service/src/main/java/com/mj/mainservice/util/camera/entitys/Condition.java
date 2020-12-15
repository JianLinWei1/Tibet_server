package com.mj.mainservice.util.camera.entitys;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * Created by MrJan on 2020/12/14
 */

@Data
public class Condition {
    @JSONField(name = "QueryType")
    private Integer QueryType;
    @JSONField(name = "LogicFlag")
    private Integer LogicFlag;
    @JSONField(name = "QueryData")
    private String  QueryData;
}
