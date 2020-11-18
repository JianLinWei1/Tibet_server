package com.jian.common.entitys.parking;

import lombok.Data;

import java.util.List;

/**
 * Created by MrJan on 2020/10/27
 */

@Data
public class WhiteListOperate {
    private  Integer operate_type; //操作类型(0:增加，1：删除)
    private List<WhiteListData>  white_list_data;

}
