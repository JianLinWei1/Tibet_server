package com.mj.mainservice.controller.person;



import com.jian.common.util.ResultUtil;

import com.mj.mainservice.annotation.SysLogInter;
import com.mj.mainservice.entitys.access.Translation;
import com.mj.mainservice.entitys.person.PersonInfo;
import com.mj.mainservice.service.person.PersonService;
import com.mj.mainservice.vo.person.PersonInfoVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class PersonController {
    @Autowired
    private PersonService personService;

    @SysLogInter("添加人员")
    @PostMapping(value = "/insertPerson")
    public ResultUtil insertPerson(@RequestBody PersonInfo info  , String userId) {
        info.setUserId(userId);
        return personService.insertPerson(info);
    }


    /*@SysLogInter("获取人员列表")*/
    @PostMapping(value = "/queryPersonsList")
    public ResultUtil queryPersonsList(@RequestBody PersonInfo info , String userId  ){
        if(StringUtils.isEmpty(info.getUserId()))
        info.setUserId(userId);
        return personService.queryPersonsList(info);
    }

    @SysLogInter("编辑人员")
    @PostMapping(value = "/editPerson")
    public ResultUtil editPerson(@RequestBody PersonInfoVo info , String userId){

        //info.setUserId(userId);
        return personService.editPerson(info);
    }


    @SysLogInter("删除人员")
    @PostMapping(value = "/delPerson")
    public ResultUtil delPerson(@RequestBody List<String> ids){
        return personService.delPerson(ids);
    }


   /* @SysLogInter("获取人员")*/
    @PostMapping(value = "/quryPersonListNoPage")
    public List<PersonInfo> quryPersonListNoPage(@RequestBody PersonInfo info ){
        return  personService.quryPersonListNoPage(info);
    }


    @SysLogInter("导出人员")
    @PostMapping("/exportPerson")
    public ResultUtil exportPerson(@RequestBody List<PersonInfoVo> personInfoVos){

        return personService.exportPerson(personInfoVos);
    }

    @SysLogInter("导入人员")
    @PostMapping("/importPerson")
    public ResultUtil importPerson(MultipartFile file , String userId){

        return personService.importPerson(file , userId);
    }


    @GetMapping("/getPersonTree")
    public  ResultUtil getPersonTree(String pid ,String  userId){
        if(StringUtils.isNotEmpty(pid))
            userId = pid;
        return  personService.getPersonTree(userId);
    }









}
