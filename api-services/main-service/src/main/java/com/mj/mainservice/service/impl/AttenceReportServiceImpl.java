package com.mj.mainservice.service.impl;

import com.jian.attenceservice.resposity.AttenceConfigResposity;
import com.jian.attenceservice.service.AttenceAccessFeign;
import com.jian.attenceservice.service.AttenceReportService;
import com.jian.common.entitys.attence.AttenceConfig;
import com.jian.common.entitys.attence.AttenceReport;
import com.jian.common.util.ResultUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @auther JianLinWei
 * @date 2020-11-15 23:09
 */
@Service
@Log4j2
public class AttenceReportServiceImpl   implements AttenceReportService {
    @Resource
    private AttenceConfigResposity configResposity;
    @Resource
    private AttenceAccessFeign accessFeign;

    @Override
    public ResultUtil getAttenceReport(AttenceReport  attenceReport ) {
        try {
            AttenceConfig attenceConfig = configResposity.findById(attenceReport.getUserId()).get();
           attenceReport.setConfig(attenceConfig);
            return  accessFeign.getAttenceReport(attenceReport);
        }catch (Exception e){
            log.error(e);
            return  new ResultUtil(-1, e.getMessage());
        }
    }
}
