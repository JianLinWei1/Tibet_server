package com.mj.mainservice.service;

import com.jian.common.entitys.attence.AttenceReport;
import com.jian.common.util.ResultUtil;

/**
 * @auther JianLinWei
 * @date 2020-11-15 19:45
 */
public interface TranslationService {
     ResultUtil getAttenceReport(AttenceReport attenceReport);
}
