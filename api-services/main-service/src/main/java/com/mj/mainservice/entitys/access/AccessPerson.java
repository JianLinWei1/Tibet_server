package com.mj.mainservice.entitys.access;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jian.common.util.PageHelper;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
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
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime  time;


    private String department;
}
