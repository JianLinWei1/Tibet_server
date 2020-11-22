package com.mj.mainservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.jian.common.util.ResultUtil;
import com.mj.mainservice.entitys.access.Translation;
import com.mj.mainservice.entitys.attence.AttenceConfig;
import com.mj.mainservice.entitys.attence.AttenceReport;
import com.mj.mainservice.resposity.attence.AttenceConfigResposity;

import com.mj.mainservice.service.attence.AttenceReportService;
import com.mj.mainservice.service.access.TranslationService;
import com.mj.mainservice.vo.attence.AttenceReportVo;
import lombok.extern.log4j.Log4j2;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @auther JianLinWei
 * @date 2020-11-15 23:09
 */
@Service
@Log4j2
public class AttenceReportServiceImpl   implements AttenceReportService {
    @Resource
    private AttenceConfigResposity configResposity;
   /* @Resource
    private AttenceAccessFeign accessFeign;*/
   @Resource
   private TranslationService translationService;

    @Override
    public ResultUtil getAttenceReport(AttenceReport attenceReport ) {
        try {
            AttenceConfig attenceConfig = configResposity.findById(attenceReport.getUserId()).get();
           attenceReport.setConfig(attenceConfig);
            return  translationService.getAttenceReport(attenceReport);
        }catch (Exception e){
            log.error(e);
            return  new ResultUtil(-1, e.getMessage());
        }
    }


    @Override
    public ResultUtil exportAttenceReport(List<AttenceReportVo> attenceReportList) {
        try {
           attenceReportList.stream().forEach(attenceReportVo -> {
               StringBuilder times_str = new StringBuilder();
               attenceReportVo.getTimes().stream().forEach(t->{
                    times_str.append(t.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    times_str.append("~");
               });
               StringBuilder timeList_str = new StringBuilder();
               attenceReportVo.getTimeList().stream().forEach(t ->{
                    timeList_str.append(t.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                   timeList_str.append("  , ");
               });
               attenceReportVo.setTimes_str(times_str.toString());
               attenceReportVo.setTimeList_str(timeList_str.toString());

           });


            String path = System.currentTimeMillis()+".xlsx";
            EasyExcel.write("upload"+ File.separator +path , AttenceReportVo.class).sheet().doWrite(attenceReportList);
            return new ResultUtil(0 ,path , "");
        }catch (Exception e){
            log.error(e);
            return new ResultUtil(-1, e.getMessage());
        }
    }
}
