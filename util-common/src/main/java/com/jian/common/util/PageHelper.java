package com.jian.common.util;

import com.alibaba.excel.annotation.ExcelIgnore;

import lombok.Data;


/**
 * Created by MrJan on 2020/10/7 15:21
 */

@Data
public class PageHelper  {

    @ExcelIgnore
    private  Integer  page ;
    @ExcelIgnore
    private Integer limit;



}
