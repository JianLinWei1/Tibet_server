package com.mj.mainservice.controller.access;



import com.jian.common.util.ResultUtil;
import com.mj.mainservice.entitys.access.Translation;
import com.mj.mainservice.service.access.AccessService;
import com.mj.mainservice.vo.access.BatchIssueVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by MrJan on 2020/11/5
 */

@RestController
@RequestMapping("/open")
public class OpenApiController {
    @Autowired
    private AccessService accessService;


    @PostMapping("/upload")
    public ResultUtil   upload(@RequestBody List<Translation>  translations ,String sn){

         return accessService.upload(translations ,sn);
    }

    @GetMapping("/getDeviceIps")
    public  ResultUtil getDeviceIps(){

        return accessService.getDeviceIps();
    }




}
