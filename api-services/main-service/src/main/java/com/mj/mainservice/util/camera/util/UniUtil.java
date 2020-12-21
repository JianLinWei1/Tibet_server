package com.mj.mainservice.util.camera.util;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;


import com.alibaba.fastjson.JSON;
import com.jian.common.util.SysConfigUtil;
import com.mj.mainservice.util.camera.entitys.Condition;
import com.mj.mainservice.util.camera.entitys.LoginInfo;
import com.mj.mainservice.util.camera.entitys.QueryCondition;
import lombok.extern.log4j.Log4j2;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;

import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Created by MrJan on 2020/12/14
 */

@Component
@Log4j2
public class UniUtil {
    private String loginUrl = "/VIID/login";
    private String  queryResUrl ="/VIID/query";
    private String  queryCamUrl = "/VIID/dev/ec/query/camera";

    public String  login() throws IOException {
        String url = "http://" + SysConfigUtil.getIns().getProUniServer() + loginUrl;
        String admin = SysConfigUtil.getIns().getProUniAdmin();
        String pw = SysConfigUtil.getIns().getProUniPw();
        LoginInfo  loginInfo = new LoginInfo();
        //第一次请求
        HttpPost hp = new HttpPost(url);
        CloseableHttpResponse response = HttpClients.createDefault().execute(hp);
        if (response.getStatusLine().getStatusCode() == 200){
            String res = EntityUtils.toString(response.getEntity());
            log.info("第一次请求登录结果：{}", res);
            loginInfo = JSON.parseObject(res, LoginInfo.class);
            loginInfo.setUserName(admin);
            String sign = SecureUtil.md5( Base64.getEncoder().encodeToString(admin.getBytes()) + loginInfo.getAccessCode()+ SecureUtil.md5(pw));
            loginInfo.setLoginSignature(sign);
            String token = HttpUtil.post(url ,JSON.toJSONString(loginInfo));
            log.info("第二次登录请求：{} ,返回：{}" , JSON.toJSONString(loginInfo),token);
            loginInfo = JSON.parseObject(token ,LoginInfo.class);

            return  loginInfo.getAccessToken();
        }

        return null;

    }


    public  String getResInfo(String  token) throws IOException {
        QueryCondition queryCondition = new QueryCondition();
        queryCondition.setItemNum(2);
        queryCondition.setPageFirstRowNumber(0);
        queryCondition.setPageRowNum(200);
        queryCondition.setQueryCount(1);
        List<Condition> conditions = new ArrayList<>();
        Condition condition = new Condition();
        condition.setLogicFlag(0);
        condition.setQueryData("1001");
        condition.setQueryType(256);
        conditions.add(condition);
        Condition condition1 = new Condition();
        condition1.setQueryType(257);
        condition1.setLogicFlag(0);
        condition1.setQueryData("1");
        conditions.add(condition1);
        queryCondition.setCondition(conditions);
        String  condition_enc = URLEncoder.encode(JSON.toJSONString(queryCondition) ,"utf-8");
        String url = "http://" + SysConfigUtil.getIns().getProUniServer() + queryResUrl+"?condition="+condition_enc;
        String res = HttpRequest.get(url).header("Authorization",token).execute().body();
        System.out.println(res);
        return "";
    }
}
