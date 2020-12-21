package com.mj.mainservice.service.camera;

import com.jian.common.util.ResultUtil;
import com.mj.mainservice.entitys.camrea.Camera;
import com.mj.mainservice.entitys.camrea.CameraBind;

/**
 * Created by MrJan on 2020/12/21
 */

public interface CameraService {

    ResultUtil  cameraBind(CameraBind cameraBind);

    ResultUtil  cameraBindList(CameraBind cameraBind);

    ResultUtil  delBind(String userId);


    ResultUtil  listCamera(Camera camera);
}
