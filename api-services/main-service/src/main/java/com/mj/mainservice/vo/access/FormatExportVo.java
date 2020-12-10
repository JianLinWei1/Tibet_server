package com.mj.mainservice.vo.access;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jian.common.util.LocalDateTimeConverter;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @auther JianLinWei
 * @date 2020-12-10 23:31
 */
@Data
@ColumnWidth(20)
public class FormatExportVo {



    @ExcelProperty(value = {"刷卡记录登记表","记录登记时间"} ,converter = LocalDateTimeConverter.class)
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
    @JsonFormat(pattern="yyyy/MM/dd HH:mm:ss",timezone = "GMT+8")
    private  LocalDateTime  checkInTime;

    @ExcelProperty({"刷卡记录登记表","明细表" ,"卡号"})
    private  String   cardNo;

    @ExcelProperty(value = {"刷卡记录登记表","明细表" ,"时间"} , converter = LocalDateTimeConverter.class)
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
    @JsonFormat(pattern="yyyy/MM/dd HH:mm:ss",timezone = "GMT+8")
    private  LocalDateTime time;
}
