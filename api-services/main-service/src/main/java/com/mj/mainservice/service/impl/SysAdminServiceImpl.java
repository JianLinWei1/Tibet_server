package com.mj.mainservice.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.jian.common.entity.AntdTree;
import com.jian.common.entity.ElTree;
import com.jian.common.util.JwtUtil;
import com.jian.common.util.MD5Util;
import com.jian.common.util.ResultUtil;
import com.mj.mainservice.entitys.system.Permission;
import com.mj.mainservice.entitys.system.SysAdmin;
import com.mj.mainservice.entitys.system.SysAdminMenu;
import com.mj.mainservice.entitys.system.SysMenu;
import com.mj.mainservice.mapper.SysAdminMapper;
import com.mj.mainservice.mapper.SysAdminMenuMapper;
import com.mj.mainservice.mapper.SysMenuMapper;
import com.mj.mainservice.resposity.access.AccessPersonResposity;
import com.mj.mainservice.resposity.person.PersonRepository;
import com.mj.mainservice.service.system.ISysAdminService;
import com.mj.mainservice.vo.AntRouter;
import com.mj.mainservice.vo.SysAdminVo;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author JianLinWei
 * @since 2020-09-18
 */
@Service
@Log4j2
public class SysAdminServiceImpl extends ServiceImpl<SysAdminMapper, SysAdmin> implements ISysAdminService {

    @Resource
    private SysAdminMapper sysAdminMapper;
    @Resource
    private SysAdminMenuMapper sysAdminMenuMapper;
    @Resource
    private SysMenuMapper sysMenuMapper;
    @Resource
    private PersonRepository personRepository;


    private final MD5Util md5Util = new MD5Util();

    @Override
    public ResultUtil login(SysAdmin sysAdmin) {

        try {
            QueryWrapper<SysAdmin> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(SysAdmin::getUserName, sysAdmin.getUserName());

            List<SysAdmin> sysAdmins = sysAdminMapper.selectList(queryWrapper);
            if (sysAdmins.size() > 0) {

                SysAdmin sysAdmin1 = sysAdmins.get(0);
                String pwd = md5Util.md5(sysAdmin.getPasswd(), "utf-8");
                if (StringUtils.equals(pwd, sysAdmin1.getPasswd())) {
                    String token = JwtUtil.getInstance().geneJsonWebToken(sysAdmin1.getUserName(), sysAdmin1.getId()
                            , getChildByUerIds(sysAdmin1.getId()));
                    JSONObject json = new JSONObject();
                    json.put("token", token);
                    ResultUtil resultUtil = new ResultUtil();
                    resultUtil.setCode(0);
                    resultUtil.setData(json);
                    resultUtil.setMsg("登录成功");
                    return resultUtil;
                } else {
                    return new ResultUtil(-1, "密码错误");
                }
            } else {
                return new ResultUtil(-1, "用户名不存在");
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return new ResultUtil(-1, "异常");
    }


    @Override
    public ResultUtil getRouter(String userId) {
        try {
//根据用户ID 生成Router
            QueryWrapper<SysAdminMenu> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(SysAdminMenu::getAdminId, userId);
            queryWrapper.lambda().orderByAsc(SysAdminMenu::getMenuId);
            List<SysAdminMenu> sysAdminMenus = sysAdminMenuMapper.selectList(queryWrapper);
            List<String> menuIds = sysAdminMenus.stream().map(SysAdminMenu::getMenuId).collect(Collectors.toList());

            List<SysMenu> sysMenus = sysMenuMapper.selectBatchIds(menuIds);

            List<AntRouter> antRouters = getRouters(sysMenus, "0");
            AntRouter antRouter = new AntRouter();
            antRouter.setRouter("root");
            antRouter.setChildren(antRouters);
            List<AntRouter> antRouters1 = new ArrayList<>();
            antRouters1.add(antRouter);

            return ResultUtil.ok(antRouters1);
        } catch (Exception e) {
            log.error(e);
            return new ResultUtil(-1, e.getMessage());
        }

    }


    public List<AntRouter> getRouters(List<SysMenu> sysMenus, String pid) {
        List<AntRouter> antRouters = new ArrayList<>();
        sysMenus.stream().forEach(sysMenu -> {
            if (StringUtils.equals(sysMenu.getParentId(), pid)) {
                AntRouter antRouter = new AntRouter();
                antRouter.setRouter(sysMenu.getRouter());
                antRouter.setName(sysMenu.getName());
                antRouter.setIcon(sysMenu.getIcon());
                if (StringUtils.isNotEmpty(sysMenu.getAuthority()))
                    antRouter.setAuthority(sysMenu.getAuthority());
                antRouter.setChildren(getRouters(sysMenus, sysMenu.getId()));
                antRouters.add(antRouter);
            }
        });
        return antRouters;
    }

    @Override
    public ResultUtil getAccountTree(String userId) {
        SysAdmin sysAdmin = sysAdminMapper.selectById(userId);
        List<ElTree> elTrees = new ArrayList<>();
        ElTree elTree = new ElTree();
        elTree.setId(sysAdmin.getId());
        elTree.setLabel(sysAdmin.getNickName() + "(账号：" + sysAdmin.getUserName() + ")");
        elTree.setChildren(getChildrens(sysAdmin));
        elTrees.add(elTree);

        return ResultUtil.ok(elTrees);
    }


    public List<ElTree> getChildrens(SysAdmin sysAdmin) {
        QueryWrapper<SysAdmin> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysAdmin::getParentId, sysAdmin.getId());
        List<SysAdmin> sysAdmins = sysAdminMapper.selectList(queryWrapper);
        List<ElTree> elTrees = new ArrayList<>();
        sysAdmins.stream().forEach(sysAdmin1 -> {
            ElTree elTree = new ElTree();
            elTree.setId(sysAdmin1.getId());
            elTree.setLabel(sysAdmin1.getNickName() + "(账号：" + sysAdmin1.getUserName() + ")");
            elTree.setChildren(getChildrens(sysAdmin1));
            elTrees.add(elTree);
        });

        return elTrees;
    }


    @Override
    public ResultUtil getAccountTree2(String userId) {
        try {

            SysAdmin sysAdmin = sysAdminMapper.selectById(userId);
            List<AntdTree> elTrees = new ArrayList<>();
            AntdTree elTree = new AntdTree();
            elTree.setKey(sysAdmin.getId());
            elTree.setValue(sysAdmin.getUserName());
            elTree.setTitle(sysAdmin.getNickName());
            elTree.setChildren(getChildrens2(sysAdmin));
            elTrees.add(elTree);
            return ResultUtil.ok(elTrees);
        } catch (Exception e) {
            log.error(e);
            return new ResultUtil(-1, e.getMessage());
        }
    }

    public List<AntdTree> getChildrens2(SysAdmin sysAdmin) {
        QueryWrapper<SysAdmin> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysAdmin::getParentId, sysAdmin.getId());
        List<SysAdmin> sysAdmins = sysAdminMapper.selectList(queryWrapper);
        List<AntdTree> elTrees = new ArrayList<>();
        sysAdmins.stream().forEach(sysAdmin1 -> {
            AntdTree elTree = new AntdTree();
            elTree.setKey(sysAdmin1.getId());
            elTree.setValue(sysAdmin1.getUserName());
            elTree.setTitle(sysAdmin1.getNickName());
            elTree.setChildren(getChildrens2(sysAdmin1));
            elTrees.add(elTree);
        });

        return elTrees;
    }

    @Override
    @Transactional
    public ResultUtil addUser(SysAdminVo sysAdminVo) {
        try {
            QueryWrapper<SysAdmin> sysAdminQueryWrapper = new QueryWrapper<>();
            sysAdminQueryWrapper.lambda().eq(SysAdmin::getUserName, sysAdminVo.getUserName());
            int length = sysAdminMapper.selectCount(sysAdminQueryWrapper);
            if (length >= 1)
                return new ResultUtil(-2, "该账号已存在");
            sysAdminVo.setPasswd(md5Util.md5(sysAdminVo.getPasswd(), "utf-8"));
            SysAdmin sysAdmin = new SysAdmin();
            BeanUtils.copyProperties(sysAdminVo, sysAdmin);
            sysAdminMapper.insert(sysAdmin);
            if (sysAdmin.getId() != null && sysAdminVo.getRouterIds() != null) {
                sysAdminVo.getRouterIds().stream().forEach(sv -> {
                    SysAdminMenu sysAdminMenu = new SysAdminMenu();
                    sysAdminMenu.setAdminId(sysAdmin.getId());
                    sysAdminMenu.setMenuId(sv);
                    sysAdminMenuMapper.insert(sysAdminMenu);
                });
            }

            return ResultUtil.ok();
        } catch (Exception e) {

            log.error(e);
            return new ResultUtil(-1, "error");
        }
    }


    @Override
    @Transactional
    public ResultUtil delUserByParentId(String parentId, String userId) {
        try {
            if (StringUtils.equals(parentId, userId))
                return new ResultUtil(-1, "无法删除");
            if (personRepository.existsByUserId(parentId))
                return new ResultUtil(-1, "当前存在人员,删除失败");

            QueryWrapper<SysAdmin> sysAdminQueryWrapper = new QueryWrapper<>();
            sysAdminQueryWrapper.lambda().eq(SysAdmin::getParentId, parentId);
            List<SysAdmin> sysAdmins = sysAdminMapper.selectList(sysAdminQueryWrapper);
            //List<String>  ids  = new ArrayList<>();

            for (SysAdmin s : sysAdmins) {
                // ids.add(sa.getId());
                if (personRepository.existsByUserId(s.getId()))
                    return new ResultUtil(-1, s.getNickName() + "删除子账号失败,当前存在人员,删除失败");
                sysAdminMapper.deleteById(s.getId());
            }
            ;
            //ids.add(parentId);
            sysAdminMapper.deleteById(parentId);
            return ResultUtil.ok();


        } catch (Exception e) {
            log.error(e);
            return new ResultUtil(-1, "删除失败");
        }
    }

    @Override
    public ResultUtil getAddUserTree(String userId) {
        try {
            QueryWrapper<SysAdminMenu> sysAdminMenuQueryWrapper = new QueryWrapper<>();
            sysAdminMenuQueryWrapper.lambda().eq(SysAdminMenu::getAdminId, userId);
            List<SysAdminMenu> sysAdminMenus = sysAdminMenuMapper.selectList(sysAdminMenuQueryWrapper);
            List<String> menuIds = sysAdminMenus.stream().map(SysAdminMenu::getMenuId).collect(Collectors.toList());

            List<SysMenu> sysMenus = sysMenuMapper.selectBatchIds(menuIds);
            List<AntdTree> antdTrees = getUserTree(sysMenus, "0");

            return new ResultUtil(0, antdTrees, "");
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResultUtil(-1, "获取失败");
        }
    }

    public List<AntdTree> getUserTree(List<SysMenu> sysMenus, String pid) {
        List<AntdTree> antdTrees = new ArrayList<>();
        sysMenus.stream().forEach(sysMenu -> {
            if (StringUtils.equals(sysMenu.getParentId(), pid)) {
                AntdTree antdTree = new AntdTree();
                antdTree.setTitle(sysMenu.getName());
                antdTree.setKey(sysMenu.getId());
                antdTree.setValue(sysMenu.getId());
                antdTree.setChildren(getUserTree(sysMenus, sysMenu.getId()));
                antdTrees.add(antdTree);
            }
        });
        return antdTrees;
    }


    @Override
    public ResultUtil getUserIdByName(String userName) {
        try {
            QueryWrapper<SysAdmin> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().like(SysAdmin::getUserName, userName);
            List<SysAdmin> sysAdmin = sysAdminMapper.selectList(queryWrapper);
            if (sysAdmin != null)
                return new ResultUtil(0, sysAdmin, "");
            return null;

        } catch (Exception e) {
            log.error(e);
            return new ResultUtil(-1, e.getMessage());
        }
    }

    @Override
    public ResultUtil getUserPermission(String userId) {
        try {
            QueryWrapper<SysAdminMenu> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(SysAdminMenu::getAdminId, userId);
            queryWrapper.lambda().orderByAsc(SysAdminMenu::getMenuId);
            List<SysAdminMenu> sysAdminMenus = sysAdminMenuMapper.selectList(queryWrapper);
            List<String> menuIds = sysAdminMenus.stream().map(SysAdminMenu::getMenuId).collect(Collectors.toList());
            log.info("获取的菜单ID:{}   ::: {}  ::: uerID：：{}", JSON.toJSONString(menuIds),
                    JSON.toJSONString(sysAdminMenus), userId);
            if (menuIds == null || menuIds.size() <= 0)
                return new ResultUtil(-1, "当前用户未拥有菜单,登录失败");
            QueryWrapper<SysMenu> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.lambda().in(SysMenu::getId, menuIds);
            queryWrapper1.lambda().isNotNull(SysMenu::getPermission);


            List<SysMenu> sysMenus = sysMenuMapper.selectList(queryWrapper1);
            List<Permission> permissions = getPermission(sysMenus);

            return ResultUtil.ok(permissions);
        } catch (Exception e) {
            log.error(e);
            return new ResultUtil(-1, e.getMessage());
        }
    }

    public List<Permission> getPermission(List<SysMenu> sysMenus) {
        List<Permission> permissions = new ArrayList<>();
        sysMenus.stream().forEach((sysMenu -> {
            List<Permission> permissions1 = permissions.stream().filter(permission -> StringUtils.equals(permission.getId(), sysMenu.getParentId())).collect(Collectors.toList());
            if (permissions1.size() > 0)
                return;
            Permission permission = new Permission();
            permission.setId(sysMenu.getParentId());
            List<String> strings = new ArrayList<>();
            List<SysMenu> sysMenus1 = sysMenus.stream().filter(sysMenu1 -> StringUtils.equals(sysMenu1.getParentId(), sysMenu.getParentId())).collect(Collectors.toList());
            sysMenus1.stream().forEach(sysMenu1 -> {
                strings.add(sysMenu1.getPermission());
            });

            permission.setOperation(strings);
            permissions.add(permission);
        }));

        return permissions;
    }


    @Override
    public ResultUtil getUserById(String id) {
        try {
            SysAdmin sysAdmin = sysAdminMapper.selectById(id);
            SysAdminVo sysAdminVo = new SysAdminVo();
            sysAdmin.setPasswd(null);
            BeanUtils.copyProperties(sysAdmin, sysAdminVo);
            QueryWrapper<SysAdminMenu> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(SysAdminMenu::getAdminId, id);
            List<SysAdminMenu> menuList = sysAdminMenuMapper.selectList(queryWrapper);
            List<String> menuIds = new ArrayList<>();
            for (SysAdminMenu sm : menuList) {
                menuIds.add(sm.getMenuId());

            }
           /* 适配Tree UI 去掉父ID 会造成右边菜单渲染失败
           CopyOnWriteArrayList<String> cowList = new CopyOnWriteArrayList<String>(menuIds);
            for (String s : cowList) {
                SysMenu sysMenu = sysMenuMapper.selectById(s);
                if (menuIds.contains(sysMenu.getParentId())) {
                    cowList.remove(sysMenu.getParentId());
                }
            }*/
            sysAdminVo.setRouterIds(menuIds);
            return ResultUtil.ok(sysAdminVo);
        } catch (Exception e) {
            log.error(e);
            return new ResultUtil(-1, e.getMessage());
        }

    }


    @Override
    @Transactional
    public ResultUtil updateUser(SysAdminVo sysAdminVo) {
        try {
            if (StringUtils.equals(sysAdminVo.getId(), "1"))
                return new ResultUtil(-1, "无法被编辑");
            if (StringUtils.equals(sysAdminVo.getId(), sysAdminVo.getUserId()))
                return new ResultUtil(-1, "无法被编辑");
            SysAdmin sysAdmin = new SysAdmin();
            BeanUtils.copyProperties(sysAdminVo, sysAdmin);
            sysAdmin.setPasswd(md5Util.md5(sysAdmin.getPasswd(), "utf-8"));
            sysAdminMapper.updateById(sysAdmin);
            QueryWrapper<SysAdminMenu> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(SysAdminMenu::getAdminId, sysAdminVo.getId());
            sysAdminMenuMapper.delete(queryWrapper);

            sysAdminVo.getRouterIds().stream().forEach(r -> {
                SysAdminMenu sysAdminMenu = new SysAdminMenu();
                sysAdminMenu.setAdminId(sysAdminVo.getId());
                sysAdminMenu.setMenuId(r);
                sysAdminMenuMapper.insert(sysAdminMenu);
            });

            return ResultUtil.ok();

        } catch (Exception e) {
            log.error(e);
            return new ResultUtil(-1, e.getMessage());
        }
    }


    @Override
    public List<String> getChildByUerIds(String userId) {
        try {
            return getChildIds(userId);

        } catch (Exception e) {
            log.error(e);
            return null;
        }

    }


    public List<String> getChildIds(String userId) {
        QueryWrapper<SysAdmin> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysAdmin::getParentId, userId);
        List<SysAdmin> sysAdmins = sysAdminMapper.selectList(queryWrapper);
        List<String> childIds = new ArrayList<>();
        sysAdmins.stream().forEach(sysAdmin -> {
            childIds.add(sysAdmin.getId());
            List<String> childIds2 = getChildIds(sysAdmin.getId());
            childIds.addAll(childIds2);
        });
        return childIds;
    }

    @Override
    @Transactional
    public ResultUtil updatePw(String uerId, String oldpw, String newpw) {
        try {
            SysAdmin sysAdmin = sysAdminMapper.selectById(uerId);
            String oldPwMd5 = md5Util.md5(oldpw, "utf-8");
            if (!StringUtils.equals(sysAdmin.getPasswd(), oldPwMd5))
                return new ResultUtil(-1, "旧密码不符");
            String newPwMd5 = md5Util.md5(newpw, "utf-8");
            sysAdmin.setPasswd(newPwMd5);
            sysAdminMapper.updateById(sysAdmin);
            return ResultUtil.ok();
        } catch (Exception e) {
            log.error(e);
            return new ResultUtil(-1, e.getMessage());
        }
    }
}
