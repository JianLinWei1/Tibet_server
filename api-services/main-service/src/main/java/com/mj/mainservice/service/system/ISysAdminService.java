package com.mj.mainservice.service.system;

import com.baomidou.mybatisplus.extension.service.IService;

import com.jian.common.util.ResultUtil;

import com.mj.mainservice.entitys.system.SysAdmin;
import com.mj.mainservice.vo.SysAdminVo;

import java.util.List;


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

    ResultUtil getAccountTree2(String userId);

    ResultUtil addUser(SysAdminVo sysAdminvo);

    ResultUtil  delUserByParentId(String parentId , String userId);

    ResultUtil getAddUserTree(String userId);

    ResultUtil getUserIdByName(String userName);

    ResultUtil getUserPermission(String userId);

    ResultUtil getUserById(String id);

    ResultUtil updateUser(SysAdminVo sysAdminVo);

    List<String>  getChildByUerIds(String userId);

    ResultUtil updatePw(String uerId , String oldpw , String newpw);

}
