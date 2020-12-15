package com.mj.mainservice.util.camera.entitys;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * Created by MrJan on 2020/12/14
 */

@Data
public class QueryCondition {
    @JSONField(name = "ItemNum")
    private  Integer ItemNum;
    @JSONField(name = "Condition")
    private List<Condition> Condition;
    @JSONField(name = "QueryCount")
    private  Integer QueryCount;
    @JSONField(name = "PageFirstRowNumber")
    private  Integer PageFirstRowNumber;
    @JSONField(name = "PageRowNum")
    private  Integer PageRowNum;
}
