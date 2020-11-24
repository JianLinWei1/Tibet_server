package com.mj.mainservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.jian.common.entity.SysConfig;
import com.jian.common.util.ResultUtil;
import com.jian.common.util.SysConfigUtil;
import com.mj.mainservice.entitys.system.SysLog;
import com.mj.mainservice.mapper.SysLogMapper;
import com.mj.mainservice.service.system.ISysLogService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by MrJan on 2020/11/3
 */

@Log4j2
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements ISysLogService {
       @Resource
       private SysLogMapper sysLogMapper;


    @Override
    public ResultUtil addSysConfig(SysConfig sysConfig) {
        try {
            SysConfigUtil.getIns().setPro(sysConfig);
            return ResultUtil.ok();
        }catch (Exception e){
            log.error(e);
            return  new ResultUtil(-1,e.getMessage());
        }
    }

    @Override
    public ResultUtil getSysConfig() {
        try {
            SysConfig sysConfig = SysConfigUtil.getIns().getSysConfig();
            return ResultUtil.ok(sysConfig);

        }catch (Exception e){
            log.error(e);
            return new ResultUtil(-1 ,e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResultUtil addSysLog(SysLog sysLog) {
        try {

          sysLogMapper.insert(sysLog);
        }catch (Exception e){
            log.error(e);
            return new ResultUtil(-1, e.getMessage());
        }
        return null;
    }

    @Override
    public ResultUtil getSysLogs(SysLog sysLog) {
        try {
            QueryWrapper<SysLog>  queryWrapper = new QueryWrapper<>();
           if(StringUtils.isNotEmpty(sysLog.getUserName()))
               queryWrapper.lambda().like(SysLog::getUserName,sysLog.getUserName());
           queryWrapper.lambda().select(SysLog.class,i -> !i.getColumn().equals("page")
                   && !i.getColumn().equals("limit"));
           queryWrapper.lambda().orderByDesc(SysLog::getTime);
           queryWrapper.lambda().eq(SysLog::getUserId , sysLog.getUserId());
            IPage<SysLog> page = new Page<>(sysLog.getPage() , sysLog.getLimit());
           sysLogMapper.selectPage(page,queryWrapper);
           ResultUtil resultUtil = new ResultUtil();
           resultUtil.setCode(0);
           resultUtil.setData(page.getRecords());
           resultUtil.setCount(page.getTotal());
           return  resultUtil;
        }catch (Exception e){
            log.error(e);
            return  new ResultUtil(-1 ,e.getMessage());
        }
    }

    @Override
    public ResultUtil delSyslog(List<String> ids) {
        try {
         ids.stream().forEach(id ->{
             sysLogMapper.deleteById(id);
         });
         return ResultUtil.ok();
        }catch (Exception e){
            log.error(e);
            return new ResultUtil(-1, e.getMessage());
        }
    }
}
