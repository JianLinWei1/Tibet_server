package com.mj.mainservice.vo.person;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jian.common.util.LocalDateTimeConverter;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.net.URL;
import java.time.LocalDateTime;

/**
 * Created by MrJan on 2020/11/20
 */

@Data
@ContentRowHeight(50)
@ColumnWidth(10)
public class PersonInfoVo {

    @ExcelProperty("人员编号")
    private  String  id ;

    @ExcelProperty("姓名")
    private String  name ;

    @ExcelProperty("部门")
    private String  department ;

    @ExcelProperty("身份证")
    private String  idCard;

    @ExcelProperty("门禁卡号")
    private String  accessId;

    @ExcelProperty("开门密码")
    private int  accessPw;

    @ExcelProperty("车牌号")
    private String carId;

    @ExcelProperty("电话号码")
    private String phone ;

    @ExcelIgnore
    private  String photo ;

    @ExcelProperty("人脸照片")
    private URL photoUrl;

    @ExcelIgnore
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime starTime;

    @ExcelIgnore
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime invalidTime ;


    @ExcelProperty(value = "注册时间", converter = LocalDateTimeConverter.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime createTime;

    @ExcelProperty("注册时间")
    private String  content;

    @ExcelIgnore
    private  byte[]  feature;

    @ExcelIgnore
    private  Integer role;

    @ExcelIgnore
    private  String userId;

    @ExcelIgnore
    private  String oid;
}
