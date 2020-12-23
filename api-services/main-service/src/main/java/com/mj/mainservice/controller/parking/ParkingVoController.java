package com.mj.mainservice.controller.parking;


import com.jian.common.util.ResultUtil;

import com.mj.mainservice.annotation.SysLogInter;
import com.mj.mainservice.entitys.person.PersonInfo;
import com.mj.mainservice.entitys.parking.ParkInfo;
import com.mj.mainservice.entitys.parking.ParkingResult;
import com.mj.mainservice.entitys.parking.ParkingUserInfo;
import com.mj.mainservice.service.system.ISysAdminService;
import com.mj.mainservice.service.parking.ParkingVoService;
import com.mj.mainservice.service.person.PersonService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by MrJan on 2020/10/29
 */

@RestController
public class ParkingVoController {
    @Autowired
    private ParkingVoService parkingVoService;

    /*  @Autowired
      private ParkingAuthFeign parkingAuthFeign;
      @Autowired
      private ParkingPersonFeign parkingPersonFeign;*/
    @Autowired
    private ISysAdminService sysAdminService;
    @Autowired
    private PersonService personService;

    /*@SysLogInter("获取车辆列表")*/
    @PostMapping("/listParking")
    public ResultUtil listParking(@RequestBody ParkInfo parkInfo, String userId) {
        if (StringUtils.isEmpty(parkInfo.getUserId()))
            parkInfo.setUserId(userId);
        return parkingVoService.listParking(parkInfo);
    }

    @GetMapping("/getUserIdByName")
    public ResultUtil getUserIdByName(String userName) {
        return sysAdminService.getUserIdByName(userName);
    }

    @SysLogInter("添加车牌信息")
    @PostMapping("/saveParkInfo")
    public ResultUtil saveParkInfo(@RequestBody ParkInfo parkInfo) {
        return parkingVoService.saveParkInfo(parkInfo);
    }

    @SysLogInter("删除车辆信息")
    @PostMapping("/delParkInfo")
    public ResultUtil delParkInfo(@RequestBody List<String> ids) {
        return parkingVoService.delParkInfo(ids);
    }


    @PostMapping("/getPersonByName")
    public ResultUtil getPersonByName(@RequestBody PersonInfo info, String userId, @RequestParam("childs") List<String> childs) {

        info.setUserId(userId);
        return personService.queryPersonsListByName(info, childs);
    }


    @SysLogInter("添加车牌信息")
    @PostMapping("/saveParkPersonInfo")
    public ResultUtil saveParkPersonInfo(@RequestBody ParkingUserInfo parkingUserInfo) {

        return parkingVoService.saveParkPersonInfo(parkingUserInfo);
    }

    /*@SysLogInter("获取车辆人员信息列表")*/
    @PostMapping("/listParkingPerson")
    public ResultUtil listParkingPerson(@RequestBody ParkingUserInfo parkingUserInfo, String userId) {
        if(StringUtils.isEmpty(parkingUserInfo.getUserId()))
        parkingUserInfo.setUserId(userId);
        return parkingVoService.listParkingPerson(parkingUserInfo);
    }


    @SysLogInter("删除车辆人员信息列表")
    @PostMapping("/delParkingPerson")
    public ResultUtil delParkingPerson(@RequestBody List<String> ids) {
        return parkingVoService.delParkingPerson(ids);
    }

    /*@SysLogInter("获取车牌识别结果")*/
    @PostMapping("/listParkingResult")
    public ResultUtil listParkingResult(@RequestBody ParkingResult parkingResult, String userId) {
        if(StringUtils.isEmpty(parkingResult.getUserId()))
        parkingResult.setUserId(userId);
        return parkingVoService.listParkingResult(parkingResult);
    }

    @SysLogInter("删除车牌识别结果")
    @PostMapping("/delParkingResult")
    public ResultUtil delParkingResult(@RequestBody List<String> ids) {
        return parkingVoService.delParkingResult(ids);
    }

    @SysLogInter("导出车牌识别结果")
    @PostMapping("/exportRecords")
    public ResultUtil exportRecords(@RequestBody List<ParkingResult> translations) {
        return parkingVoService.exportRecords(translations);
    }

    @PostMapping("/exportSearchRecords")
    public ResultUtil exportSearchRecords(@RequestBody ParkingResult parkingResult, String userId) {
        if(StringUtils.isEmpty(parkingResult.getUserId()))
            parkingResult.setUserId(userId);
        return parkingVoService.exportSearchRecords(parkingResult);
    }



}
