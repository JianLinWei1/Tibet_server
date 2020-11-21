package com.mj.mainservice.resposity.person;


import com.mj.mainservice.entitys.person.PersonInfo;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.stereotype.Repository;

/**
 * Created by MrJan on 2020/10/7 21:44
 */

@Repository
public interface PersonRepository extends MongoRepository<PersonInfo,String> {

    PersonInfo findByAccessIdEquals(String accessId);

    Page<PersonInfo>  findAll(Example<PersonInfo> example , Query query, Pageable pageable);
}
