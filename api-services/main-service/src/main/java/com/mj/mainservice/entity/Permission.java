package com.mj.mainservice.entity;

import lombok.Data;

import java.util.List;

/**
 * Created by MrJan on 2020/11/4
 */
@Data
public class Permission {

    private  String id;
    private List<String> operation;
}
