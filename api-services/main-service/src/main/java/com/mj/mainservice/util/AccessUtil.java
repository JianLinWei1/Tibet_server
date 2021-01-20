package com.mj.mainservice.util;

import cn.hutool.core.lang.TypeReference;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jian.common.util.HttpUtil;
import com.jian.common.util.ResultUtil;
import com.jian.common.util.SysConfigUtil;
import com.mj.mainservice.entitys.access.Doors;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @auther JianLinWei
 * @date 2020-12-05 23:54
 */
@Log4j2
public class AccessUtil {

    /**
     * 将上传的数据解析从map<设备， 门号>
     *
     * @param dvIds
     * @return
     */
    public static List<Map<String, List<Doors>>> parseDoors(List<String> dvIds) {
        List<String> deviceId = new ArrayList<>();

        List<Map<String, List<Doors>>> maps = new ArrayList<>();
        dvIds.stream().forEach(d -> {
            String[] objs = d.split("-");
            if (objs.length >= 3) {
                Map<String, List<Doors>> map = new HashedMap<>();
                if (!deviceId.contains(objs[0])) {
                    deviceId.add(objs[0]);
                    List<Doors> doors = new ArrayList<>();
                    dvIds.stream().forEach(dr -> {
                        String[] objs1 = dr.split("-");
                        if (objs1.length >= 2) {
                            Doors doors1 = new Doors();
                            doors1.setId(Integer.valueOf(objs1[1]));
                            doors1.setName(objs1[2]);
                            if (!doors.contains(doors1) && StringUtils.equals(objs1[0] ,objs[0]))
                                doors.add(doors1);
                        }
                    });
                    map.put(objs[0], doors);
                }
                maps.add(map);
            }
        });
        return maps;
    }


    public static int getDoor(List<Integer> doorIds) {
        StringBuilder bin = new StringBuilder("0000");

        for (int i = 0; i < doorIds.size(); i++) {
            if (doorIds.get(i) == 1)
                bin.replace(3, 4, "1");
            if (doorIds.get(i) == 2)
                bin.replace(2, 3, "1");
            if (doorIds.get(i) == 3)
                bin.replace(1, 2, "1");
            if (doorIds.get(i) == 4)
                bin.replace(0, 1, "1");
        }
        String str = bin.toString();
        int doorId = Biannary2Decimal(Integer.valueOf(str));

        return doorId;
    }

    public static Integer Biannary2Decimal(int bi) {
        String binStr = bi + "";
        Integer sum = 0;
        int len = binStr.length();
        for (int i = 1; i <= len; i++) {
            //第i位 的数字为：
            int dt = Integer.parseInt(binStr.substring(i - 1, i));
            sum += (int) Math.pow(2, len - i) * dt;
        }
        return sum;
    }


    public  static List<JSONObject> getStatus(List<String>  ips) throws IOException {
        JSONArray array = new JSONArray();
        ips.stream().forEach(ip ->{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ip",ip);
            array.add(jsonObject);
        });
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("ips",array);
        log.info(jsonObject1.toString());
        String url = "http://" + SysConfigUtil.getIns().getProAccessServer() +
                "/getDoorsState";
        HttpUtil httpUtil = new HttpUtil();
        ResultUtil ru = httpUtil.post(url, jsonObject1.toString());


        if(ru.getCode() == 0){
            return (List<JSONObject>)ru.getData();

        }


        return null;
    }


}
