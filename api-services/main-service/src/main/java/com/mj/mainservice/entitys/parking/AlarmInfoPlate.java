package com.mj.mainservice.entitys.parking;

import lombok.Data;

/**
 * Created by MrJan on 2020/10/27
 */

@Data
public class AlarmInfoPlate {
    private  Integer channel;
    private  String deviceName;
    private  String ipaddr;
    private Result result;
    private  String serialno;
}
