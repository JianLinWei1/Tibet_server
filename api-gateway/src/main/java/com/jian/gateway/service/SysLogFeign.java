package com.jian.gateway.service;

import com.google.common.collect.Multimap;
import com.jian.common.util.ResultUtil;
import com.jian.gateway.config.FeignSimpleEncoderConfig;
import com.jian.gateway.entity.SysLog;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Created by MrJan on 2020/11/3
 */

@FeignClient(name = "auth-service" ,configuration = FeignSimpleEncoderConfig.class)
public interface SysLogFeign {



    @PostMapping(value = "/addSysLog" , consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    void addSysLog(SysLog sysLog);


}
