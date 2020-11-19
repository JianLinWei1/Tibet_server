package com.mj.mainservice.entitys.parking;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jian.common.util.LocalDateTimeConverter;
import com.jian.common.util.PageHelper;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * Created by MrJan on 2020/10/30
 */

@Data
public class ParkingResult  extends PageHelper {
    @ExcelProperty("id")
    private String id;
    @ExcelProperty("设备序列号")
    private  String serialno;
    @ExcelProperty("设备名称")
    private  String deviceName;
    @ExcelProperty("ip地址")
    private  String ipaddr;
    @ExcelIgnore
    private  String plateid;
    @ExcelProperty("车牌")
    private  String license;
    @ExcelProperty("图片")
    private  String img;
    @ExcelProperty(value = "日期时间", converter = LocalDateTimeConverter.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
   private LocalDateTime time;
    @ExcelIgnore
    private String userId;
}
