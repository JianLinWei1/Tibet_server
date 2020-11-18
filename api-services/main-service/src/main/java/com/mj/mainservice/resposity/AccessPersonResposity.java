package com.mj.mainservice.resposity;

import com.jian.common.entitys.AccessPerson;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by MrJan on 2020/10/20 19:28
 */

@Repository
public interface AccessPersonResposity  extends MongoRepository<AccessPerson,String> {

    AccessPerson findByAccessIdEquals(String pid);
}
