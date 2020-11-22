package com.mj.mainservice.service.impl;


import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.jian.common.util.ResultUtil;

import com.mj.mainservice.entitys.access.AccessPerson;
import com.mj.mainservice.entitys.access.Translation;
import com.mj.mainservice.entitys.parking.ParkingUserInfo;
import com.mj.mainservice.entitys.person.PersonInfo;
import com.mj.mainservice.resposity.access.AccessPersonResposity;
import com.mj.mainservice.resposity.parking.ParkingPersonResposity;
import com.mj.mainservice.resposity.person.PersonRepository;
import com.mj.mainservice.service.person.PersonService;
import com.mj.mainservice.vo.person.PersonInfoVo;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
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
    private MongoTemplate mongoTemplate;
    @Resource
    private AccessPersonResposity accessPersonResposity;
    @Resource
    private ParkingPersonResposity parkingPersonResposity;
    @Value("${server.port}")
    private String port;


    @Override
    @Transactional
    public ResultUtil insertPerson(PersonInfo info) {
        try {
            if (personRepository.findById(info.getId()).isPresent())
                return new ResultUtil(-1, "该ID已存在");
            PersonInfo personInfo1 = personRepository.findByAccessIdEquals(info.getAccessId());
            if (personInfo1 != null)
                return new ResultUtil(-1, "该门禁卡号已经被注册");
            info.setCreateTime(LocalDateTime.now(ZoneId.systemDefault()));
            personRepository.insert(info);
            return ResultUtil.ok();
        } catch (Exception e) {
            log.error(e);
            return new ResultUtil(-1, e.getMessage());
        }

    }

    @Override
    public ResultUtil queryPersonsList(PersonInfo info, List<String> childs) {
        try {
            PageRequest pageRequest = PageRequest.of(info.getPage() - 1, info.getLimit(), Sort.by(Sort.Direction.DESC, "createTime"));
            List<PersonInfo> personInfos = new ArrayList<PersonInfo>();
            long total = 0;
//            if (StringUtils.isNotEmpty(info.getId()) || StringUtils.isNotEmpty(info.getCarId()) ||
//                    StringUtils.isNotEmpty(info.getName()) || StringUtils.isNotEmpty(info.getAccessId()) || info.getRole() != null) {
            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withMatcher("id", ExampleMatcher.GenericPropertyMatchers.exact())
                   // .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                    .withIgnorePaths("page", "limit", "accessPw","userId")
                    .withNullHandler(ExampleMatcher.NullHandler.IGNORE)
                    .withIgnoreNullValues();

            childs.add(info.getUserId());
            Example<PersonInfo> example = Example.of(info, matcher);
            Query query = new Query();
            query.addCriteria(Criteria.where("userId").in(childs));


            Page<PersonInfo> personInfos1 = personRepository.findAll(example, query, pageRequest);

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
    public ResultUtil editPerson(PersonInfoVo info) {
        try {
            PersonInfo personInfo = new PersonInfo();
            BeanUtils.copyProperties(info, personInfo);
            if (StringUtils.isNotEmpty(info.getOid()))
                personRepository.deleteById(info.getOid());

            personRepository.save(personInfo);


            return ResultUtil.ok();
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResultUtil(-1, e.getMessage());
        }

    }

    @Override
    @Transactional
    public ResultUtil delPerson(List<String> ids) {
        try {
            List<String> dels = new ArrayList<>();
            ids.stream().forEach(id -> {
                List<AccessPerson> accessPerson = accessPersonResposity.findAllByPidEquals(id);
                if (accessPerson.size() > 0) {
                    dels.add(id);
                    return;
                }
                List<ParkingUserInfo> parkingUserInfos = parkingPersonResposity.findAllByPersonIdEquals(id);
                if (parkingUserInfos.size() > 0) {
                    dels.add(id);
                    return;
                }
                personRepository.deleteById(id);
            });
            if (dels.size() > 0)
                return new ResultUtil(-2, "存在已下发人员，请先删除从门禁设备/车辆删除,人员ID:" + JSON.toJSONString(dels));

            return ResultUtil.ok();
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResultUtil(-1, e.getMessage());
        }
    }

    @Override
    public List<PersonInfo> quryPersonListNoPage(PersonInfo personInfo ) {
        try {


//            if (StringUtils.isNotEmpty(info.getId()) || StringUtils.isNotEmpty(info.getCarId()) ||
//                    StringUtils.isNotEmpty(info.getName()) || StringUtils.isNotEmpty(info.getAccessId()) || info.getRole() != null) {
            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withMatcher("id", ExampleMatcher.GenericPropertyMatchers.exact())
                    .withMatcher("userId", ExampleMatcher.GenericPropertyMatchers.exact())
                    //.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                    .withIgnorePaths("page", "limit", "accessPw","userId")
                    .withNullHandler(ExampleMatcher.NullHandler.IGNORE);
            Example<PersonInfo> example = Example.of(personInfo, matcher);
            Query query = new Query();
            query.addCriteria(Criteria.byExample(example));

            List<PersonInfo> personInfos = personRepository.findAll(example);

            return personInfos;

        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    @Override
    public PersonInfo getPersonById(String id) {
        try {
            PersonInfo personInfo = personRepository.findById(id).get();
            return personInfo;
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }


    @Override
    public ResultUtil exportPerson(List<PersonInfoVo> personInfoVo) {
        try {
            personInfoVo.stream().forEach(p->{
                try {
                    p.setPhotoUrl(new URL("http://localhost:"+port+"/"+p.getPhoto().replace("\\", "/")));
                } catch (MalformedURLException e) {
                    log.error(e);
                }
            });
            String path = System.currentTimeMillis()+".xlsx";
            EasyExcel.write("upload"+ File.separator +path , PersonInfoVo.class).sheet().doWrite(personInfoVo);
            return new ResultUtil(0 ,path , "");
        }catch (Exception e){
            log.error(e);
            return new ResultUtil(-1, e.getMessage());
        }
    }
}
