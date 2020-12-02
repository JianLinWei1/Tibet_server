package com.mj.mainservice.entitys.access;

import com.jian.common.util.PageHelper;
import lombok.Data;

import java.util.List;

/**
 * Created by MrJan on 2020/10/20 16:36
 */

@Data
public class AccessPerson  extends PageHelper {

    private String id;
    private String pid;
    private String  name ;
    private String  accessId;
    private int  accessPw;
    private String advId;//控制器设备ID
    private String advName;
    private  String ip;
    private String userId;
    private List<Doors>  doorsNum;


    private String department;
}
