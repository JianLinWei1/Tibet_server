package com.mj.mainservice.resposity;

import com.jian.common.entitys.access.Translation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by MrJan on 2020/11/5
 */

public interface TranslationResposity  extends MongoRepository<Translation , String> {


    List<Translation>  findAllByPersonIdAndSnInAndTimeBetween(String personId , List<String> sn , Date date1 , Date date2);
}
