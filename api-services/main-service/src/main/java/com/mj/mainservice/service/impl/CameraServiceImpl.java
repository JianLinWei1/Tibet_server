package com.mj.mainservice.service.impl;

import com.baomidou.mybatisplus.extension.api.R;
import com.jian.common.util.ResultUtil;
import com.jian.common.util.SysConfigUtil;
import com.mj.mainservice.entitys.camrea.Camera;
import com.mj.mainservice.entitys.camrea.CameraBind;
import com.mj.mainservice.entitys.system.SysAdmin;
import com.mj.mainservice.mapper.SysAdminMapper;
import com.mj.mainservice.resposity.camera.CameraBindResposity;
import com.mj.mainservice.service.camera.CameraService;
import com.mj.mainservice.util.camera.util.UniUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * Created by MrJan on 2020/12/21
 */

@Service
@Log4j2
public class CameraServiceImpl  implements CameraService {
    @Resource
    private CameraBindResposity cameraBindResposity;
    @Resource
    private SysAdminMapper sysAdminMapper;
    @Resource
    private UniUtil uniUtil;

    @Override
    public ResultUtil cameraBind(CameraBind cameraBind) {
        try {
            String token =  uniUtil.login(cameraBind.getAdmin() , cameraBind.getPw());
            if(StringUtils.isEmpty(token))
                return new ResultUtil(-2 ,"验证用户名密码失败");
            cameraBind.setServerIp(SysConfigUtil.getIns().getProUniServer());
            cameraBindResposity.save(cameraBind);
            return ResultUtil.ok();
        }catch (Exception e){
          log.error(e);
          return  new ResultUtil(-1, e.getMessage());
        }
    }

    @Override
    public ResultUtil cameraBindList(CameraBind cameraBind) {
        try {
            ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("page" ,"limit");
            Example<CameraBind>  example = Example.of(cameraBind ,matcher);
            Page<CameraBind>  page =  cameraBindResposity.findAll(example, PageRequest.of(cameraBind.getPage() -1 , cameraBind.getLimit()));
            page.getContent().stream().forEach(c->{
                SysAdmin sysAdmin = sysAdminMapper.selectById(c.getUserId());
                if(sysAdmin == null)
                    return;
                c.setName(sysAdmin.getNickName());
            });
            ResultUtil resultUtil = new ResultUtil();
            resultUtil.setCode(0);
            resultUtil.setCount(page.getTotalElements());
            resultUtil.setData(page.getContent());
            return  resultUtil;
        }catch (Exception e){
            log.error(e);
            return  new ResultUtil(-1 ,e.getMessage());
        }
    }

    @Override
    public ResultUtil delBind(String userId) {
        try {
          cameraBindResposity.deleteById(userId);
          return  ResultUtil.ok();
        }catch (Exception e){
            log.error(e);
            return  new ResultUtil(-1 ,e.getMessage());
        }
    }

    @Override
    public ResultUtil listCamera(Camera camera) {
        try {
           Optional<CameraBind>  optional = cameraBindResposity.findById(camera.getUserId());
           if(!optional.isPresent())
               return ResultUtil.ok();
            String token  = uniUtil.login(optional.get().getAdmin() ,optional.get().getPw());
            if(StringUtils.isEmpty(token))
                return new ResultUtil(-2,"登录平台token失败");
            ResultUtil resultUtil = uniUtil.getResInfo(token ,camera.getPage()-1 ,camera.getLimit() ,camera.getUserId());


            return resultUtil;
        }catch (Exception e){
            log.error(e);
            return  new ResultUtil(-1 ,e.getMessage());
        }
    }
}
