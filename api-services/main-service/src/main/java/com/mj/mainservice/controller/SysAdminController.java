package com.mj.mainservice.controller;

import com.jian.authservice.service.ISysAdminService;
import com.jian.common.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by MrJan on 2020/10/29
 */

@RestController
public class SysAdminController {
    @Autowired
    private ISysAdminService sysAdminService;



    @GetMapping("/getUserIdByName")
    public ResultUtil getUserIdByName(String userName){
        return  sysAdminService.getUserIdByName(userName);
    }

    @GetMapping("/getUserById")
    public   ResultUtil getUserById(String id){

        return sysAdminService.getUserById(id);
    }
}