package com.mj.mainservice.service.parking;

import com.jian.common.util.ResultUtil;
import com.mj.mainservice.entitys.parking.ParkInfo;
import com.mj.mainservice.entitys.parking.ParkingResult;
import com.mj.mainservice.entitys.parking.ParkingUserInfo;
import com.mj.mainservice.vo.access.BatchIssueVo;
import com.mj.mainservice.vo.parking.TmpPersonVo;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
    ResultUtil exportSearchRecords(ParkingResult parkingResult);
    Long  countParking(String userId);

    ResultUtil getParingTree(String userId);

    ResultUtil  batchIssue(BatchIssueVo issueVo);

    ResultUtil  batchIssue(MultipartFile file , List<String> parkIds);
}
