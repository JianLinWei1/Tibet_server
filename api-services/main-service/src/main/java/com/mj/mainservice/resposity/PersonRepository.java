package com.mj.mainservice.resposity;


import com.mj.mainservice.entitys.PersonInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by MrJan on 2020/10/7 21:44
 */

@Repository
public interface PersonRepository extends MongoRepository<PersonInfo,String> {

    PersonInfo findByAccessIdEquals(String accessId);
}
