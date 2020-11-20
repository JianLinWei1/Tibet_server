package com.mj.mainservice.resposity.access;


import com.mj.mainservice.entitys.access.AccessPerson;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by MrJan on 2020/10/20 19:28
 */

@Repository
public interface AccessPersonResposity  extends MongoRepository<AccessPerson,String> {

    AccessPerson findByAccessIdEquals(String pid);

    List<AccessPerson>  findAllByPidEquals(String pid);
}
