package com.mj.mainservice.service;

import com.jian.common.util.ResultUtil;
import com.mj.mainservice.entitys.attence.AttenceReport;

/**
 * @auther JianLinWei
 * @date 2020-11-15 23:09
 */
public interface AttenceReportService {

    ResultUtil getAttenceReport(AttenceReport attenceReport );
}
