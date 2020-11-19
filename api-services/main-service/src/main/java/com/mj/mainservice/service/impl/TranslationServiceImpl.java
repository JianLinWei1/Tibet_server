package com.mj.mainservice.service.impl;

import com.jian.common.util.ResultUtil;
import com.mj.mainservice.entitys.person.PersonInfo;
import com.mj.mainservice.entitys.access.Translation;
import com.mj.mainservice.entitys.attence.AttenceReport;
import com.mj.mainservice.resposity.access.TranslationResposity;
import com.mj.mainservice.service.person.PersonService;
import com.mj.mainservice.service.access.TranslationService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @auther JianLinWei
 * @date 2020-11-15 19:45
 */
@Service
@Log4j2
public class TranslationServiceImpl implements TranslationService {
    @Resource
    private TranslationResposity translationResposity;

//    @Resource
//    private AccessPersonFeign personFeign;

    @Resource
    private PersonService personService;

    @Override
    public ResultUtil getAttenceReport(AttenceReport attenceReport) {
        try {

            List<LocalDate> localDates = new ArrayList<>();
            //当天
            if(attenceReport.getCon()!= null && attenceReport.getCon() == 1){
                localDates.add(LocalDate.now());
                localDates.add(LocalDate.now());
                attenceReport.setTimes(localDates);
            }
            //最近一周
            if(attenceReport.getCon() != null && attenceReport.getCon() == 2){
               localDates.add(LocalDate.now().minusWeeks(1));
               localDates.add(LocalDate.now());
            }
            //最近一月
            if (attenceReport.getCon() != null && attenceReport.getCon() ==3){
                localDates.add(LocalDate.now().minusMonths(1));
                localDates.add(LocalDate.now());
            }

           /* //如果条件满足 通过matcher查询
            if(attenceReport.getPersonId() != null || attenceReport.getName() != null){
                List<AttenceReport>  attenceReports = getReportMatcher(attenceReport);
                return ResultUtil.ok(attenceReports);
            }*/
           attenceReport.setTimes(localDates);
            List<AttenceReport> attenceReports = getReportMatcher(attenceReport);


            return  ResultUtil.ok(attenceReports);
        }catch (Exception e){
            log.error(e);
            return  null;
        }
    }


    public List<AttenceReport> getReportMatcher(AttenceReport report){

        PersonInfo accessPersonVo = new PersonInfo();
        accessPersonVo.setUserId(report.getUserId());
        if(StringUtils.isNotEmpty(report.getName()))
            accessPersonVo.setName(report.getName());
        if(StringUtils.isNotEmpty(report.getPersonId()))
            accessPersonVo.setId(report.getPersonId());
        List<PersonInfo> personInfos = personService.quryPersonListNoPage(accessPersonVo);
   if(personInfos==null)
       return null;
        List<AttenceReport> attenceReports = new ArrayList<>();
        personInfos.stream().forEach(p ->{
            AttenceReport attenceReport = new AttenceReport();
            attenceReport.setPersonId(p.getId());
            attenceReport.setName(p.getName());
            attenceReport.setTimes(report.getTimes());
            //查询考勤的记录
            Date date1 = Date.from(report.getTimes().get(0).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            Date date2 = Date.from(report.getTimes().get(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            List<Translation> translations = translationResposity.findAllByPersonIdAndSnInAndTimeBetween(p.getId(),
                    report.getConfig().getSns() ,date1  ,date2 );
            //迟到早退次数
            AtomicInteger lateCount = new AtomicInteger();
            AtomicInteger earlyCount = new AtomicInteger();
            List<LocalDateTime> localDateTimes = new ArrayList<>();
            translations.stream().forEach(t->{
                      localDateTimes.add(t.getTime());
                      if(t.getTime().toLocalTime().isAfter(report.getConfig().getAmClockIn()))
                          lateCount.getAndIncrement();
                      if(t.getTime().toLocalTime().isBefore(report.getConfig().getAmClockOut()))
                          earlyCount.getAndIncrement();
                      if(t.getTime().toLocalTime().isAfter(report.getConfig().getPmClockIn()))
                          lateCount.getAndIncrement();
                      if(t.getTime().toLocalTime().isBefore(report.getConfig().getPmClockOut()));
                        earlyCount.getAndIncrement();
            });
            attenceReport.setLateCount(lateCount.get());
            attenceReport.setEarlyCount(earlyCount.get());
            attenceReport.setTimeList(localDateTimes);
            attenceReports.add(attenceReport);
        });

        return  attenceReports;
    }


}
