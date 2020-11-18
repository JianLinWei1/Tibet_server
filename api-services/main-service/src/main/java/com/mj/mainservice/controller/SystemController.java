package com.mj.mainservice.controller;

import com.jian.authservice.annotation.SysLogInter;
import com.jian.authservice.entity.SysLog;
import com.jian.authservice.service.ISysLogService;
import com.jian.common.entity.SysConfig;
import com.jian.common.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by MrJan on 2020/11/3
 */

@RestController
public class SystemController {
    @Autowired
    private ISysLogService iSysLogService;



    @SysLogInter("添加系统配置")
    @PostMapping("/addSysConfig")
    public ResultUtil addSysConfig(@RequestBody  SysConfig sysConfig){
        return iSysLogService.addSysConfig(sysConfig);
    }

    @SysLogInter("获取系统配置")
    @GetMapping("/getSysConfig")
    public  ResultUtil getSysConfig(){
        return iSysLogService.getSysConfig();
    }



    @PostMapping("/addSysLog")
    public void addSysLog(@RequestBody SysLog sysLog){
            sysLog.setTime(LocalDateTime.now());
        iSysLogService.addSysLog(sysLog);
    }


    @PostMapping("/getSysLogs")
    public ResultUtil getSysLogs(@RequestBody SysLog sysLog){

        return iSysLogService.getSysLogs(sysLog);
    }

    @SysLogInter("删除系统日志")
    @PostMapping("/delSyslog")
    public ResultUtil delSyslog(@RequestBody List<String> ids) {

        return iSysLogService.delSyslog(ids);
    }

}
