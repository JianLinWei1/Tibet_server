package com.mj.mainservice.service.impl;



import com.jian.common.util.ResultUtil;
import com.mj.mainservice.entitys.attence.AttenceConfig;
import com.mj.mainservice.resposity.attence.AttenceConfigResposity;
import com.mj.mainservice.service.attence.AttenceConfigService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @auther JianLinWei
 * @date 2020-11-15 0:40
 */
@Service
@Log4j2
public class AttenceConfigServiceImpl  implements AttenceConfigService {
    @Resource
    private AttenceConfigResposity configResposity;

    @Override
    public ResultUtil addConfig(AttenceConfig attenceConfig) {
        try {
       configResposity.save(attenceConfig);
      return ResultUtil.ok();
        }catch (Exception e){
            log.error(e);
            return  new ResultUtil(-1, e.getMessage());
        }

    }


    @Override
    public ResultUtil getConfig(String userId) {
        try {
            AttenceConfig config = configResposity.findById(userId).get();
            return new ResultUtil(0, config , "");
        }catch (Exception e){
            log.error(e);
            return  new ResultUtil(-1, e.getMessage());
        }
    }
}
