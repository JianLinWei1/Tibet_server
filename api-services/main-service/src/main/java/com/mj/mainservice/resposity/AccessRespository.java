package com.mj.mainservice.resposity;

import com.jian.common.entitys.DeviceInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by MrJan on 2020/10/19 16:19
 */

@Repository
public interface AccessRespository  extends MongoRepository<DeviceInfo, String> {
}
