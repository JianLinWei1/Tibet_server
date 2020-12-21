package com.mj.mainservice.util.camera.entitys;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * Created by MrJan on 2020/12/14
 */

@Data
public class RspPageInfo {
    @JSONField(name = "TotalRowNum")
    private  int TotalRowNum;

    @JSONField(name = "RowNum")
    private int RowNum;
}
