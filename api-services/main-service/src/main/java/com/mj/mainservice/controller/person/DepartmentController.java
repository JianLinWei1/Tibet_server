package com.mj.mainservice.controller.person;

import com.jian.common.util.ResultUtil;
import com.mj.mainservice.entitys.person.Department;
import com.mj.mainservice.service.person.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @auther JianLinWei
 * @date 2020-11-21 19:52
 */
@RestController
@RequestMapping("/department")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    @PostMapping("/insert")
    public ResultUtil insert(@RequestBody Department department ,String userId ){
        department.setUserId(userId);
        return departmentService.save(department);
    }

    @PostMapping("/getList")
    public ResultUtil getList(@RequestBody Department department , String userId , @RequestParam("childs")List<String> childs){
        department.setUserId(userId);
        return  departmentService.getList(department, childs);
    }

    @PostMapping("/del")
    public ResultUtil del(@RequestBody List<String> ids){

        return  departmentService.del(ids);
    }

}
