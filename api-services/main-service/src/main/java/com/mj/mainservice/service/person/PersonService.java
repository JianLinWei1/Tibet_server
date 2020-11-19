package com.mj.mainservice.service.person;


import com.jian.common.util.ResultUtil;
import com.mj.mainservice.entitys.person.PersonInfo;

import java.util.List;

public interface PersonService {

    ResultUtil  insertPerson(PersonInfo info );


    ResultUtil  queryPersonsList(PersonInfo info  ) ;

    ResultUtil  editPerson(PersonInfo info);


    ResultUtil delPerson(List<String>  ids);


    List<PersonInfo>   quryPersonListNoPage(PersonInfo personInfo);

    PersonInfo getPersonById(String  id);


}