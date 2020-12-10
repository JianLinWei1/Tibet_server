package com.mj.mainservice.vo.access.newservice;

import lombok.Data;

/**
 * @auther JianLinWei
 * @date 2020-12-11 1:22
 */
@Data
public class UserDataVo {
    private String CardNo;
    private String pin;
    private String pw ;
    private String doorId;
    private Boolean flag;
}
