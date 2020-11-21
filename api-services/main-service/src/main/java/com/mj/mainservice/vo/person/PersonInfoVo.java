package com.mj.mainservice.vo.person;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * Created by MrJan on 2020/11/20
 */

@Data
public class PersonInfoVo {

    private  String  id ;

    private String  name ;

    private String  idCard;

    private String  accessId;

    private int  accessPw;

    private String carId;

    private String phone ;

    private  String photo ;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime starTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime invalidTime ;


    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime createTime;

    private String  content;

    private  byte[]  feature;

    private  Integer role;

    private  String userId;

    private  String oid;
}
