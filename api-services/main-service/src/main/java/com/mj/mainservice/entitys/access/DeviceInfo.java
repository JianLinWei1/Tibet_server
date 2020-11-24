package com.mj.mainservice.entitys.access;

import com.jian.common.util.PageHelper;
import lombok.Data;


/**
 * Created by MrJan on 2020/10/15 17:22
 */

@Data
public class DeviceInfo  extends PageHelper {
    private String id;

    private String name;
    private  String sn;
    private  String netMask;
    private  String gateipaddress;
    private  String deviceType;
    private  String mac;
    private  String ver;
    private  String ip;
    private  String userId;
}
