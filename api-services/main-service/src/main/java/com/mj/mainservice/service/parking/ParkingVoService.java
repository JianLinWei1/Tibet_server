package com.mj.mainservice.service.parking;

import com.jian.common.util.ResultUtil;
import com.mj.mainservice.entitys.parking.ParkInfo;
import com.mj.mainservice.entitys.parking.ParkingResult;
import com.mj.mainservice.entitys.parking.ParkingUserInfo;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ParkingVoService {

    ResultUtil  listParking(ParkInfo parkInfo );

    ResultUtil saveParkInfo(ParkInfo parkInfo);

    ResultUtil  delParkInfo(List<String>  Ids);

    ResultUtil  saveParkPersonInfo(ParkingUserInfo parkingUserInfo);

    ResultUtil  listParkingPerson(ParkingUserInfo parkingUserInfo);


    ResultUtil delParkingPerson(List<String> ids);


    ResultUtil listParkingResult(ParkingResult parkingResult );

    ResultUtil delParkingResult(List<String>  ids);

    ResultUtil exportRecords(List<ParkingResult>  results);
}
