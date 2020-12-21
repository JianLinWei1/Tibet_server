package com.mj.mainservice.util.camera.entitys;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * Created by MrJan on 2020/12/21
 */

@Data
public class ResInfo {
    @JSONField(name = "OrgName")
    private String OrgName;

    @JSONField(name = "ResItemV1")
    private ResItemV1 ResItemV1;

}
