package com.mj.mainservice.service.impl;

import com.alibaba.excel.EasyExcel;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jian.common.entity.AntdTree;


import com.jian.common.util.HttpUtil;
import com.jian.common.util.ResultUtil;
import com.jian.common.util.SysConfigUtil;
import com.mj.mainservice.entitys.access.AccessPerson;
import com.mj.mainservice.entitys.access.DeviceInfo;
import com.mj.mainservice.entitys.person.PersonInfo;
import com.mj.mainservice.entitys.access.Translation;
import com.mj.mainservice.resposity.access.AccessPersonResposity;
import com.mj.mainservice.resposity.access.AccessRespository;
import com.mj.mainservice.resposity.person.PersonRepository;
import com.mj.mainservice.resposity.access.TranslationResposity;
import com.mj.mainservice.service.access.AccessService;
import com.mj.mainservice.vo.AccessPersonVo;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrJan on 2020/10/16 10:29
 */

@Service
@Log4j2
public class AccessServiceImpl implements AccessService {

    @Resource
    private AccessRespository accessRespository;
    @Resource
    private AccessPersonResposity personResposity;
    /*  @Resource
      private AccessPersonFeign accessPersonFeign;*/
    @Resource
    private PersonRepository personRepository;
    @Resource
    private TranslationResposity translationResposity;
    @Resource
    private MongoTemplate mongoTemplate;


    @Override
    public ResultUtil searchDevice(String ip) {
        try {
            if (StringUtils.isEmpty(ip))
                ip = SysConfigUtil.getIns().getProAccessServer();
            String url = "http://" + ip + "/searchDevice";
            HttpUtil httpUtil = new HttpUtil();
            ResultUtil res = httpUtil.get(url);
            log.info("搜索设备返回：{}", JSON.toJSONString(res));

            return res;
        } catch (Exception e) {
            log.error(e);
            return new ResultUtil(-1, e.getMessage());
        }

    }

    @Override
    @Transactional
    public ResultUtil addDevice(DeviceInfo info) {
        try {
            DeviceInfo info1 = new DeviceInfo();

            if(StringUtils.isEmpty(info.getSn())){
                String url = "http://" + SysConfigUtil.getIns().getProAccessServer() + "/getDeviceDataByIp?ip="+info.getIp();
                HttpUtil httpUtil = new HttpUtil();
                ResultUtil res = httpUtil.get(url);
                log.info(url+"获取设备参数返回：{}", JSON.toJSONString(res));
                if(res.getCode() != 0)
                    return res;
                Object object = res.getData();
               DeviceInfo  info2 = JSON.toJavaObject((JSON) JSON.toJSON(object), DeviceInfo.class);
               info2.setIp(info.getIp());
               info2.setName(info.getName());
               info2.setUserId(info.getUserId());
               BeanUtils.copyProperties(info2, info);
            }
            info1.setSn(info.getSn());

            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withMatcher("sn", ExampleMatcher.GenericPropertyMatchers.exact())
                    .withNullHandler(ExampleMatcher.NullHandler.IGNORE)
                    .withStringMatcher(ExampleMatcher.StringMatcher.EXACT);
            Example<DeviceInfo> example = Example.of(info1, matcher);
            List<DeviceInfo> infos = accessRespository.findAll(example);
            if (infos != null && infos.size() > 0){
               return new ResultUtil(-1 ,"该门禁已经被绑定");
            }
                //info.setId(infos.get(0).getId());
            accessRespository.save(info);
            return ResultUtil.ok();
        } catch (Exception e) {
            log.error(e);
            return new ResultUtil(-1, e.getMessage());
        }
    }

    @Override
    public ResultUtil listDevice(DeviceInfo info ) {
        try {
            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withIgnorePaths("page", "limit")
                    .withMatcher("userId", ExampleMatcher.GenericPropertyMatchers.exact())
                    .withNullHandler(ExampleMatcher.NullHandler.IGNORE)
                    .withIgnoreNullValues();
            Example<DeviceInfo> example = Example.of(info, matcher);
            Page<DeviceInfo> page1 = accessRespository.findAll(example,  PageRequest.of(info.getPage(), info.getLimit()));
            ResultUtil r = new ResultUtil();
            r.setCode(0);
            r.setData(page1.getContent());
            r.setCount(page1.getTotalElements());
            return r;

        } catch (Exception e) {
            log.error(e);
            return new ResultUtil(-1, e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResultUtil issuedPerson(AccessPersonVo accessPersonVo) {
        try {
            HttpUtil httpUtil = new HttpUtil();
            ResultUtil resultUtil = new ResultUtil();
            List<String> ms = new ArrayList<>();
            for (Object pid : accessPersonVo.getPids()) {
                try {
                    PersonInfo personInfo = personRepository.findById((String) pid).get();

                    DeviceInfo deviceInfo = accessRespository.findById(accessPersonVo.getAdvId()).get();
                    String doorIds = "";
                    for (int i : accessPersonVo.getDoors()) {
                        doorIds += i + ",";
                    }
                    String pin = "";
                    if (personInfo.getAccessId().length() > 8)
                        pin = personInfo.getAccessId().substring(0, 8);
                    else
                        pin = personInfo.getAccessId();
                    String url = "http://" + SysConfigUtil.getIns().getProAccessServer() +
                            "/setDeviceData?ip=" + deviceInfo.getIp() + "&CardNo=" + personInfo.getAccessId() + "&pin=" + pin +
                            "&pw=" + personInfo.getAccessPw() + "&doorIds=" + doorIds.substring(0, doorIds.length() - 1);
                    ResultUtil ru = httpUtil.get(url);

                    log.info("门禁下发返回：{} , URL:{}", JSON.toJSONString(ru), url);
                    if (ru.getCode() != 0) {
                        resultUtil.setCode(-1);
                        ms.add(ru.getMsg());
                    } else {
                        /**下发成功 加入数据库**/
                        AccessPerson accessPerson = accessPersonVo;
                        accessPerson.setId(null);
                        accessPerson.setPid((String) pid);
                        accessPerson.setAccessId(personInfo.getAccessId());
                        accessPerson.setAccessPw(personInfo.getAccessPw());
                        accessPerson.setName(personInfo.getName());
                        accessPerson.setIp(deviceInfo.getIp());
                        accessPerson.setDepartment(personInfo.getDepartment());
                        personResposity.save(accessPerson);
                        resultUtil.setCode(0);
                    }

                } catch (Exception e) {
                    log.error(e);
                    resultUtil.setCode(-1);
                    resultUtil.setMsg(e.getMessage());
                }
            }
            if (resultUtil.getCode() != 0) {
                resultUtil.setData(ms);
                return resultUtil;
            }


            return ResultUtil.ok();
        } catch (Exception e) {
            log.error(e);
            return new ResultUtil(-1, e.getMessage());
        }
    }


    @Override
    public ResultUtil listAccessPersons(AccessPerson accessPerson) {
        try {
            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withIgnoreNullValues()
                    .withMatcher("ip", ExampleMatcher.GenericPropertyMatchers.exact())
                    .withMatcher("advName", ExampleMatcher.GenericPropertyMatchers.contains())
                    .withMatcher("pid", ExampleMatcher.GenericPropertyMatchers.contains())
                    .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains())
                    .withMatcher("department", ExampleMatcher.GenericPropertyMatchers.contains())
                    .withMatcher("userId", ExampleMatcher.GenericPropertyMatchers.exact())
                    .withIgnorePaths("page", "limit", "accessPw")
                    .withNullHandler(ExampleMatcher.NullHandler.IGNORE);
            Example<AccessPerson> example = Example.of(accessPerson, matcher);

            Page<AccessPerson> personPage = personResposity.findAll(example, PageRequest.of(accessPerson.getPage(), accessPerson.getLimit()));
            ResultUtil resultUtil = new ResultUtil();
            resultUtil.setCode(0);
            resultUtil.setData(personPage.toList());
            resultUtil.setCount(personPage.getTotalElements());
            return resultUtil;
        } catch (Exception e) {
            log.error(e);
            return new ResultUtil(-1, e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResultUtil DelAccessPerson(List<String> ids) {
        try {
            HttpUtil httpUtil = new HttpUtil();
            ResultUtil resultUtil = new ResultUtil();
            List<String> ms = new ArrayList<>();
            for (String id : ids) {
                AccessPerson accessPerson = personResposity.findById(id).get();
                String doorIds = "";
                for (int i : accessPerson.getDoors()) {
                    doorIds += i + ",";
                }
                String pin = "";
                if (accessPerson.getAccessId().length() > 8)
                    pin = accessPerson.getAccessId().substring(0, 8);
                else
                    pin = accessPerson.getAccessId();
                String url = "http://" + SysConfigUtil.getIns().getProAccessServer() + "/deleteDeviceData?ip=" + accessPerson.getIp() +
                        "&CardNo=" + accessPerson.getAccessId() + "&pin=" + pin + "&pw=" + accessPerson.getAccessPw()
                        +"&doorIds=" + doorIds.substring(0, doorIds.length() - 1);
                ResultUtil ru = httpUtil.get(url);
                log.info("门禁下发返回：{} , URL:{}", JSON.toJSONString(ru), url);
                if (ru.getCode() != 0) {
                    resultUtil.setCode(-1);
                    ms.add(ru.getMsg());
                } else {
                    /**下发成功 从数据库删除**/
                    personResposity.delete(accessPerson);
                    resultUtil.setCode(0);
                }
            }

            if (resultUtil.getCode() != 0) {
                resultUtil.setData(ms);
                return resultUtil;
            }


            return ResultUtil.ok();
        } catch (Exception e) {
            log.error(e);
            return new ResultUtil(-1, e.getMessage());
        }

    }

    @Override
    public ResultUtil upload(List<Translation> translations) {
        try {
            translations.stream().forEach(translation -> {
                AccessPerson accessPerson = personResposity.findByAccessIdEquals(translation.getIcCard());
                translation.setPersonId(accessPerson.getPid());
                translation.setName(accessPerson.getName());
                translation.setDvName(accessPerson.getAdvName());
                translation.setUserId(accessPerson.getUserId());
                translation.setDepartment(accessPerson.getDepartment());
                translationResposity.save(translation);
            });

            return ResultUtil.ok();


        } catch (Exception e) {
            log.error(e);
            return new ResultUtil(-1, e.getMessage());
        }
    }


    @Override
    public ResultUtil listRecords(Translation translation) {
        try {
            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withIgnorePaths("page", "limit")
                    .withMatcher("icCard", ExampleMatcher.GenericPropertyMatchers.contains())
                    .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains())
                    .withMatcher("dvName", ExampleMatcher.GenericPropertyMatchers.contains())
                    .withMatcher("department", ExampleMatcher.GenericPropertyMatchers.contains())
                    .withMatcher("userId", ExampleMatcher.GenericPropertyMatchers.exact())
                    .withNullHandler(ExampleMatcher.NullHandler.IGNORE)
                    .withIgnoreNullValues();

            Example<Translation> example = Example.of(translation, matcher);
            /*childs.add(translation.getUserId());
            Query query = new Query();
            query.addCriteria(Criteria.where("userId").in(childs));*/

            Page<Translation> page = translationResposity.findAll(example, PageRequest.of(translation.getPage(), translation.getLimit(), Sort.by(Sort.Direction.DESC, "time")));

            ResultUtil resultUtil = new ResultUtil();
            resultUtil.setCode(0);
            resultUtil.setCount(page.getTotalElements());
            resultUtil.setData(page.getContent());
            return resultUtil;
        } catch (Exception e) {
            log.error(e);
            return new ResultUtil(-1, e.getMessage());
        }
    }

    @Override
    public ResultUtil delRecords(List<String> ids) {
        try {
            ids.stream().forEach(id -> {
                translationResposity.deleteById(id);
            });
            return ResultUtil.ok();
        } catch (Exception e) {
            log.error(e);
            return new ResultUtil(-1, e.getMessage());
        }
    }

    @Override
    public ResultUtil exportRecords(List<Translation> translations) {
        try {
            String path = System.currentTimeMillis() + ".xlsx";
            EasyExcel.write("upload" + File.separator + path, Translation.class).sheet().doWrite(translations);
            return new ResultUtil(0, path, "");
        } catch (Exception e) {
            log.error(e);
            return new ResultUtil(-1, e.getMessage());
        }
    }

    @Override
    public ResultUtil getDeviceTree(String userId) {
        try {
            DeviceInfo deviceInfo = new DeviceInfo();
            deviceInfo.setUserId(userId);
            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withMatcher("userId", ExampleMatcher.GenericPropertyMatchers.exact());
            Example<DeviceInfo> example = Example.of(deviceInfo, matcher);
            List<DeviceInfo> deviceInfos = accessRespository.findAll(example);
            AntdTree antdTree = new AntdTree();
            antdTree.setKey("0");
            antdTree.setTitle("门禁设备");
            List<AntdTree> child = new ArrayList<>();
            deviceInfos.stream().forEach(dv -> {
                AntdTree antdTree1 = new AntdTree();
                antdTree1.setKey(dv.getSn());
                antdTree1.setTitle(dv.getName());
                child.add(antdTree1);
            });
            antdTree.setChildren(child);
            List<Object> objects = new ArrayList<>();
            ResultUtil resultUtil = new ResultUtil();
            resultUtil.setCode(0);
            objects.add(antdTree);
            resultUtil.setData(objects);
            return resultUtil;
        } catch (Exception e) {
            log.error(e);
            return new ResultUtil(-1, e.getMessage());
        }

    }


    @Override
    public ResultUtil delDevice(List<String> ids) {

        try {
            ids.stream().forEach(id -> {
                accessRespository.deleteById(id);
            });

            return ResultUtil.ok();
        } catch (Exception e) {
            log.error(e);
            return new ResultUtil(-1, e.getMessage());
        }
    }
}
