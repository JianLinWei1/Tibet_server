package com.jian.common.util;

import com.jian.common.entity.SysConfig;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.Properties;

/**
 * Created by MrJan on 2020/10/20 19:37
 */

public class SysConfigUtil {
    private static SysConfigUtil sysConfigUtil;

    private File file = new File("upload" + File.separator + "SysConfig.properties");

    private SysConfigUtil() {

    }

    public static synchronized SysConfigUtil getIns() {
        if (sysConfigUtil != null)
            return sysConfigUtil;
        return new SysConfigUtil();
    }




    public void setPro(SysConfig sysConfig) throws IOException {
        Properties properties = new Properties();
        if (!file.exists())
            file.createNewFile();

        if (StringUtils.isNotEmpty(sysConfig.getAccessServer()))
            properties.setProperty("accessServer", sysConfig.getAccessServer());
        if(StringUtils.isNotEmpty(sysConfig.getUniServer()))
            properties.setProperty("uniServer", sysConfig.getUniServer());
        if(StringUtils.isNotEmpty(sysConfig.getAdmin()))
            properties.setProperty("admin", sysConfig.getAdmin());
        if(StringUtils.isNotEmpty(sysConfig.getPw()))
            properties.setProperty("pw", sysConfig.getPw());
         properties.store(new PrintStream(file) ,"utf-8");
         properties.load(new FileInputStream(file));


    }

    public String getProAccessServer() throws IOException {

        Properties properties = new Properties();

        properties.load(new FileInputStream(file));
        return  properties.getProperty("accessServer");


    }

    public String getProUniServer() throws IOException {

        Properties properties = new Properties();

        properties.load(new FileInputStream(file));
        return  properties.getProperty("uniServer");


    }
    public String getProUniAdmin() throws IOException {

        Properties properties = new Properties();

        properties.load(new FileInputStream(file));
        return  properties.getProperty("admin");


    }

    public String getProUniPw() throws IOException {

        Properties properties = new Properties();

        properties.load(new FileInputStream(file));
        return  properties.getProperty("pw");


    }


    public  SysConfig getSysConfig() throws IOException {
        Properties properties = new Properties();

        properties.load(new FileInputStream(file));

        SysConfig sysConfig = new SysConfig();
        sysConfig.setAccessServer(properties.getProperty("accessServer"));
        sysConfig.setUniServer(properties.getProperty("uniServer"));
        sysConfig.setAdmin(properties.getProperty("admin"));
        sysConfig.setPw(properties.getProperty("pw"));
        return  sysConfig;
    }

    public static void main(String[] args) throws IOException {
        SysConfig sysConfig = new SysConfig();
        sysConfig.setAccessServer("49.4.85.77:9999");
        SysConfigUtil.getIns().setPro(sysConfig);

        System.out.println(SysConfigUtil.getIns().getProAccessServer());
    }

}
