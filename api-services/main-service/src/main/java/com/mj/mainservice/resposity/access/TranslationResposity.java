package com.mj.mainservice.resposity.access;


import com.mj.mainservice.entitys.access.Translation;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by MrJan on 2020/11/5
 */

@Repository
public interface TranslationResposity  extends MongoRepository<Translation, String> {


    List<Translation>  findAllByPersonIdAndSnInAndTimeBetween(String personId , List<String> sn , Date date1 , Date date2);

    Page<Translation>  findAll(Example<Translation> example , Query query, Pageable pageable);

    Page<Translation>  findAllByUserIdIn( List<String> childs,Example<Translation> example , Pageable pageable);
}
