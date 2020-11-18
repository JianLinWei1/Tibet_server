package com.mj.mainservice.service;

import com.jian.common.entitys.parking.ParkInfo;
import com.jian.common.entitys.parking.ParkingResponse;
import com.jian.common.entitys.parking.PlateUpload;

public interface UploadRecoedService {


    ParkingResponse heartBeat(ParkInfo parkInfo);

    ParkingResponse getAddWihteList(String serialno);
    ParkingResponse getDelWihteList(String serialno);

    void   plateUpload(PlateUpload plateUpload);
}
