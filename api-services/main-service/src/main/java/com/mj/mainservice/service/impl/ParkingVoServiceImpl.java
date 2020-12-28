package com.mj.mainservice.service.impl;

import com.alibaba.excel.EasyExcel;

import com.jian.common.entity.AntdTree;
import com.jian.common.util.ResultUtil;

import com.mj.mainservice.entitys.person.PersonInfo;
import com.mj.mainservice.entitys.parking.ParkInfo;
import com.mj.mainservice.entitys.parking.ParkingResult;
import com.mj.mainservice.entitys.parking.ParkingUserInfo;
import com.mj.mainservice.resposity.parking.ParkingPersonResposity;
import com.mj.mainservice.resposity.parking.ParkingResposity;
import com.mj.mainservice.resposity.parking.ParkingResultResposity;
import com.mj.mainservice.resposity.person.PersonRepository;
import com.mj.mainservice.service.parking.ParkingVoService;
import com.mj.mainservice.service.person.PersonService;
import com.mj.mainservice.vo.access.BatchIssueVo;
import com.mj.mainservice.vo.parking.TmpPersonVo;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    @Resource
    private PersonRepository personRepository;


    @Override
    public ResultUtil listParking(ParkInfo parkInfo) {
        try {


            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withMatcher("ipaddr", ExampleMatcher.GenericPropertyMatchers.exact())
                    .withMatcher("userId", ExampleMatcher.GenericPropertyMatchers.exact())
                    .withMatcher("device_name", ExampleMatcher.GenericPropertyMatchers.contains())
                    .withNullHandler(ExampleMatcher.NullHandler.IGNORE)
                    .withIgnorePaths("page", "limit");

            Pageable pageable = PageRequest.of(parkInfo.getPage()-1, parkInfo.getLimit());
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
            if(parkingUserInfo.getCarId() == null || parkingUserInfo.getCarId().size() <=0 )
                return new ResultUtil(-1,"车牌和人员下发二选一不能为空");

            if (parkingUserInfo.getPersonIds() != null && parkingUserInfo.getPersonIds().size() > 0) {
                parkingUserInfo.getPersonIds().stream().forEach(id -> {
                    ParkingUserInfo parkingUserInfo1 = new ParkingUserInfo();
                    parkingUserInfo.setId(null);
                    parkingUserInfo1 = parkingUserInfo;
                    PersonInfo personInfo = personService.getPersonById(id);
                    parkingUserInfo1.setAction(0);
                   /* if(parkingUserInfo.getCarId() != null)
                        personInfo.setCarId(parkingUserInfo.getCarId());*/
                    parkingUserInfo1.setCarId(personInfo.getCarId());
                    parkingUserInfo1.setPersonId(id);
                    parkingUserInfo1.setName(personInfo.getName());
                    parkingUserInfo1.setPersonIds(null);
                    parkingUserInfo1.setStatus(false);
                    parkingUserInfo1.setDepartment(personInfo.getDepartment());
                    //排除已下发的
                    Optional<ParkingUserInfo>  optional1 = parkingPersonResposity.findAllByCarIdEqualsAndSerialnoIsAndActionIsNot(personInfo.getCarId(),
                            parkingUserInfo.getSerialno() ,1);
                    if(optional1.isPresent())
                        return;

                    parkingPersonResposity.save(parkingUserInfo1);
                    log.info(personInfo.getName());

                });
            } else {
                //排除已下发的
                Optional<ParkingUserInfo>  optional1 = parkingPersonResposity.findAllByCarIdEqualsAndSerialnoIsAndActionIsNot(parkingUserInfo.getCarId(),
                        parkingUserInfo.getSerialno() ,1);
                if(optional1.isPresent())
                    return  new ResultUtil(-1 ,"该临时车牌已经下发");
                parkingUserInfo.setAction(0);
                parkingUserInfo.setStatus(false);
                parkingPersonResposity.save(parkingUserInfo);
            }


            return ResultUtil.ok();
        } catch (
                Exception e) {
            log.error(e);
            return new ResultUtil(-1, e.getMessage());
        }

    }


    @Override
    public ResultUtil listParkingPerson(ParkingUserInfo parkingUserInfo) {
        try {

            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withIgnorePaths("page", "limit","carId")
                    .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains())
                    ;
            Example<ParkingUserInfo> example = Example.of(parkingUserInfo, matcher);
            Pageable pageable = PageRequest.of(parkingUserInfo.getPage()-1, parkingUserInfo.getLimit());
            Query query = new Query();
            //childs.add(parkingUserInfo.getUserId());
            query.addCriteria(Criteria.where("action").ne(1));
            if(parkingUserInfo.getCarId()!= null && parkingUserInfo.getCarId().size()>0)
            query.addCriteria(Criteria.where("carId").in(parkingUserInfo.getCarId()));
            //query.addCriteria(Criteria.where("userId").in(childs));
            Page<ParkingUserInfo> page = parkingPersonResposity.findAll(example, query, pageable);
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
                    .withMatcher("license", ExampleMatcher.GenericPropertyMatchers.contains())
                    .withIgnorePaths("page", "limit");
            Example<ParkingResult> example = Example.of(parkingResult, matcher);
            Page<ParkingResult> page = parkingResultResposity.findAll(example, PageRequest.of(parkingResult.getPage()-1,
                    parkingResult.getLimit() , Sort.by(Sort.Direction.DESC, "time")));

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
            EasyExcel.write("upload" + File.separator + path, ParkingResult.class).sheet("sheet").doWrite(results);
            return new ResultUtil(0, path, "");
        } catch (Exception e) {
            log.error(e);
            return new ResultUtil(-1, e.getMessage());
        }
    }

    @Override
    public ResultUtil exportSearchRecords(ParkingResult parkingResult) {
        try {
            ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("ipaddr", ExampleMatcher.GenericPropertyMatchers.exact())
                    .withMatcher("plateid", ExampleMatcher.GenericPropertyMatchers.contains())
                    .withIgnorePaths("page", "limit");
            Example<ParkingResult> example = Example.of(parkingResult, matcher);
            Page<ParkingResult> page = parkingResultResposity.findAll(example, PageRequest.of(0,
                    2000 , Sort.by(Sort.Direction.DESC, "time")));

            String path = System.currentTimeMillis() + ".xlsx";
            EasyExcel.write("upload" + File.separator + path, ParkingResult.class).sheet("sheet").doWrite(page.getContent());
            return new ResultUtil(0, path, "");

        } catch (Exception e) {
            log.error(e);
            return new ResultUtil(-1, e.getMessage());
        }
    }

    @Override
    public Long countParking(String userId) {
        try {
            ParkInfo  info = new ParkInfo();
            info.setUserId(userId);
            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withMatcher("ipaddr", ExampleMatcher.GenericPropertyMatchers.exact())
                    .withMatcher("userId", ExampleMatcher.GenericPropertyMatchers.exact())
                    .withMatcher("device_name", ExampleMatcher.GenericPropertyMatchers.contains())
                    .withNullHandler(ExampleMatcher.NullHandler.IGNORE)
                    .withIgnorePaths("page", "limit");
            Example<ParkInfo> example = Example.of(info, matcher);
            long count = parkingResposity.count(example);
            return  count;
        }catch (Exception e){
            log.error(e);
            return  0l;
        }
    }

    @Override
    public ResultUtil getParingTree(String userId) {
        try {
           List<ParkInfo>  parkInfos = parkingResposity.findAllByUserIdEquals(userId);
            List<AntdTree> antdTrees = new ArrayList<>();
            List<AntdTree> antdTrees2 = new ArrayList<>();
            parkInfos.stream().forEach(dv -> {
                AntdTree antdTree = new AntdTree();
                antdTree.setKey(dv.getSerialno());
                antdTree.setValue(dv.getIpaddr());
                antdTree.setTitle(dv.getDevice_name());
                antdTrees.add(antdTree);
            });

            AntdTree antdTree = new AntdTree();
            antdTree.setKey("-");
            antdTree.setTitle("选择车辆设备");
            antdTree.setValue("-");
            antdTree.setChildren(antdTrees);
            antdTrees2.add(antdTree);
            ResultUtil resultUtil = new ResultUtil();
            resultUtil.setCode(0);
            resultUtil.setData(antdTrees2);
            return resultUtil;
        }catch (Exception e){
            log.error(e);
            return  new ResultUtil(-1, e.getMessage());
        }
    }

    @Override
    public ResultUtil batchIssue(BatchIssueVo issueVo) {
        try {

            issueVo.getPids().forEach(pid ->{
                Optional<PersonInfo>  optional = personRepository.findById(pid);
                if(!optional.isPresent())
                    return;
                PersonInfo personInfo = optional.get();
                if(personInfo.getCarId()== null ||personInfo.getCarId().size()<=0 || StringUtils.isEmpty(personInfo.getCarId().get(0)))
                    return;

                ParkingUserInfo parkingUserInfo = new ParkingUserInfo();
                parkingUserInfo.setAction(0);
                parkingUserInfo.setCarId(personInfo.getCarId());
                parkingUserInfo.setDepartment(personInfo.getDepartment());
                parkingUserInfo.setEnable(1);
                parkingUserInfo.setNeed_alarm(0);
                parkingUserInfo.setEnable_time(LocalDateTime.now());
                parkingUserInfo.setName(personInfo.getName());
                parkingUserInfo.setPersonId(pid);
                parkingUserInfo.setUserId(personInfo.getUserId());
                parkingUserInfo.setStatus(false);
                parkingUserInfo.setOverdue_time(null);
                issueVo.getDvIds().forEach(dv ->{
                    Optional<ParkInfo> optional2 =parkingResposity.findById(dv);
                    if(!optional2.isPresent())
                        return;
                    //排除已下发的
                    Optional<ParkingUserInfo>  optional1 = parkingPersonResposity.findAllByPersonIdEqualsAndCarIdEqualsAndSerialnoIsAndActionIsNot(pid,
                            personInfo.getCarId(),optional2.get().getSerialno() ,1);
                    if(optional1.isPresent())
                        return;
                    ParkInfo  parkInfo = optional2.get();
                    parkingUserInfo.setId(null);
                    parkingUserInfo.setSerialno(parkInfo.getSerialno());
                    parkingUserInfo.setDevice_name(parkInfo.getDevice_name());
                    parkingPersonResposity.save(parkingUserInfo);
                });
            });


            return ResultUtil.ok();
        }catch (Exception e){
            log.error(e);
            return  new ResultUtil(-1,e.getMessage());
        }
    }

    @Override
    public ResultUtil batchIssue(MultipartFile file, List<String> parkIds) {
        try {
          List<TmpPersonVo>  tmpPersonVos = EasyExcel.read(file.getInputStream()).head(TmpPersonVo.class).sheet().doReadSync();
          tmpPersonVos.stream().forEach(t->{
               if(StringUtils.isEmpty(t.getCarId()))
                   return;
              ParkingUserInfo parkingUserInfo = new ParkingUserInfo();
              parkingUserInfo.setAction(0);
              List<String> carids =new ArrayList<>();
              carids.add(t.getCarId());
              parkingUserInfo.setCarId(carids);

              parkingUserInfo.setEnable(1);
              parkingUserInfo.setNeed_alarm(0);
              parkingUserInfo.setEnable_time(t.getStartTime());

              //parkingUserInfo.setUserId(personInfo.getUserId());
              parkingUserInfo.setStatus(false);
              parkingUserInfo.setOverdue_time(t.getEndTime());
              parkIds.forEach(dv ->{
                  Optional<ParkInfo> optional2 =parkingResposity.findById(dv);
                  if(!optional2.isPresent())
                      return;
                  //排除已下发的
                  Optional<ParkingUserInfo>  optional1 = parkingPersonResposity.findAllByCarIdEqualsAndSerialnoIsAndActionIsNot(carids ,dv ,1);
                  if(optional1.isPresent()){

                      return;
                  }

                  ParkInfo  parkInfo = optional2.get();
                  parkingUserInfo.setId(null);
                  parkingUserInfo.setSerialno(parkInfo.getSerialno());
                  parkingUserInfo.setDevice_name(parkInfo.getDevice_name());
                  parkingUserInfo.setUserId(parkInfo.getUserId());
                  parkingPersonResposity.save(parkingUserInfo);
              });
          });

          return  ResultUtil.ok();
        }catch (Exception e){
            log.error(e);
            return  new ResultUtil(-1 ,"请检查Excel文件（注意日期格式)");
        }
    }
}
