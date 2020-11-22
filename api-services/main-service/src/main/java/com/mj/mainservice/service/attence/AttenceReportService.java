package com.mj.mainservice.service.attence;

import com.jian.common.util.ResultUtil;
import com.mj.mainservice.entitys.attence.AttenceReport;
import com.mj.mainservice.vo.attence.AttenceReportVo;

import java.util.List;

/**
 * @auther JianLinWei
 * @date 2020-11-15 23:09
 */
public interface AttenceReportService {

    ResultUtil getAttenceReport(AttenceReport attenceReport );

    ResultUtil exportAttenceReport(List<AttenceReportVo> attenceReportList);
}
