package com.mj.mainservice.vo.access;

import com.mj.mainservice.entitys.access.Doors;
import lombok.Data;

import java.util.List;

/**
 * Created by MrJan on 2020/12/5
 */

@Data
public class BatchIssueVo {
    private  String  pid ;
    private  String  name;
    private  Integer status; //-1 下发失败  0 正在下发 1下发成功
    private  String advId ; // - 拼接门号
    private String advName;
    private  String ip ;
    private List<Doors> doorsNum;

}
