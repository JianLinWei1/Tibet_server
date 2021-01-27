package com.mj.mainservice.controller.parking;


import com.jian.common.util.ResultUtil;

import com.mj.mainservice.entitys.parking.ParkInfo;
import com.mj.mainservice.entitys.parking.ParkingResponse;
import com.mj.mainservice.entitys.parking.PlateUpload;
import com.mj.mainservice.service.parking.UploadRecoedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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





        return uploadRecoedService.repairTest();
    }


    @PostMapping("/plateUpload")
    public  void plateUpload(@RequestBody PlateUpload plateUpload ){

       uploadRecoedService.plateUpload(plateUpload);
    }

}
