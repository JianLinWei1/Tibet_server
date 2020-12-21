package com.mj.mainservice.util.camera.entitys;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * Created by MrJan on 2020/12/21
 */

@Data
public class ResItemV1 {

    @JSONField(name = "ResCode")
    private String  ResCode;

    @JSONField(name = "ResName")
    private String ResName;
}
