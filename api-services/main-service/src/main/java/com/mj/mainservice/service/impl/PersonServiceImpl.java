package com.mj.mainservice.service.impl;


import com.alibaba.fastjson.JSON;
import com.jian.common.util.ResultUtil;

import com.mj.mainservice.entitys.access.AccessPerson;
import com.mj.mainservice.entitys.parking.ParkingUserInfo;
import com.mj.mainservice.entitys.person.PersonInfo;
import com.mj.mainservice.resposity.access.AccessPersonResposity;
import com.mj.mainservice.resposity.parking.ParkingPersonResposity;
import com.mj.mainservice.resposity.person.PersonRepository;
import com.mj.mainservice.service.person.PersonService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;


@Service
@Log4j2
public class PersonServiceImpl implements PersonService {
    //    @Resource
//    private MongoTemplate mongoTemplate;
    @Resource
    private PersonRepository personRepository;
    @Resource
    private AccessPersonResposity accessPersonResposity ;
    @Resource
    private ParkingPersonResposity parkingPersonResposity;


    @Override
    @Transactional
    public ResultUtil insertPerson(PersonInfo info) {
        try {
            if(personRepository.findById(info.getId()).isPresent())
                return new ResultUtil(-1,"该ID已存在");
            PersonInfo personInfo1 = personRepository.findByAccessIdEquals(info.getAccessId());
            if(personInfo1 != null)
                return new ResultUtil(-1,"该门禁卡号已经被注册");
            info.setCreateTime(LocalDateTime.now(ZoneId.systemDefault()));
            personRepository.insert(info);
            return ResultUtil.ok();
        } catch (Exception e) {
            log.error(e);
            return new ResultUtil(-1, e.getMessage());
        }

    }

    @Override
    public ResultUtil queryPersonsList(PersonInfo info) {
        try {
            PageRequest pageRequest = PageRequest.of(info.getPage() - 1, info.getLimit(), Sort.by(Sort.Direction.DESC, "createTime"));
            List<PersonInfo> personInfos = new ArrayList<PersonInfo>();
            long total = 0;
//            if (StringUtils.isNotEmpty(info.getId()) || StringUtils.isNotEmpty(info.getCarId()) ||
//                    StringUtils.isNotEmpty(info.getName()) || StringUtils.isNotEmpty(info.getAccessId()) || info.getRole() != null) {
                ExampleMatcher matcher = ExampleMatcher.matching()
                        .withMatcher("id", ExampleMatcher.GenericPropertyMatchers.exact())
                        .withMatcher("userId" , ExampleMatcher.GenericPropertyMatchers.exact())
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                        .withIgnorePaths("page", "limit","accessPw")
                        .withNullHandler(ExampleMatcher.NullHandler.IGNORE);
                Example<PersonInfo> example = Example.of(info, matcher);

                Page<PersonInfo> personInfos1 = personRepository.findAll(example, pageRequest);
                personInfos = personInfos1.toList();
                total = personInfos1.getTotalElements();
//            } else {
//                Page<PersonInfo> personInfos2 = personRepository.findAll(pageRequest);
//                personInfos = personInfos2.toList();
//                total = personInfos2.getTotalElements();
//            }


            ResultUtil resultUtil = new ResultUtil();
            resultUtil.setCode(0);
            resultUtil.setCount(total);
            resultUtil.setData(personInfos);
            return resultUtil;

        } catch (Exception e) {
            log.error(e);
            return new ResultUtil(-1, e.getMessage());
        }

    }

    @Override
    @Transactional
    public ResultUtil editPerson(PersonInfo info) {
        try {
          personRepository.save(info);
          return ResultUtil.ok();
        }catch (Exception e){
            log.error(e.getMessage());
            return  new ResultUtil(-1 , e.getMessage());
        }

    }

    @Override
    @Transactional
    public ResultUtil delPerson(List<String> ids) {
        try{
            List<String>  dels = new ArrayList<>();
            ids.stream().forEach(id ->{
                List<AccessPerson> accessPerson = accessPersonResposity.findAllByPidEquals(id);
                if(accessPerson.size() > 0){
                    dels.add(id);
                    return;
                }
                List<ParkingUserInfo>  parkingUserInfos = parkingPersonResposity.findAllByPersonIdEquals(id);
                if(parkingUserInfos.size() > 0){
                    dels.add(id);
                    return;
                }
                personRepository.deleteById(id);
            });
            if(dels.size() > 0)
                return new ResultUtil(-2, "存在已下发人员，请先删除从门禁设备/车辆删除,人员ID:"+ JSON.toJSONString(dels));

            return  ResultUtil.ok();
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResultUtil(-1 , e.getMessage());
        }
    }

    @Override
    public List<PersonInfo>  quryPersonListNoPage(PersonInfo personInfo) {
        try {



//            if (StringUtils.isNotEmpty(info.getId()) || StringUtils.isNotEmpty(info.getCarId()) ||
//                    StringUtils.isNotEmpty(info.getName()) || StringUtils.isNotEmpty(info.getAccessId()) || info.getRole() != null) {
            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withMatcher("id", ExampleMatcher.GenericPropertyMatchers.exact())
                    .withMatcher("userId" , ExampleMatcher.GenericPropertyMatchers.exact())
                    .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                    .withIgnorePaths("page", "limit","accessPw")
                    .withNullHandler(ExampleMatcher.NullHandler.IGNORE);
            Example<PersonInfo> example = Example.of(personInfo, matcher);

            List<PersonInfo> personInfos   = personRepository.findAll(example);

            return personInfos ;

        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    @Override
    public PersonInfo getPersonById(String id) {
        try {
         PersonInfo personInfo =   personRepository.findById(id).get();
         return  personInfo;
        }catch (Exception e){
            log.error(e);
            return  null;
        }
    }
}
