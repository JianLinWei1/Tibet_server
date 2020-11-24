package com.mj.mainservice.service.impl;

import com.alibaba.excel.EasyExcel;

import com.jian.common.util.ResultUtil;

import com.mj.mainservice.entitys.person.PersonInfo;
import com.mj.mainservice.entitys.parking.ParkInfo;
import com.mj.mainservice.entitys.parking.ParkingResult;
import com.mj.mainservice.entitys.parking.ParkingUserInfo;
import com.mj.mainservice.resposity.parking.ParkingPersonResposity;
import com.mj.mainservice.resposity.parking.ParkingResposity;
import com.mj.mainservice.resposity.parking.ParkingResultResposity;
import com.mj.mainservice.service.parking.ParkingVoService;
import com.mj.mainservice.service.person.PersonService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

/**
 * Created by MrJan on 2020/10/29
 */

@Service
@Log4j2
public class ParkingVoServiceImpl implements ParkingVoService {
    @Resource
    private ParkingResposity parkingResposity;
    /* @Resource
     private ParkingPersonFeign parkingPersonFeign;*/
    @Resource
    private PersonService personService;
    @Resource
    private ParkingPersonResposity parkingPersonResposity;
    @Resource
    private ParkingResultResposity parkingResultResposity;


    @Override
    public ResultUtil listParking(ParkInfo parkInfo) {
        try {


            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withMatcher("ipaddr", ExampleMatcher.GenericPropertyMatchers.exact())
                    .withMatcher("userId", ExampleMatcher.GenericPropertyMatchers.exact())
                    .withMatcher("device_name", ExampleMatcher.GenericPropertyMatchers.contains())
                    .withNullHandler(ExampleMatcher.NullHandler.IGNORE)
                    .withIgnorePaths("page", "limit");

            Pageable pageable = PageRequest.of(parkInfo.getPage(), parkInfo.getLimit());
            Example<ParkInfo> example = Example.of(parkInfo, matcher);
//            childs.add(parkInfo.getUserId());
//            Query query = new Query();
//            query.addCriteria(Criteria.where("userId").in(childs));
            Page<ParkInfo> page = parkingResposity.findAll(example, pageable);
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
    @Transactional
    public ResultUtil saveParkInfo(ParkInfo parkInfo) {
        try {
            parkingResposity.save(parkInfo);
            return ResultUtil.ok();
        } catch (Exception e) {
            log.error(e);
            return new ResultUtil(-1, e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResultUtil delParkInfo(List<String> Ids) {
        try {
            Ids.stream().forEach(id -> {
                parkingResposity.deleteById(id);
            });

            return ResultUtil.ok();
        } catch (Exception e) {
            log.error(e);
            return new ResultUtil(-1, e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResultUtil saveParkPersonInfo(ParkingUserInfo parkingUserInfo) {
        try {
            parkingUserInfo.getPersonIds().stream().forEach(id -> {
                ParkingUserInfo parkingUserInfo1 = new ParkingUserInfo();
                parkingUserInfo.setId(null);
                parkingUserInfo1 = parkingUserInfo;
                PersonInfo personInfo = personService.getPersonById(id);
                parkingUserInfo1.setAction(0);
                parkingUserInfo1.setCarId(personInfo.getCarId());
                parkingUserInfo1.setPersonId(id);
                parkingUserInfo1.setName(personInfo.getName());
                parkingUserInfo1.setPersonIds(null);
                parkingUserInfo1.setStatus(false);
                parkingPersonResposity.save(parkingUserInfo1);
                log.info(personInfo.getName());

            });
            return ResultUtil.ok();
        } catch (Exception e) {
            log.error(e);
            return new ResultUtil(-1, e.getMessage());
        }
    }


    @Override
    public ResultUtil listParkingPerson(ParkingUserInfo parkingUserInfo) {
        try {

            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withIgnorePaths("page", "limit")
                    .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains())
                    .withMatcher("carId", ExampleMatcher.GenericPropertyMatchers.contains());
            Example<ParkingUserInfo> example = Example.of(parkingUserInfo, matcher);
            Pageable pageable = PageRequest.of(parkingUserInfo.getPage(), parkingUserInfo.getLimit());
//            Query query = new Query();
//            childs.add(parkingUserInfo.getUserId());
//            query.addCriteria(Criteria.where("action").ne(1));
//            query.addCriteria(Criteria.where("userId").in(childs));
            Page<ParkingUserInfo> page = parkingPersonResposity.findAll(example,  pageable);
            ResultUtil resultUtil = new ResultUtil();
            resultUtil.setCode(0);
            resultUtil.setData(page.getContent());
            resultUtil.setCount(page.getTotalElements());
            return resultUtil;

        } catch (Exception e) {
            log.error(e);
            return new ResultUtil(-1, e.getMessage());
        }
    }

    @Override
    public ResultUtil delParkingPerson(List<String> ids) {
        try {
            ids.stream().forEach(id -> {
                ParkingUserInfo userInfo = parkingPersonResposity.findById(id).get();
                userInfo.setStatus(false);
                userInfo.setAction(1);
                parkingPersonResposity.save(userInfo);

            });
            return ResultUtil.ok();
        } catch (Exception e) {
            log.error(e);
            return new ResultUtil(-1, e.getMessage());
        }
    }

    @Override
    public ResultUtil listParkingResult(ParkingResult parkingResult) {
        try {
            ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("ipaddr", ExampleMatcher.GenericPropertyMatchers.exact())
                    .withMatcher("plateid", ExampleMatcher.GenericPropertyMatchers.contains())
                    .withIgnorePaths("page", "limit");
            Example<ParkingResult> example = Example.of(parkingResult, matcher);
            Page<ParkingResult> page = parkingResultResposity.findAll(example, PageRequest.of(parkingResult.getPage(), parkingResult.getLimit()));

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
    public ResultUtil delParkingResult(List<String> ids) {
        try {
            ids.stream().forEach(id -> {
                parkingResultResposity.deleteById(id);
            });
            return ResultUtil.ok();
        } catch (Exception e) {
            log.error(e);
            return new ResultUtil(-1, e.getMessage());
        }

    }

    @Override
    public ResultUtil exportRecords(List<ParkingResult> results) {
        try {
            String path = System.currentTimeMillis() + ".xlsx";
            EasyExcel.write("upload" + File.separator + path, ParkingResult.class).sheet().doWrite(results);
            return new ResultUtil(0, path, "");
        } catch (Exception e) {
            log.error(e);
            return new ResultUtil(-1, e.getMessage());
        }
    }
}
