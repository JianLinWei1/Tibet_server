package com.mj.mainservice.entitys.parking;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jian.common.util.PageHelper;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by MrJan on 2020/10/29
 */

@Data
public class ParkingUserInfo   extends PageHelper {

    @Id
    private  String id;

    private String serialno;

    private  String device_name;

    private  String personId;

    private String name ;

    private  String carId;

    private  String userId;

    private List<String> personIds;

    private  Integer action;

    private  Integer enable;

    private  Integer need_alarm;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime enable_time;//当前名单生效时间，如：2018-01-01 11:11:11
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private  LocalDateTime overdue_time;//当前名单过期时间，如：2018-01-01 11:11:11

    private Boolean status;

}
