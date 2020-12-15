package com.jian.common.util;

import com.alibaba.fastjson.JSON;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by MrJan on 2020/10/15 18:10
 */

@Log4j2
public class HttpUtil {
    public   ResultUtil get(String url){
        ResultUtil resultUtil = new ResultUtil();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet hp = new HttpGet(url);

        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(60 * 1000)
                .setConnectionRequestTimeout(60 * 1000).setSocketTimeout(60 * 1000).setRedirectsEnabled(true)
                .build();

        hp.setConfig(requestConfig);
        hp.setHeader("Content-Type", "application/json");


        try {
            CloseableHttpResponse response = httpClient.execute(hp);
            StatusLine statusline = response.getStatusLine();
            int responsecode = statusline.getStatusCode();
            if(responsecode == 200){
                HttpEntity he = response.getEntity();
                resultUtil =JSON.parseObject(EntityUtils.toString(he, "UTF-8"),ResultUtil.class);

            }else{
                return  new ResultUtil(-1 ,EntityUtils.toString(response.getEntity())) ;
            }

            response.close();
            httpClient.close();
            return resultUtil ;
        } catch (ClientProtocolException e) {
            log.error(e);
            return new ResultUtil(-1 ,null ,e.getMessage());
        } catch (IOException e) {
            log.error(e);
            return new ResultUtil(-1 ,null ,e.getMessage());
        }


    }


    public  ResultUtil  post(String  url , String strPost)  {

        ResultUtil resultUtil = new ResultUtil();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost hp = new HttpPost(url);

        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(590 * 1000)
                .setConnectionRequestTimeout(590 * 1000).setSocketTimeout(590 * 1000).setRedirectsEnabled(true)
                .build();

        hp.setConfig(requestConfig);
        hp.setHeader("Content-Type", "application/json");

        hp.setEntity(new StringEntity(strPost, Charset.forName("UTF-8")));
        try {
            CloseableHttpResponse  response = httpClient.execute(hp);
            StatusLine statusline = response.getStatusLine();
            int responsecode = statusline.getStatusCode();
            if(responsecode == 200){
                HttpEntity he = response.getEntity();
                String res = EntityUtils.toString(he, "UTF-8");
                log.info("请求返回：" + res);
                resultUtil = JSON.parseObject(res,ResultUtil.class);

            }else{
                return  new ResultUtil(-1 ,EntityUtils.toString(response.getEntity())) ;
            }

            response.close();
            httpClient.close();
            return resultUtil ;

        } catch (ClientProtocolException e) {
            log.error(e);
            return new ResultUtil(-1 ,null ,e.getMessage());
        } catch (IOException e) {
            log.error(e);
            return new ResultUtil(-1 ,null ,e.getMessage());
        }

    }


}
