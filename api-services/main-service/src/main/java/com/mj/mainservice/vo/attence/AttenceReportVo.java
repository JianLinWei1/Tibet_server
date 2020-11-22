package com.mj.mainservice.vo.attence;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jian.common.util.LocalDateTimeConverter;
import com.mj.mainservice.entitys.attence.AttenceConfig;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @auther JianLinWei
 * @date 2020-11-22 16:00
 */
@Data
public class AttenceReportVo {

    @ExcelProperty("姓名")
    private  String name;

    @ExcelProperty("人员编号")
    private String personId;




    @ExcelIgnore
    @DateTimeFormat(pattern = "yyyy-MM-dd" )
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private List<LocalDate> times;

    @ExcelIgnore
    private Integer con;




    @ExcelIgnore
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private List<LocalDateTime>  timeList;


    @ExcelProperty("迟到次数")
    private  Integer lateCount;

    @ExcelProperty("早退次数")
    private  Integer earlyCount;


    @ExcelIgnore
    private String userId;


    @ExcelIgnore
    private AttenceConfig config;

    @ExcelProperty(value = "日期范围" )
    private String times_str;

    @ExcelProperty(value = "考勤时间" )
    private String timeList_str;



}
