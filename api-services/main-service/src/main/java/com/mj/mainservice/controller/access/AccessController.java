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
import com.mj.mainservice.vo.access.BatchIssueVo;
import com.mj.mainservice.vo.access.TranslationVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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

    @SysLogInter("编辑门禁控制器")
    @PostMapping("/editDevice")
    public ResultUtil editDevice(@RequestBody DeviceInfo info, String userId) {
        //info.setUserId(userId);
        return accessService.editDevice(info);
    }


   /* @SysLogInter("获取门禁控制器列表")*/
    @PostMapping("/listDevice")
    public ResultUtil listDevice(@RequestBody DeviceInfo info, String userId ) {
     if(StringUtils.isEmpty(info.getUserId()))
         info.setUserId(userId);
        return accessService.listDevice(info);
    }

    @PostMapping("/queryPersonsList")
    public ResultUtil queryPersonsList(@RequestBody PersonInfo infoVo, String userId ,@RequestParam("childs") List<String>  childs) {
        infoVo.setUserId(userId);
        return personService.queryPersonsListByName(infoVo ,childs);
    }

    @SysLogInter("下发人员")
    @PostMapping("/issuedPerson")
    public ResultUtil issuedPerson(@RequestBody AccessPersonVo personVo ,String userId){
        personVo.setUserId(userId);


        return accessService.issuedPerson3(personVo);
    }

    /*@SysLogInter("获取发卡记录")*/
    @PostMapping("/listAccessPersons")
    public ResultUtil listAccessPersons(@RequestBody AccessPerson accessPerson,String userId ){
        if(StringUtils.isEmpty(accessPerson.getUserId()))
        accessPerson.setUserId(userId);
        return accessService.listAccessPersons(accessPerson);
    }

    @SysLogInter("删除记录并从设备删除人员")
    @PostMapping("/DelAccessPerson")
    public ResultUtil DelAccessPerson(@RequestBody List<String> ids){

        return accessService.DelAccessPerson2(ids);
    }


    @PostMapping("/listRecords")
    public ResultUtil listRecords(@RequestBody TranslationVo translation , String userId ){
        if(StringUtils.isEmpty(translation.getUserId()))
         translation.setUserId(userId);
        return accessService.listRecords(translation);
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

    @PostMapping("/exportSearchRecords")
    public ResultUtil exportSearchRecords( @RequestBody TranslationVo translation , String userId ){
        if(StringUtils.isEmpty(translation.getUserId()))
            translation.setUserId(userId);
        return accessService.exportSearchRecords(translation);
    }


    @PostMapping("/delDevice")
    public ResultUtil  delDevice( @RequestBody List<String> ids){

        return  accessService.delDevice(ids);

    }

    @GetMapping("/getDeviceTreeDoor")
    public  ResultUtil getDeviceTreeDoor(String pid ,String  userId){
        if(StringUtils.isNotEmpty(pid))
            userId = pid;
        return  accessService.getDeviceTreeDoor(userId);
    }

    @PostMapping("/batchIssue")
    public  ResultUtil batchIssue(@RequestBody BatchIssueVo issueVo){

        return accessService.batchIssue3(issueVo);
    }

    @PostMapping("/exportSearchRecords2")
    public  ResultUtil exportSearchRecords2(@RequestBody TranslationVo issueVo){

        return accessService.exportSearchRecords2(issueVo);
    }

    @SysLogInter(value = "远程开门")
    @GetMapping("/openDoor")
    public  ResultUtil openDoor(String ip , Integer id ){

        return accessService.openDoor(ip , id);
    }













}
