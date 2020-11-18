package com.mj.mainservice.service;

import com.jian.common.entitys.attence.AttenceConfig;
import com.jian.common.util.ResultUtil;

/**
 * @auther JianLinWei
 * @date 2020-11-15 0:40
 */
public interface AttenceConfigService {

    ResultUtil  addConfig(AttenceConfig attenceConfig);

    ResultUtil getConfig(String userId);
}
