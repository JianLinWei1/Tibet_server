package com.mj.mainservice.controller;


import com.jian.common.util.ResultUtil;
import com.mj.mainservice.annotation.SysLogInter;
import com.mj.mainservice.entitys.attence.AttenceConfig;
import com.mj.mainservice.entitys.attence.AttenceReport;
import com.mj.mainservice.service.AccessService;
import com.mj.mainservice.service.AttenceConfigService;
import com.mj.mainservice.service.AttenceReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auther JianLinWei
 * @date 2020-11-15 1:21
 */
@RestController
public class AttenceVoController {

    @Autowired
    private AttenceConfigService attenceConfigService;
    @Autowired
    private AttenceReportService reportService;
    @Autowired
    private AccessService  accessService;


    @GetMapping("/getDeviceTree")
    public ResultUtil  getDeviceTree(String userId){

        return accessService.getDeviceTree(userId);
    }


    @SysLogInter("添加考勤配置")
    @PostMapping("/addConfig")
    public  ResultUtil addConfig(@RequestBody AttenceConfig attenceConfig , String userId){
           attenceConfig.setUserId(userId);
        return attenceConfigService.addConfig(attenceConfig);
    }


    @SysLogInter("获取考勤配置")
    @GetMapping("/getConfig")
    public  ResultUtil getConfig(String userId){
        return  attenceConfigService.getConfig(userId);
    }


    @SysLogInter("生成报表")
    @PostMapping("/getAttenceReport")
    public ResultUtil getAttenceReport(@RequestBody AttenceReport report , String userId){
        report.setUserId(userId);
        return reportService.getAttenceReport(report);
    }


}
