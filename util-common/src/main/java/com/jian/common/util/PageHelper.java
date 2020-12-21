package com.jian.common.util;

import com.alibaba.excel.annotation.ExcelIgnore;

import lombok.Data;
import org.springframework.data.annotation.Transient;


/**
 * Created by MrJan on 2020/10/7 15:21
 */

@Data
public class PageHelper  {

    @ExcelIgnore
    @Transient
    private  Integer  page ;
    @ExcelIgnore
    @Transient
    private Integer limit;



}
