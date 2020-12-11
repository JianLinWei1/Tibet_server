package com.mj.mainservice.entitys.access;

import com.jian.common.util.PageHelper;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by MrJan on 2020/10/15 17:22
 */

@Data
@Document(collection = "deviceInfo")
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
    private List<Doors> doors = Arrays.asList(new Doors(1,"1号门") ,new Doors(2,"2号门") ,
            new Doors(3,"3号门") ,new Doors(4,"4号门"));//初始化四个门

    private  Boolean status;

}
