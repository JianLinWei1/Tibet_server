package com.jian.common.entity;

import lombok.Data;

import java.util.List;

/**
 * @auther JianLinWei
 * @date 2020-10-04 21:48
 */
@Data
public class AntdTree {
    private  String title ;
    private  String  key ;
    private  String  value;
    private List<AntdTree> children;
}
