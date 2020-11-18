package com.jian.common.entity;

import lombok.Data;

import java.util.List;

/***
 * @ClassName: ElTree
 * @Description:
 * @Author: JianLinWei
 * @Date: 2020/9/25 17:00
 * @version : V1.0
 */
@Data
public class ElTree {
private  String id ;
private  String label;
private List<ElTree>  children;
}
