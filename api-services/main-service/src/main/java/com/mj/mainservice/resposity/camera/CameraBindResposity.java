package com.mj.mainservice.resposity.camera;

import com.mj.mainservice.entitys.camrea.CameraBind;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CameraBindResposity  extends MongoRepository<CameraBind ,String> {
}
