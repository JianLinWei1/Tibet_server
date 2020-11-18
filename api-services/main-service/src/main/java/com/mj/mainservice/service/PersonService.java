package com.mj.mainservice.service;

import com.jian.common.entitys.PersonInfo;
import com.jian.common.util.ResultUtil;

import java.util.List;

public interface PersonService {

    ResultUtil  insertPerson(PersonInfo info );


    ResultUtil  queryPersonsList(PersonInfo info  ) ;

    ResultUtil  editPerson(PersonInfo info);


    ResultUtil delPerson(List<String>  ids);


    List<PersonInfo>   quryPersonListNoPage(PersonInfo personInfo);


}
