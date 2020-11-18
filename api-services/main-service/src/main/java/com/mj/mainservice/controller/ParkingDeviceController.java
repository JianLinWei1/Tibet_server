package com.mj.mainservice.controller;

import com.jian.common.entitys.parking.ParkInfo;
import com.jian.common.entitys.parking.ParkingResponse;
import com.jian.common.entitys.parking.PlateUpload;
import com.jian.common.util.ResultUtil;
import com.jian.parkingservice.service.UploadRecoedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by MrJan on 2020/10/28
 */

@RestController
@RequestMapping("/lj")
public class ParkingDeviceController {

   @Autowired
   private UploadRecoedService uploadRecoedService;

    @PostMapping("/heartBeat")
    public ParkingResponse heartBeat(ParkInfo heartBeat){

        return  uploadRecoedService.heartBeat(heartBeat);
    }

    @GetMapping("/test")
    public ResultUtil  test(){
        return ResultUtil.ok("测试");
    }


    @PostMapping("/plateUpload")
    public  void plateUpload(@RequestBody  PlateUpload plateUpload){

       uploadRecoedService.plateUpload(plateUpload);
    }

}
