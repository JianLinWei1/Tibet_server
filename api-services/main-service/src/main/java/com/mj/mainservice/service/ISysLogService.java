package com.mj.mainservice.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.jian.common.entity.SysConfig;
import com.jian.common.util.ResultUtil;
import com.mj.mainservice.entitys.SysLog;

import java.util.List;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author JianLinWei
 * @since 2020-11-03
 */
public interface ISysLogService extends IService<SysLog> {

    ResultUtil  addSysConfig(SysConfig  sysConfig);


    ResultUtil getSysConfig();


    ResultUtil  addSysLog(SysLog sysLog);

    ResultUtil getSysLogs(SysLog sysLog);

    ResultUtil delSyslog(List<String> ids);

}
