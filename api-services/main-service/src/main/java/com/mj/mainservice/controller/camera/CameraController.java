package com.mj.mainservice.controller.camera;

import com.jian.common.util.ResultUtil;
import com.mj.mainservice.entitys.camrea.Camera;
import com.mj.mainservice.entitys.camrea.CameraBind;
import com.mj.mainservice.service.camera.CameraService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by MrJan on 2020/12/21
 */

@RestController
@RequestMapping("/camera")
public class CameraController {
    @Autowired
    private CameraService cameraService;




    @PostMapping("/cameraBind")
    public ResultUtil  cameraBind(@RequestBody CameraBind cameraBind){

        return cameraService.cameraBind(cameraBind);
    }

    @PostMapping("/cameraBindList")
    public ResultUtil cameraBindList(@RequestBody CameraBind cameraBind){

        return cameraService.cameraBindList(cameraBind);
    }

    @GetMapping("/delBind")
    public ResultUtil delBind(  String id){

        return cameraService.delBind(id);
    }

    @PostMapping("/listCamera")
    public ResultUtil listCamera(@RequestBody Camera camera  ,String userId){
        if (StringUtils.isEmpty(camera.getUserId()))
            camera.setUserId(userId);
        return cameraService.listCamera(camera);
    }

}
