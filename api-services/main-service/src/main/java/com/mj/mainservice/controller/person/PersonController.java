package com.mj.mainservice.controller.person;



import com.jian.common.util.ResultUtil;

import com.mj.mainservice.annotation.SysLogInter;
import com.mj.mainservice.entitys.person.PersonInfo;
import com.mj.mainservice.service.person.PersonService;
import com.mj.mainservice.vo.person.PersonInfoVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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


    @SysLogInter("获取人员列表")
    @PostMapping(value = "/queryPersonsList")
    public ResultUtil queryPersonsList(@RequestBody PersonInfo info , String userId  , @RequestParam("childs") List<String> childs){
        if(StringUtils.isEmpty(info.getUserId()))
        info.setUserId(userId);
        return personService.queryPersonsList(info ,childs);
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


    @SysLogInter("获取人员")
    @PostMapping(value = "/quryPersonListNoPage")
    public List<PersonInfo> quryPersonListNoPage(@RequestBody PersonInfo info ){
        return  personService.quryPersonListNoPage(info);
    }






}
