package com.mj.mainservice.service.parking;


import com.jian.common.util.ResultUtil;
import com.mj.mainservice.entitys.parking.ParkInfo;
import com.mj.mainservice.entitys.parking.ParkingResponse;
import com.mj.mainservice.entitys.parking.PlateUpload;

public interface UploadRecoedService {


    ParkingResponse heartBeat(ParkInfo parkInfo);

    ParkingResponse getAddWihteList(String serialno);
    ParkingResponse getDelWihteList(String serialno);

    Boolean  getStatus(String sn);

    void   plateUpload(PlateUpload plateUpload);

    /**
     * 设备数据修复接口
     * @return
     */
    ResultUtil  repairTest();


}
