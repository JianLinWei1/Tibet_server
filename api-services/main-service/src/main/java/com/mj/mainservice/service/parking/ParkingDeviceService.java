package com.mj.mainservice.service.parking;

import com.mj.mainservice.entitys.parking.ParkInfo;
import com.mj.mainservice.entitys.parking.ParkingUserInfo;

public interface ParkingDeviceService {

    /**
     * 获取一条未被设备获取的信息添加白名单
     * @return
     */
    ParkingUserInfo   getUserInfoBySatus();

    /**
     * 获取设备信息SN
     * @param sn
     * @return
     */
    ParkInfo  getParkInfoBySn(String sn);

    boolean  updateParkUserInfo(ParkingUserInfo parkingUserInfo);

}
