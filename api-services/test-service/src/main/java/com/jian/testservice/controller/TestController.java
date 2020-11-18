package com.jian.testservice.controller;


import com.jian.common.util.ResultUtil;
import com.jian.testservice.entity.Test;
import com.jian.testservice.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("/test")
    public ResultUtil test(){
        ResultUtil util = new ResultUtil();
        util.setCode(0);
        util.setData(testService.test());
        return util;
    }

    @PostMapping("/test")
    public ResultUtil test(@RequestBody @Valid Test test){
        return new ResultUtil(0 , test ,"success" );
    }
}
