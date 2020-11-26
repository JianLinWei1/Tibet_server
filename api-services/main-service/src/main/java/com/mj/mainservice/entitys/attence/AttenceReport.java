package com.mj.mainservice.entitys.attence;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jian.common.util.LocalDateTimeConverter;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @auther JianLinWei
 * @date 2020-11-15 15:28
 */
@Data
public class AttenceReport {

    @ExcelProperty("姓名")
    private  String name;

    @ExcelProperty("人员编号")
    private String personId;


    @ExcelProperty(value = "日期范围" ,converter = LocalDateTimeConverter.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd" )
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private List<LocalDate>  times;

    private Integer con;

    @ExcelProperty("部门")
    private String department;




    @ExcelProperty(value = "考勤时间" , converter = LocalDateTimeConverter.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private List<LocalDateTime>  timeList;


    @ExcelProperty("迟到次数")
    private  Integer lateCount;

    @ExcelProperty("早退次数")
    private  Integer earlyCount;


    @ExcelProperty("旷工次数")
    private  Integer absentCount;


    @ExcelIgnore
    private String userId;


    @ExcelIgnore
    private AttenceConfig config;



}
