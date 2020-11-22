package com.mj.mainservice.controller.access;



import com.jian.common.util.ResultUtil;
import com.mj.mainservice.annotation.SysLogInter;
import com.mj.mainservice.entitys.access.AccessPerson;
import com.mj.mainservice.entitys.access.DeviceInfo;
import com.mj.mainservice.entitys.person.PersonInfo;
import com.mj.mainservice.entitys.access.Translation;
import com.mj.mainservice.service.access.AccessService;
import com.mj.mainservice.service.person.PersonService;
import com.mj.mainservice.vo.AccessPersonVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by MrJan on 2020/10/16 10:05
 */

@RestController
@RequestMapping("/access")
public class AccessController {

    @Autowired
    private AccessService accessService;
//    @Qualifier("accessPersonFeignImpl")
//    @Autowired
//    private AccessPersonFeign personFeign;
  @Autowired
  private PersonService personService;


    @SysLogInter("搜索门禁控制器")
    @GetMapping("/searchDevice")
    public ResultUtil searchDevice(String ip) {
        return accessService.searchDevice(ip);
    }

    @SysLogInter("添加门禁控制器")
    @PostMapping("/addDevice")
    public ResultUtil addDevice(@RequestBody DeviceInfo info, String userId) {
        info.setUserId(userId);
        return accessService.addDevice(info);
    }


    @SysLogInter("获取门禁控制器列表")
    @GetMapping("/listDevice")
    public ResultUtil listDevice(int page, int limit, String userId ,@RequestParam("childs") List<String> childs) {

        return accessService.listDevice(page, limit, userId ,childs);
    }

    @PostMapping("/queryPersonsList")
    public ResultUtil queryPersonsList(@RequestBody PersonInfo infoVo, String userId  ,@RequestParam("childs") List<String>  childs) {
        infoVo.setUserId(userId);
        infoVo.setPage(1);
        infoVo.setLimit(10);
        return personService.queryPersonsList(infoVo , childs);
    }

    @SysLogInter("下发人员")
    @PostMapping("/issuedPerson")
    public ResultUtil issuedPerson(@RequestBody AccessPersonVo personVo ,String userId){
        personVo.setUserId(userId);


        return accessService.issuedPerson(personVo);
    }

    @SysLogInter("获取发卡记录")
    @PostMapping("/listAccessPersons")
    public ResultUtil listAccessPersons(@RequestBody AccessPerson accessPerson,String userId ,@RequestParam("childs") List<String>  childs){
        accessPerson.setUserId(userId);
        return accessService.listAccessPersons(accessPerson ,childs);
    }

    @SysLogInter("删除记录并从设备删除人员")
    @PostMapping("/DelAccessPerson")
    public ResultUtil DelAccessPerson(@RequestBody List<String> ids){

        return accessService.DelAccessPerson(ids);
    }

    @SysLogInter("刷卡记录")
    @PostMapping("/listRecords")
    public ResultUtil listRecords(@RequestBody Translation translation , String userId ,@RequestParam("childs") List<String>  childs){
         translation.setUserId(userId);
        return accessService.listRecords(translation ,childs);
    }


    @PostMapping("/delRecords")
    public  ResultUtil delRecords(@RequestBody List<String> ids){
        return  accessService.delRecords(ids);
    }


    @SysLogInter("导出刷卡记录")
    @PostMapping("/exportRecords")
    public ResultUtil exportRecords(@RequestBody List<Translation> translation){

        return accessService.exportRecords(translation);
    }


    @PostMapping("/delDevice")
    public ResultUtil  delDevice( @RequestBody List<String> ids){

        return  accessService.delDevice(ids);

    }






}
