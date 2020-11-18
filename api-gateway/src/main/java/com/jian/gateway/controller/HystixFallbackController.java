package com.jian.gateway.controller;


import com.jian.common.util.ResultUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auther JianLinWei
 * @date 2020-03-22 20:15
 */
@RestController
@Log4j2
public class HystixFallbackController {

    @GetMapping(value = "/defaultHystrixFallback" ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultUtil defaultHystrixFallback(){
        ResultUtil  httpResult = new ResultUtil();
        httpResult.setCode(-500);
        httpResult.setMsg("services busy!!");
        log.info("服务器进入熔断");
        return httpResult;
    }
    @PostMapping(value = "/defaultHystrixFallback" ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultUtil defaultHystrixFallback2(){
        ResultUtil  httpResult = new ResultUtil();
        httpResult.setCode(-500);
        httpResult.setMsg("services busy!!!");
        log.info("服务器进入熔断!");
        return httpResult;
    }
}
