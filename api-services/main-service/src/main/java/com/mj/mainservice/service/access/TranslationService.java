package com.mj.mainservice.service.access;


import com.jian.common.util.ResultUtil;
import com.mj.mainservice.entitys.attence.AttenceReport;

/**
 * @auther JianLinWei
 * @date 2020-11-15 19:45
 */
public interface TranslationService {
     ResultUtil getAttenceReport(AttenceReport attenceReport);
}
