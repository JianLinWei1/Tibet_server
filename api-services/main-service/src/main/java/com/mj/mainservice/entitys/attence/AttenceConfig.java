package com.mj.mainservice.entitys.attence;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;
import java.util.List;

/**
 * @auther JianLinWei
 * @date 2020-11-15 0:35
 */
@Data
public class AttenceConfig {

    //private  String  id ;

    @DateTimeFormat(pattern = "HH:mm:ss")
    @JsonFormat(pattern="HH:mm:ss",timezone = "GMT+8")
    private LocalTime amClockIn;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="HH:mm:ss",timezone = "GMT+8")
    private LocalTime amClockOut;
    @DateTimeFormat(pattern = "HH:mm:ss")
    @JsonFormat(pattern="HH:mm:ss",timezone = "GMT+8")
    private LocalTime pmClockIn;
    @DateTimeFormat(pattern = "HH:mm:ss")
    @JsonFormat(pattern="HH:mm:ss",timezone = "GMT+8")
    private LocalTime pmClockOut;


    private List<String>  sns;

    @Id
    private String userId;

}
