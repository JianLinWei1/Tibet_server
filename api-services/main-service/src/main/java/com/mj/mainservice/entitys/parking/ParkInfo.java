package com.mj.mainservice.entitys.parking;

import com.jian.common.util.PageHelper;
import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * Created by MrJan on 2020/10/28
 */

@Data
public class ParkInfo  extends PageHelper {

    private  String  device_name;

    private  String ipaddr;

    private  Integer port;

    private  String user_name;

    private  String pass_wd;



    @Id
    private  String serialno;

    private  Integer channel_num;

    private  String userId;
}