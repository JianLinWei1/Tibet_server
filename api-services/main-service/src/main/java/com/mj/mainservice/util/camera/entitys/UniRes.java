package com.mj.mainservice.util.camera.entitys;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * Created by MrJan on 2020/12/14
 */

@Data
public class UniRes {
    @JSONField(name = "ErrCode")
    private  Integer ErrCode;
    @JSONField(name = "ErrMsg")
    private  String  ErrMsg;
    @JSONField(name = "Result")
    private  Object Result;
}
