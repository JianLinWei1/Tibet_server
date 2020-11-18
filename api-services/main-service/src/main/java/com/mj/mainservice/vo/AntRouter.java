package com.mj.mainservice.vo;

import lombok.Data;

import java.util.List;

/***
 * @ClassName: AntRouter
 * @Description:
 * @Author: JianLinWei
 * @Date: 2020/9/21 10:42
 * @version : V1.0
 */
@Data
public class AntRouter {
    private  String router;
    private  String name ;
    private  String icon;
    private  String authority;
    List<AntRouter>  children;

}
