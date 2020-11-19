package com.mj.mainservice.resposity;


import com.mj.mainservice.entitys.attence.AttenceConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @auther JianLinWei
 * @date 2020-11-15 3:03
 */
public interface AttenceConfigResposity extends MongoRepository<AttenceConfig,String> {
}
