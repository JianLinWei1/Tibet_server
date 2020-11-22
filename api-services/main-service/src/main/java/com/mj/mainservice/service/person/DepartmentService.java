package com.mj.mainservice.service.person;

import com.jian.common.util.ResultUtil;
import com.mj.mainservice.entitys.person.Department;

import java.util.List;

/**
 * @auther JianLinWei
 * @date 2020-11-21 19:53
 */
public interface DepartmentService {
    ResultUtil save(Department department);

    ResultUtil getList(Department department , List<String> childs);

    ResultUtil del(List<String> ids);
}
