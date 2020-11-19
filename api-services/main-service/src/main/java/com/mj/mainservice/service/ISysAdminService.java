package com.mj.mainservice.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.jian.common.util.ResultUtil;

import com.mj.mainservice.entitys.SysAdmin;
import com.mj.mainservice.vo.SysAdminVo;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author JianLinWei
 * @since 2020-09-18
 */
public interface ISysAdminService extends IService<SysAdmin> {

    ResultUtil login(SysAdmin sysAdmin);

    ResultUtil getRouter(String userId);

    ResultUtil getAccountTree(String userId);

    ResultUtil addUser(SysAdminVo sysAdminvo);

    ResultUtil  delUserByParentId(String parentId);

    ResultUtil getAddUserTree(String userId);

    ResultUtil getUserIdByName(String userName);

    ResultUtil getUserPermission(String userId);

    ResultUtil getUserById(String id);

    ResultUtil updateUser(SysAdminVo sysAdminVo);

}
