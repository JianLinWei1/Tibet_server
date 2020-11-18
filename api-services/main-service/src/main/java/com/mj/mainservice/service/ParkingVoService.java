package com.mj.mainservice.service;

import com.jian.common.entitys.parking.ParkInfo;
import com.jian.common.entitys.parking.ParkingResult;
import com.jian.common.entitys.parking.ParkingUserInfo;
import com.jian.common.util.ResultUtil;

import java.util.List;

public interface ParkingVoService {

    ResultUtil  listParking(ParkInfo parkInfo);

    ResultUtil saveParkInfo(ParkInfo parkInfo);

    ResultUtil  delParkInfo(List<String>  Ids);

    ResultUtil  saveParkPersonInfo(ParkingUserInfo  parkingUserInfo);

    ResultUtil  listParkingPerson(ParkingUserInfo parkingUserInfo);


    ResultUtil delParkingPerson(List<String> ids);


    ResultUtil listParkingResult(ParkingResult parkingResult);

    ResultUtil delParkingResult(List<String>  ids);

    ResultUtil exportRecords(List<ParkingResult>  results);
}
