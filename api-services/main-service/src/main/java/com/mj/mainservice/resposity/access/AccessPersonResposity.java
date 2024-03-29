package com.mj.mainservice.resposity.access;



import com.mj.mainservice.entitys.access.AccessPerson;
import com.mj.mainservice.entitys.access.Doors;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by MrJan on 2020/10/20 19:28
 */

@Repository
public interface AccessPersonResposity  extends MongoRepository<AccessPerson,String> {

    List<AccessPerson>  findByAccessIdEqualsAndAdvIdEquals(String pid  ,String sn);

    List<AccessPerson>  findAllByPidEquals(String pid);

    Page<AccessPerson> findAll(Example<AccessPerson> example , Query query, Pageable pageable);

    AccessPerson  findByPidEqualsAndAdvIdEqualsAndDoorsNumContains(String pid , String aid , List<Doors> doors);

    AccessPerson  findByPidEqualsAndAdvIdEqualsAndDoorsNumLike(String pid , String aid , List<Doors> doors);

    @org.springframework.data.mongodb.repository.Query("{'pid':?0 , 'advId':?1 ,'doorsNum.id':{'$in':?2}}")
    AccessPerson  findByPidEqualsAndAdvIdEqualsAndDoorsNum(String pid , String aid , List<Integer> doors);


    int   countByPidEqualsAndAdvIdEquals(String pid , String advId);



}
