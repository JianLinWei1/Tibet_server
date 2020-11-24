package com.mj.mainservice.service.impl;

import com.jian.common.util.ResultUtil;
import com.mj.mainservice.entitys.person.Department;
import com.mj.mainservice.entitys.system.SysAdmin;
import com.mj.mainservice.mapper.SysAdminMapper;
import com.mj.mainservice.resposity.department.DepartmentResposity;
import com.mj.mainservice.service.person.DepartmentService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @auther JianLinWei
 * @date 2020-11-21 19:54
 */
@Service
@Log4j2
public class DeparmentServiceImpl implements DepartmentService {
    @Resource
    private DepartmentResposity departmentResposity;
    @Resource
    private SysAdminMapper sysAdminMapper;

    @Override
    @Transactional
    public ResultUtil save(Department department) {
        try {
            SysAdmin sysAdmin = sysAdminMapper.selectById(department.getUserId());
            department.setNickName(sysAdmin.getNickName());
            departmentResposity.save(department);
            return ResultUtil.ok();
        } catch (Exception e) {
            log.error(e);
            return new ResultUtil(-1, e.getMessage());
        }
    }

    @Override
    public ResultUtil getList(Department department, List<String> childs) {
        try {
            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains())
                    .withMatcher("nickName", ExampleMatcher.GenericPropertyMatchers.contains())
                    .withIgnorePaths("page", "limit", "accessPw","userId")
                    .withNullHandler(ExampleMatcher.NullHandler.IGNORE);

            Example<Department> example = Example.of(department ,matcher);
            childs.add(department.getUserId());
            Query query = new Query();
            query.addCriteria(Criteria.where("userId").in(childs));

            Page<Department>  page = departmentResposity.findAll(example, query, PageRequest.of(department.getPage(), department.getLimit()));
            ResultUtil resultUtil = new ResultUtil();
            resultUtil.setData(page.getContent());
            resultUtil.setCount(page.getTotalElements());
            resultUtil.setCode(0);
            return  resultUtil;
        } catch (Exception e) {
            log.error(e);
            return new ResultUtil(-1, e.getMessage());
        }

    }


    @Override
    public ResultUtil del(List<String> ids) {
        try {
           ids.stream().forEach(s -> {
               departmentResposity.deleteById(s);
           });
           return  ResultUtil.ok();
        }catch (Exception e){
            log.error(e);
            return  new ResultUtil(-1, e.getMessage());
        }
    }
}
