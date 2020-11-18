package com.jian.common.entitys;


import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author JianLinWei
 * @since 2020-11-03
 */
@Data

public class SysLog implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 用户名
     */
    private String userName;

    /**
     * 请求地址
     */
    private String path;

    /**
     * 操作
     */
    private String action;

    /**
     * ip地址
     */
    private String ip;

    /**
     * 产生日志的用户id
     */
    private String userId;



}
