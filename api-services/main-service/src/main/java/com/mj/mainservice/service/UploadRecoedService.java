package com.mj.mainservice.service;


import com.mj.mainservice.entitys.parking.ParkInfo;
import com.mj.mainservice.entitys.parking.ParkingResponse;
import com.mj.mainservice.entitys.parking.PlateUpload;

public interface UploadRecoedService {


    ParkingResponse heartBeat(ParkInfo parkInfo);

    ParkingResponse getAddWihteList(String serialno);
    ParkingResponse getDelWihteList(String serialno);

    void   plateUpload(PlateUpload plateUpload);
}
