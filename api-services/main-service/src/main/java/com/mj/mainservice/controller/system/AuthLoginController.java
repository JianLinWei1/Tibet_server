package com.mj.mainservice.controller.system;


import com.jian.common.util.ResultUtil;
import com.mj.mainservice.annotation.SysLogInter;

import com.mj.mainservice.entitys.system.SysAdmin;
import com.mj.mainservice.service.system.ISysAdminService;
import com.mj.mainservice.vo.SysAdminVo;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auther JianLinWei
 * @date 2020-03-23 12:43
 */

@RestController
@Log4j2
public class AuthLoginController {
    @Autowired
    private ISysAdminService sysAdminService;





    @PostMapping(value = "/login")
    public ResultUtil login(@RequestBody SysAdmin sysAdmin){

        return sysAdminService.login(sysAdmin);
    }

    @GetMapping(value = "/getRouter")
    public ResultUtil  getRouter(String  userId){

        return sysAdminService.getRouter(userId);
    }


    @SysLogInter("获取用户列表")
    @GetMapping(value = "/getAccountTree")
    public  ResultUtil getAccountTree(String userId){

        return  sysAdminService.getAccountTree(userId);
    }

    @GetMapping(value = "/getAccountTree2")
    public  ResultUtil getAccountTree2(String userId){

        return  sysAdminService.getAccountTree2(userId);
    }


    @SysLogInter("添加用户")
    @PostMapping(value = "/addUser")
    public ResultUtil addUser(@RequestBody SysAdminVo sysAdminVo ){

        return  sysAdminService.addUser(sysAdminVo);
    }


    @SysLogInter("编辑用户")
    @PostMapping(value = "/updateUser")
    public ResultUtil updateUser(@RequestBody SysAdminVo sysAdminVo  , String userId ){
             sysAdminVo.setUserId(userId);
        return  sysAdminService.updateUser(sysAdminVo);
    }

    @SysLogInter("删除用户")
    @GetMapping(value = "/delUserByParentId")
    public  ResultUtil delUserByParentId(String parentId , String  userId){
        return  sysAdminService.delUserByParentId(parentId ,userId);
    }

    @GetMapping(value = "/getAddUserTree")
    public  ResultUtil getAddUserTree(String userId){

        return  sysAdminService.getAddUserTree(userId);
    }

    @GetMapping(value = "/getUserPermission")
    public  ResultUtil getUserPermission(String userId){

        return  sysAdminService.getUserPermission(userId);
    }



}
