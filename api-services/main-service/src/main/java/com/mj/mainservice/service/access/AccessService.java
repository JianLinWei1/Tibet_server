package com.mj.mainservice.service.access;


import com.jian.common.util.ResultUtil;
import com.mj.mainservice.entitys.access.AccessPerson;
import com.mj.mainservice.entitys.access.DeviceInfo;
import com.mj.mainservice.entitys.access.Translation;
import com.mj.mainservice.vo.AccessPersonVo;
import com.mj.mainservice.vo.access.TranslationVo;

import java.util.List;

/**
 * Created by MrJan on 2020/10/16 10:25
 */

public interface AccessService {

    ResultUtil  searchDevice(String ip);


    ResultUtil addDevice(DeviceInfo info);

    ResultUtil  editDevice(DeviceInfo info);

    ResultUtil listDevice(DeviceInfo info );

    ResultUtil issuedPerson(AccessPersonVo accessPersonVo);


    ResultUtil listAccessPersons(AccessPerson accessPerson );


    ResultUtil DelAccessPerson(List<String> ids);


    ResultUtil upload(List<Translation>  translations );


    ResultUtil listRecords(TranslationVo translation );

    ResultUtil delRecords(List<String>  ids);

    ResultUtil  exportRecords(List<Translation> translations);

    ResultUtil  getDeviceTree(String userId);


    ResultUtil  delDevice(List<String> ids);
}
