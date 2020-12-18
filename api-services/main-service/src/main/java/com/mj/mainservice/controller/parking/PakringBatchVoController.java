package com.mj.mainservice.controller.parking;

import com.jian.common.util.ResultUtil;
import com.mj.mainservice.service.parking.ParkingVoService;
import com.mj.mainservice.vo.access.BatchIssueVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by MrJan on 2020/12/18
 */

@RestController
@RequestMapping("/parkingBatch")
public class PakringBatchVoController {
    @Autowired
    private ParkingVoService  parkingVoService;


    @GetMapping("/getParingTree")
    public ResultUtil  getParingTree(String pid ,String userId){
        if(StringUtils.isNotEmpty(pid))
            userId = pid;
        return  parkingVoService.getParingTree(userId);
    }

    @PostMapping("/batchIssue")
    public ResultUtil  batchIssue(@RequestBody BatchIssueVo batchIssueVo){

        return  parkingVoService.batchIssue(batchIssueVo);
    }
}
