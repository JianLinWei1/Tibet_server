package com.mj.mainservice.service;

import com.jian.accessservice.vo.AccessPersonVo;
import com.jian.common.entitys.AccessPerson;
import com.jian.common.entitys.DeviceInfo;
import com.jian.common.entitys.access.Translation;
import com.jian.common.util.ResultUtil;

import java.util.List;

/**
 * Created by MrJan on 2020/10/16 10:25
 */

public interface AccessService {

    ResultUtil  searchDevice(String ip);


    ResultUtil addDevice(DeviceInfo info);

    ResultUtil listDevice(int page , int limit , String userId);

    ResultUtil issuedPerson(AccessPersonVo accessPersonVo);


    ResultUtil listAccessPersons(AccessPerson accessPerson );


    ResultUtil DelAccessPerson(List<String> ids);


    ResultUtil upload(List<Translation>  translations);


    ResultUtil listRecords(Translation translation);

    ResultUtil delRecords(List<String>  ids);

    ResultUtil  exportRecords(List<Translation> translations);

    ResultUtil  getDeviceTree(String userId);


    ResultUtil  delDevice(List<String> ids);
}
