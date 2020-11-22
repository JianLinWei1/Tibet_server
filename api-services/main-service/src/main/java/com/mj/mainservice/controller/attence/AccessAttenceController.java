package com.mj.mainservice.controller.attence;


import com.jian.common.util.ResultUtil;
import com.mj.mainservice.annotation.SysLogInter;
import com.mj.mainservice.entitys.attence.AttenceReport;
import com.mj.mainservice.service.access.AccessService;
import com.mj.mainservice.service.access.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @auther JianLinWei
 * @date 2020-11-15 0:51
 */
@RestController
@RequestMapping("/attence")
public class AccessAttenceController {
    @Autowired
    private AccessService accessService;
    @Autowired
    private TranslationService translationService;

    @GetMapping("/getDeviceTree")
    public ResultUtil getDeviceTree(String userId){

      return accessService.getDeviceTree(userId);
    }


    @PostMapping("/getAttenceReport")
    public  ResultUtil getAttenceReport(@RequestBody AttenceReport report){
        return  translationService.getAttenceReport(report);
    }
}
