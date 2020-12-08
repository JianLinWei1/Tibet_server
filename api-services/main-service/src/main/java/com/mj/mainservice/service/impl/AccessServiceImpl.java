package com.mj.mainservice.service.impl;

import com.alibaba.excel.EasyExcel;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.api.R;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jian.common.entity.AntdTree;


import com.jian.common.util.HttpUtil;
import com.jian.common.util.MapCache;
import com.jian.common.util.ResultUtil;
import com.jian.common.util.SysConfigUtil;
import com.mj.mainservice.entitys.access.AccessPerson;
import com.mj.mainservice.entitys.access.DeviceInfo;
import com.mj.mainservice.entitys.access.Doors;
import com.mj.mainservice.entitys.person.PersonInfo;
import com.mj.mainservice.entitys.access.Translation;
import com.mj.mainservice.resposity.access.AccessPersonResposity;
import com.mj.mainservice.resposity.access.AccessRespository;
import com.mj.mainservice.resposity.person.PersonRepository;
import com.mj.mainservice.resposity.access.TranslationResposity;
import com.mj.mainservice.service.access.AccessService;
import com.mj.mainservice.util.AccessUtil;
import com.mj.mainservice.vo.AccessPersonVo;
import com.mj.mainservice.vo.access.BatchIssueVo;
import com.mj.mainservice.vo.access.BatchMsg;
import com.mj.mainservice.vo.access.BatchPersonInfo;
import com.mj.mainservice.vo.access.TranslationVo;
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

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by MrJan on 2020/10/16 10:29
 */

@Service
@Log4j2
public class AccessServiceImpl implements AccessService {

    @Resource
    private AccessRespository accessRespository;
    @Resource
    private AccessPersonResposity accessPersonResposity;
    /*  @Resource
      private AccessPersonFeign accessPersonFeign;*/
    @Resource
    private PersonRepository personRepository;
    @Resource
    private TranslationResposity translationResposity;
    @Resource
    private MongoTemplate mongoTemplate;

    private MapCache mapCache = new MapCache();


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

            if (StringUtils.isEmpty(info.getSn())) {
                String url = "http://" + SysConfigUtil.getIns().getProAccessServer() + "/getDeviceDataByIp?ip=" + info.getIp();
                HttpUtil httpUtil = new HttpUtil();
                ResultUtil res = httpUtil.get(url);
                log.info(url + "获取设备参数返回：{}", JSON.toJSONString(res));
                if (res.getCode() != 0)
                    return res;
                Object object = res.getData();
                DeviceInfo info2 = JSON.toJavaObject((JSON) JSON.toJSON(object), DeviceInfo.class);
                info2.setIp(info.getIp());
                info2.setName(info.getName());
                info2.setUserId(info.getUserId());
                BeanUtils.copyProperties(info2, info);
            }
            info1.setSn(info.getSn());

            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withMatcher("sn", ExampleMatcher.GenericPropertyMatchers.exact())
                    .withNullHandler(ExampleMatcher.NullHandler.IGNORE)
                    .withStringMatcher(ExampleMatcher.StringMatcher.EXACT)
                    .withIgnorePaths("doors");
            Example<DeviceInfo> example = Example.of(info1, matcher);
            List<DeviceInfo> infos = accessRespository.findAll(example);

            if (infos != null && infos.size() > 0) {
                return new ResultUtil(-1, "该门禁已经被绑定");
            }
            //info.setId(infos.get(0).getId());
//            info.setDoors(Arrays.asList(new Doors(1,"1号门") ,new Doors(2,"2号门") ,
//                    new Doors(3,"3号门") ,new Doors(4,"4号门")));
            accessRespository.save(info);
            return ResultUtil.ok();
        } catch (Exception e) {
            log.error(e);
            return new ResultUtil(-1, e.getMessage());
        }
    }

    @Override
    public ResultUtil editDevice(DeviceInfo info) {
        try {
            accessRespository.save(info);
            return ResultUtil.ok();
        } catch (Exception e) {
            log.error(e);
            return new ResultUtil(-1, e.getMessage());
        }
    }

    @Override
    public ResultUtil listDevice(DeviceInfo info) {
        try {
            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withIgnorePaths("page", "limit", "doors")
                    .withMatcher("userId", ExampleMatcher.GenericPropertyMatchers.exact())
                    .withNullHandler(ExampleMatcher.NullHandler.IGNORE)
                    .withIgnoreNullValues();
            Example<DeviceInfo> example = Example.of(info, matcher);
            Page<DeviceInfo> page1 = accessRespository.findAll(example, PageRequest.of(info.getPage(), info.getLimit()));
            page1.getContent().stream().forEach(d -> {
                if (mapCache.get(d.getSn()) != null)
                    d.setStatus(true);
            });
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
                    long t1 = System.currentTimeMillis();
                    PersonInfo personInfo = personRepository.findById((String) pid).get();
                    DeviceInfo deviceInfo = accessRespository.findById(accessPersonVo.getAdvId()).get();
                    AccessPerson accessPerson1 = accessPersonResposity.findByPidEqualsAndAdvIdEqualsAndDoorsNumContains((String) pid, deviceInfo.getId(),
                            accessPersonVo.getDoorsNum());
                    if (accessPerson1 != null)
                        continue;
                    // return new ResultUtil(-1 ,"存在同一个人下发到了相同门："+accessPerson1.getName()+",请重新选择");
                    log.info("下发人员查询时间{}ms", System.currentTimeMillis() - t1);
                    String doorIds = "";
                    for (Doors i : accessPersonVo.getDoorsNum()) {
                        doorIds += i.getId() + ",";
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
                    //ResultUtil ru = ResultUtil.ok();
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
                        accessPersonResposity.save(accessPerson);
                        resultUtil.setCode(0);
                    }

                } catch (Exception e) {
                    log.error(e);
                    resultUtil.setCode(-1);
                    resultUtil.setMsg(e.getMessage());
                }
            }
            if (resultUtil.getCode() != null && resultUtil.getCode() != 0) {
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

            Page<AccessPerson> personPage = accessPersonResposity.findAll(example, PageRequest.of(accessPerson.getPage() - 1, accessPerson.getLimit(), Sort.by(Sort.Direction.DESC, "time")));
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
                AccessPerson accessPerson = accessPersonResposity.findById(id).get();
                String doorIds = "";
                for (Doors i : accessPerson.getDoorsNum()) {
                    doorIds += i.getId() + ",";
                }
                String pin = "";
                if (accessPerson.getAccessId().length() > 8)
                    pin = accessPerson.getAccessId().substring(0, 8);
                else
                    pin = accessPerson.getAccessId();
                String url = "http://" + SysConfigUtil.getIns().getProAccessServer() + "/deleteDeviceData?ip=" + accessPerson.getIp() +
                        "&CardNo=" + accessPerson.getAccessId() + "&pin=" + pin + "&pw=" + accessPerson.getAccessPw()
                        + "&doorIds=" + doorIds.substring(0, doorIds.length() - 1);
                ResultUtil ru = httpUtil.get(url);

                log.info("门禁下发返回：{} , URL:{}", JSON.toJSONString(ru), url);
                if (ru.getCode() != 0) {
                    resultUtil.setCode(-1);
                    ms.add(ru.getMsg());
                } else {
                    /**下发成功 从数据库删除**/
                    accessPersonResposity.delete(accessPerson);
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
    public ResultUtil upload(List<Translation> translations, String sn) {
        try {
            if (mapCache.get(sn) == null)
                mapCache.add(sn, 1, 30 * 1000);
            translations.stream().forEach(translation -> {
                AccessPerson accessPerson = accessPersonResposity.findByAccessIdEqualsAndAdvIdEquals(translation.getIcCard() ,sn);
                if (accessPerson == null)
                    return;

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
    public ResultUtil listRecords(TranslationVo translation) {
        try {
            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withIgnorePaths("page", "limit", "dates")
                    .withMatcher("icCard", ExampleMatcher.GenericPropertyMatchers.contains())
                    .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains())
                    .withMatcher("dvName", ExampleMatcher.GenericPropertyMatchers.contains())
                    .withMatcher("department", ExampleMatcher.GenericPropertyMatchers.contains())
                    .withMatcher("userId", ExampleMatcher.GenericPropertyMatchers.exact())
                    .withNullHandler(ExampleMatcher.NullHandler.IGNORE)
                    .withIgnoreNullValues();

            Example<Translation> example = Example.of(translation, matcher);
            /*childs.add(translation.getUserId());*/
            Query query = new Query();
            if (translation.getDates() != null)
                query.addCriteria(Criteria.where("time").gte(translation.getDates().get(0))
                        .lte(translation.getDates().get(1)));
            Page<Translation> page = translationResposity.findAll(example, query, PageRequest.of(translation.getPage() - 1, translation.getLimit(), Sort.by(Sort.Direction.DESC, "time")));

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
            EasyExcel.write("upload" + File.separator + path, Translation.class).sheet("sheet").doWrite(translations);
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
                    .withMatcher("userId", ExampleMatcher.GenericPropertyMatchers.exact())
                    .withIgnorePaths("doors");
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

    @Override
    public ResultUtil getDeviceTreeDoor(String userId) {
        try {
            DeviceInfo deviceInfo = new DeviceInfo();
            deviceInfo.setUserId(userId);
            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withMatcher("userId", ExampleMatcher.GenericPropertyMatchers.exact())
                    .withIgnorePaths("doors");
            Example<DeviceInfo> example = Example.of(deviceInfo, matcher);
            List<DeviceInfo> deviceInfos = accessRespository.findAll(example);
            List<AntdTree> antdTrees = new ArrayList<>();
            deviceInfos.stream().forEach(dv -> {
                AntdTree antdTree = new AntdTree();
                antdTree.setKey(dv.getId());
                antdTree.setValue(dv.getIp());
                antdTree.setTitle(dv.getName());
                List<AntdTree> child = new ArrayList<>();
                dv.getDoors().stream().forEach(d -> {
                    AntdTree antdTree1 = new AntdTree();
                    antdTree1.setKey(dv.getId() + "-" + d.getId() + "-" + d.getName());
                    antdTree1.setTitle(d.getName());
                    child.add(antdTree1);
                });

                antdTree.setChildren(child);
                antdTrees.add(antdTree);
            });

          /*  AntdTree antdTree = new AntdTree();
            antdTree.setKey("0");
            antdTree.setTitle("选择门禁设备");
            List<AntdTree> child = new ArrayList<>();
            deviceInfos.stream().forEach(dv -> {
                dv.getDoors().stream().forEach(d->{
                    AntdTree antdTree1 = new AntdTree();
                    antdTree1.setKey(dv.getId()+"-"+d.getId());
                    antdTree1.setTitle(dv.getName()+"-"+d.getName());
                    child.add(antdTree1);
                });
            });*/
          /*  antdTree.setChildren(child);
            List<Object> objects = new ArrayList<>();
            objects.add(antdTree);*/
            ResultUtil resultUtil = new ResultUtil();
            resultUtil.setCode(0);
            resultUtil.setData(antdTrees);
            return resultUtil;
        } catch (Exception e) {
            log.error(e);
            return new ResultUtil(-1, e.getMessage());
        }
    }

    @Override
    public ResultUtil  batchIssue(BatchIssueVo issueVo) {
        try {
            HttpUtil httpUtil = new HttpUtil();
            ResultUtil resultUtil = new ResultUtil();

            List<Map<String, List<Doors>>> list = AccessUtil.parseDoors(issueVo.getDvIds());
            StringBuilder ips = new StringBuilder();
            List<BatchPersonInfo> batchPersonInfos = new ArrayList<>();
            List<AccessPerson> accessPersons = new ArrayList<>();
            //批量下发设备
            list.stream().forEach(l -> {
                Iterator a = l.entrySet().iterator();
                while (a.hasNext()) {
                    Map.Entry entry = (Map.Entry) a.next();
                    System.out.println(entry.getKey() + "------" + entry.getValue());
                    DeviceInfo deviceInfo = accessRespository.findById(String.valueOf(entry.getKey())).get();
                    ips.append(deviceInfo.getIp());
                    ips.append(",");

                    issueVo.getPids().stream().forEach(pid -> {
                        BatchPersonInfo batchPersonInfo = new BatchPersonInfo();
                        //查出人员信息
                        Optional<PersonInfo> optional = personRepository.findById(pid);
                        if (!optional.isPresent() || StringUtils.isEmpty(optional.get().getAccessId()))
                            return;
                        List<Doors> doorNums = (List<Doors>) entry.getValue();
                        PersonInfo personInfo = optional.get();

                        AccessPerson accessPerson1 = accessPersonResposity.findByPidEqualsAndAdvIdEqualsAndDoorsNumContains((String) personInfo.getId(), deviceInfo.getSn(),
                                doorNums);
                        if (accessPerson1 != null)
                            return;
                        AccessPerson accessPerson = new AccessPerson();

                        accessPerson.setPid(personInfo.getId());
                        accessPerson.setAccessId(personInfo.getAccessId());
                        accessPerson.setAccessPw(personInfo.getAccessPw());
                        accessPerson.setName(personInfo.getName());
                        accessPerson.setIp(deviceInfo.getIp());
                        accessPerson.setDepartment(personInfo.getDepartment());
                        accessPerson.setDoorsNum(doorNums);
                        accessPerson.setUserId(personInfo.getUserId());
                        accessPerson.setAdvId(deviceInfo.getSn());
                        accessPerson.setAdvName(deviceInfo.getName());
                        accessPerson.setTime(LocalDateTime.now());
                        accessPersons.add(accessPerson);
                        int doorId = AccessUtil.getDoor(doorNums.stream().map(Doors::getId).collect(Collectors.toList()));
                        String pin = "";
                        if (personInfo.getAccessId() != null && personInfo.getAccessId().length() > 8)
                            pin = personInfo.getAccessId().substring(0, 8);
                        else
                            pin = personInfo.getAccessId();

                        batchPersonInfo.setCardNo(personInfo.getAccessId());
                        batchPersonInfo.setPin(pin);
                        batchPersonInfo.setDoorId(doorId);
                        batchPersonInfo.setPw(personInfo.getAccessPw());
                        batchPersonInfos.add(batchPersonInfo);
                    });


                }
            });
            List<BatchMsg> list1 = new ArrayList<>();
            for (String ip : ips.toString().split(",")) {
                String url = "http://" + SysConfigUtil.getIns().getProAccessServer() +
                        "/batch?ip=" + ip;
                ResultUtil ru = httpUtil.post(url, JSON.toJSONString(batchPersonInfos));
               // ResultUtil ru = ResultUtil.ok();
                log.info("门禁下发返回：{} , URL:{}", JSON.toJSONString(ru), url);
               BatchMsg batchMsg = JSON.parseObject(JSON.toJSONString(ru.getData()), BatchMsg.class);
             /*   BatchMsg batchMsg = new BatchMsg();
                batchMsg.setIp(ip);
                batchMsg.setStatus(true);*/
                if(batchMsg == null)
                    continue;
                if ( batchMsg.getStatus()) {
                    /**下发成功 加入数据库**/
                    accessPersons.stream().forEach(p -> {
                        if (StringUtils.equals(p.getIp(), batchMsg.getIp()))
                            accessPersonResposity.save(p);
                    });
                    list1.add(batchMsg);
                }else {
                  BatchMsg batchMsg1 = new BatchMsg();
                    batchMsg1.setIp(ip);
                    batchMsg1.setStatus(false);
                    batchMsg1.setMsg(ru.getMsg());
                    list1.add(batchMsg1);
                }



            }
            resultUtil.setData(list1);
            resultUtil.setCode(0);

            return resultUtil;
        } catch (Exception e) {
            log.error(e);
            return new ResultUtil(-1, e.getMessage());
        }
    }

    @Override
    public ResultUtil issuedPerson2(AccessPersonVo accessPersonVo) {
        try {
            HttpUtil httpUtil = new HttpUtil();
            ResultUtil resultUtil = new ResultUtil();
            List<String> ms = new ArrayList<>();
            List<BatchPersonInfo> batchPersonInfos = new ArrayList<>();
            List<AccessPerson> accessPersons = new ArrayList<>();
            String ip = "";
            for (Object pid : accessPersonVo.getPids()) {
                BatchPersonInfo batchPersonInfo = new BatchPersonInfo();
                //查出人员信息
                PersonInfo personInfo = personRepository.findById((String) pid).get();
                DeviceInfo deviceInfo = accessRespository.findById(accessPersonVo.getAdvId()).get();
                ip = deviceInfo.getIp();
                AccessPerson accessPerson1 = accessPersonResposity.findByPidEqualsAndAdvIdEqualsAndDoorsNumContains((String) pid, deviceInfo.getId(),
                        accessPersonVo.getDoorsNum());
                if (accessPerson1 != null)
                    continue;
                // return new ResultUtil(-1 ,"存在同一个人下发到了相同门："+accessPerson1.getName()+",请重新选择");
                AccessPerson accessPerson2 = accessPersonResposity.findByPidEqualsAndAdvIdEqualsAndDoorsNumContains((String) personInfo.getId(), deviceInfo.getSn(),
                        accessPersonVo.getDoorsNum());
                if (accessPerson2 != null)
                    continue;


                String pin = "";
                if (personInfo.getAccessId().length() > 8)
                    pin = personInfo.getAccessId().substring(0, 8);
                else
                    pin = personInfo.getAccessId();
                int doorId = AccessUtil.getDoor(accessPersonVo.getDoorsNum().stream().map(Doors::getId).collect(Collectors.toList()));
                AccessPerson accessPerson = new AccessPerson();

                accessPerson.setPid(personInfo.getId());
                accessPerson.setAccessId(personInfo.getAccessId());
                accessPerson.setAccessPw(personInfo.getAccessPw());
                accessPerson.setName(personInfo.getName());
                accessPerson.setIp(deviceInfo.getIp());
                accessPerson.setDepartment(personInfo.getDepartment());
                accessPerson.setDoorsNum(accessPersonVo.getDoorsNum());
                accessPerson.setUserId(personInfo.getUserId());
                accessPerson.setAdvId(deviceInfo.getSn());
                accessPerson.setAdvName(deviceInfo.getName());
                accessPerson.setTime(LocalDateTime.now());
                accessPersons.add(accessPerson);
                batchPersonInfo.setCardNo(personInfo.getAccessId());
                batchPersonInfo.setPin(pin);
                batchPersonInfo.setDoorId(doorId);
                batchPersonInfo.setPw(personInfo.getAccessPw());
                batchPersonInfos.add(batchPersonInfo);


            }
            ;
            String url = "http://" + SysConfigUtil.getIns().getProAccessServer() +
                    "/batch?ip=" + ip;
            ResultUtil ru = httpUtil.post(url, JSON.toJSONString(batchPersonInfos));
           // ResultUtil ru = ResultUtil.ok();
            log.info("门禁下发返回：{} , URL:{}", JSON.toJSONString(ru), url);

            if (ru.getCode() == 0) {
                accessPersonResposity.saveAll(accessPersons);
            }

            return ru;
        } catch (Exception e) {
            log.error(e);
            return new ResultUtil(-1, e.getMessage());
        }
    }

    @Override
    public ResultUtil exportSearchRecords(TranslationVo translation) {
        try {
            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withIgnorePaths("page", "limit", "dates")
                    .withMatcher("icCard", ExampleMatcher.GenericPropertyMatchers.contains())
                    .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains())
                    .withMatcher("dvName", ExampleMatcher.GenericPropertyMatchers.contains())
                    .withMatcher("department", ExampleMatcher.GenericPropertyMatchers.contains())
                    .withMatcher("userId", ExampleMatcher.GenericPropertyMatchers.exact())
                    .withNullHandler(ExampleMatcher.NullHandler.IGNORE)
                    .withIgnoreNullValues();

            Example<Translation> example = Example.of(translation, matcher);

            /*childs.add(translation.getUserId());*/
            Query query = new Query();
            if (translation.getDates() != null)
                query.addCriteria(Criteria.where("time").gte(translation.getDates().get(0))
                        .lte(translation.getDates().get(1)));
            Page<Translation> page = translationResposity.findAll(example, query, PageRequest.of(0 ,2000 ,Sort.by(Sort.Direction.DESC, "time")));

            String path = System.currentTimeMillis() + ".xlsx";
            EasyExcel.write("upload" + File.separator + path, Translation.class).sheet("sheet").doWrite(page.getContent());
            return new ResultUtil(0, path, "");
        } catch (Exception e) {
            log.error(e);
            return new ResultUtil(-1, e.getMessage());
        }
    }
}
