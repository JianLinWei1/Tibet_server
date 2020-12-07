package com.mj.mainservice.entitys.access;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jian.common.util.LocalDateTimeConverter;
import com.jian.common.util.PageHelper;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * Created by MrJan on 2020/11/5
 */

@Data
public class Translation  extends PageHelper {
    @ExcelProperty("ID")
    private String id;
    @ExcelProperty("门禁卡号")
    private String  icCard;
    @ExcelProperty("人员编号")
    private String personId;
    @ExcelProperty("姓名")
    private String name;
    @ExcelProperty("部门")
    private String department;


    @ExcelProperty(value = "刷卡时间" ,converter = LocalDateTimeConverter.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime time;
    @ExcelProperty("设备编号")
    private String sn;
    @ExcelProperty("设备名称")
    private String dvName;

    @ExcelIgnore
    private String userId;
}
