package com.mj.mainservice.vo.person;

import cn.hutool.core.annotation.Alias;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @auther JianLinWei
 * @date 2020-12-04 0:48
 */
@Data
public class ImportPersonVo {

    @Alias("人员编号")
    private  String  id ;

    @Alias("人员姓名")
    private String  name ;

    @Alias("部门名称")
    private String  department ;
}
