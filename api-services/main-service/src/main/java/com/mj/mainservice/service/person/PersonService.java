package com.mj.mainservice.service.person;


import com.jian.common.util.ResultUtil;
import com.mj.mainservice.entitys.person.PersonInfo;
import com.mj.mainservice.vo.person.PersonInfoVo;

import java.util.List;

public interface PersonService {

    ResultUtil  insertPerson(PersonInfo info );


    ResultUtil  queryPersonsList(PersonInfo info  , List<String> childs ) ;

    ResultUtil  editPerson(PersonInfoVo info);


    ResultUtil delPerson(List<String>  ids);


    List<PersonInfo>   quryPersonListNoPage(PersonInfo personInfo );

    PersonInfo getPersonById(String  id);

    ResultUtil exportPerson(List<PersonInfoVo>  personInfoVo);


}
