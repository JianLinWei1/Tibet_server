package com.jian.common.entity;

import lombok.Data;

/**
 * Created by MrJan on 2020/10/20 19:36
 */

@Data
public class SysConfig {
    private String accessServer;

    private String uniServer;

    private String admin;

    private String pw;

    private int uniPort;
}
