package com.mj.mainservice.util.camera.entitys;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * Created by MrJan on 2020/12/14
 */

@Data
public class LoginInfo {

    @JSONField(name = "UserName")
    private  String  UserName;
    @JSONField(name = "AccessCode")
    private  String  AccessCode;
    @JSONField(name = "LoginSignature")
    private  String  LoginSignature;
    @JSONField(name = "Encryption")
    private  String  Encryption;
    @JSONField(name = "AccessToken")
    private  String  AccessToken;

}
