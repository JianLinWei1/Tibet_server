package com.mj.mainservice.controller;


import com.jian.authservice.annotation.SysLogInter;
import com.jian.authservice.entity.SysAdmin;
import com.jian.authservice.service.ISysAdminService;
import com.jian.authservice.vo.SysAdminVo;
import com.jian.common.util.ResultUtil;
import lombok.extern.log4j.Log4j2;
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

    @SysLogInter("添加用户")
    @PostMapping(value = "/addUser")
    public ResultUtil addUser(@RequestBody SysAdminVo sysAdminVo ){

        return  sysAdminService.addUser(sysAdminVo);
    }


    @SysLogInter("编辑用户")
    @PostMapping(value = "/updateUser")
    public ResultUtil updateUser(@RequestBody SysAdminVo sysAdminVo ){

        return  sysAdminService.updateUser(sysAdminVo);
    }

    @SysLogInter("删除用户")
    @GetMapping(value = "/delUserByParentId")
    public  ResultUtil delUserByParentId( String parentId){
        return  sysAdminService.delUserByParentId(parentId);
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