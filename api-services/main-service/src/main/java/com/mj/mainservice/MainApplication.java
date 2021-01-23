package com.mj.mainservice;

import com.softkey.jsyunew3;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;


/**
 * @auther JianLinWei
 * @date 2020-11-19 0:36
 */
@SpringBootApplication
@EnableEurekaClient
@MapperScan("com.mj.mainservice.mapper")
@EnableAsync
public class MainApplication {
    public   static ConfigurableApplicationContext context;
    public static void main(String[] args) {
       context = SpringApplication.run(MainApplication.class, args);
       /*try {
            String inPath = jsyunew3.FindPort(0);
            System.out.println("路径："+ inPath);
            if(StringUtils.isEmpty(inPath))
                context.close();
            String  str = jsyunew3.NewReadString(1 ,6 , "9DEF302D" ,"32738943" ,inPath);
            System.out.println("获取解密："+str);
            if(!StringUtils.equals(str, "123456"))
                context.close();
            if (jsyunew3.get_LastError() != 0)
                context.close();
            System.out.println("错误码："+ jsyunew3.get_LastError());
        }catch (UnsatisfiedLinkError  e){
            e.printStackTrace();
            System.out.println("关闭-close");
            context.close();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("关闭-close");
            context.close();
        }*/
    }
}
