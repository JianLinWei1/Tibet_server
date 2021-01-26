package com.mj.mainservice.util.parking;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.inject.internal.asm.$AnnotationVisitor;
import com.mj.mainservice.entitys.parking.ParkInfo;
import com.mj.mainservice.entitys.parking.ParkingUserInfo;
import com.mj.mainservice.service.parking.ParkingDeviceService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by MrJan on 2021/1/21
 * TCP下发白名单
 */

@Component
@Order(2)
@Log4j2
@Async
public class ParkingUtil implements CommandLineRunner {
    @Autowired
    private ParkingDeviceService parkingDeviceService;


    @Override
    public void run(String... args) throws Exception {
        while (true) {
            try {

                Thread.sleep(1000);
                System.out.println("***************开始获取*************");
                ParkingUserInfo parkingUserInfo = parkingDeviceService.getUserInfoBySatus();
                if (parkingUserInfo == null)
                    continue;
                //获取设备信息
                ParkInfo parkInfo = parkingDeviceService.getParkInfoBySn(parkingUserInfo.getSerialno());
                Socket socket = new Socket(parkInfo.getIpaddr(), 8131);

                System.out.println("***************获取到" + JSON.toJSONString(parkingUserInfo) + "*************");
                boolean tag = sendCmdPro(parkingUserInfo, socket, 1);
                if (tag) {
                    parkingUserInfo.setStatus(true);
                    parkingDeviceService.updateParkUserInfo(parkingUserInfo);
                }


            } catch (Exception e) {
                log.error("tcp线程{}", e);
            }

        }

    }


    /**
     * 生成增加白名单cmd命令
     *
     * @return
     */
    public static String generaCmd(ParkingUserInfo parkingUserInfo, String plate) {
        JSONObject dldb_rec = new JSONObject();
        dldb_rec.put("create_time", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
        if (parkingUserInfo.getEnable_time() != null)
            dldb_rec.put("enable_time", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(parkingUserInfo.getEnable_time()));
        if (parkingUserInfo.getOverdue_time() != null)
            dldb_rec.put("overdue_time", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(parkingUserInfo.getOverdue_time()));
        dldb_rec.put("plate", plate);
        dldb_rec.put("enable", parkingUserInfo.getEnable());
        dldb_rec.put("need_alarm", parkingUserInfo.getNeed_alarm());

        JSONObject cmd = new JSONObject();
        cmd.put("cmd", "white_list_operator");
        cmd.put("operator_type", "update_or_add");
        cmd.put("dldb_rec", dldb_rec);
        return cmd.toJSONString();

    }

    /**
     * 删除
     *
     * @param parkingUserInfo
     * @return
     */
    public static String delCmd(ParkingUserInfo parkingUserInfo, String plate) {

        JSONObject cmd = new JSONObject();
        cmd.put("cmd", "white_list_operator");
        cmd.put("operator_type", "delete");
        cmd.put("plate", plate);
        return cmd.toJSONString();
    }


    /**
     * 集成业务发送
     *
     * @param parkingUserInfo action  1 增加 0 删除
     * @return
     */
    public static boolean sendCmdPro(ParkingUserInfo parkingUserInfo, Socket socket, int action) {
        //多个车牌
        List<String> cmds = new ArrayList<>();

        if (parkingUserInfo.getCarId() != null && parkingUserInfo.getCarId().size() > 0) {
            parkingUserInfo.getCarId().stream().forEach(obj -> {
                String cmd = null;
                if (action == 1)
                    cmd = generaCmd(parkingUserInfo, obj);
                else
                    cmd = delCmd(parkingUserInfo, obj);
                cmds.add(cmd);
            });
        }
        //下发  记录下发数量
        AtomicInteger count = new AtomicInteger();
        cmds.stream().forEach(cmd -> {
            log.info("下发命令{}", cmd);
            try {
                Boolean status = sendCmd(socket, new String(cmd.getBytes("gb2312"), "gb2312"));
                if (status) {
                    count.getAndIncrement();
                }
            } catch (UnsupportedEncodingException e) {
                log.error(e);
            }
        });
        //当下发成功数量等于命令的长度 则这个人的车牌全部下发成功 改变为已被设备获取
        if (count.get() == cmds.size()) {
            log.info("发送完成");
            return true;
        }
        return false;

    }


    public static boolean sendCmd(Socket socket, String cmd) {
        try {
            int len = cmd.getBytes().length;
            byte[] header = {'V', 'Z', 0, 0, 0, 0, 0, 0};
            header[4] += (byte) ((len >> 24) & 0xFF);
            header[5] += (byte) ((len >> 16) & 0xFF);
            header[6] += (byte) ((len >> 8) & 0xFF);
            header[7] += (byte) (len & 0xFF);

            OutputStream out = socket.getOutputStream();
            out.write(header);
            out.write(cmd.getBytes());
            //接收消息

            int sn_len = recvPacketSize(socket);
            String res = "";
            if (sn_len > 0) {
                //接收实际数据
                byte[] data = new byte[sn_len];
                int recvLen = recvBlock(socket, data, sn_len);

                res = new String(data, 0, recvLen);
            }

            log.info("tcp返回{}", res);
            if (StringUtils.isEmpty(res))
                return false;
            JSONObject resJson = JSON.parseObject(res);
            if (resJson.getInteger("state_code") == 200)
                return true;
            else
                return false;
        } catch (Exception e) {
            log.error("Error:" + e);
            return false;
        }
    }

    public static int recvPacketSize(Socket socket) {
        byte[] header = new byte[8];
        int recvLen = recvBlock(socket, header, 8);
        if (recvLen <= 0) {
            return -1;
        }

        if (header[0] != 'V' || header[1] != 'Z') {
            //格式不对
            return -1;
        }

        if (header[2] == 1) {
            //心跳包
            return 0;
        }

        return convBytesToInt(header, 4);
    }

    //接收指定长度的数据，收完为止
    public static int recvBlock(Socket socket, byte[] buff, int len) {
        try {
            InputStream in = socket.getInputStream();
            int totleRecvLen = 0;
            int recvLen;
            while (totleRecvLen < len) {
                recvLen = in.read(buff, totleRecvLen, len - totleRecvLen);
                totleRecvLen += recvLen;
            }
            return len;
        } catch (Exception e) {
            log.error("recvBlock timeout!", e);
            // System.out.println("Error:"+e);
            return -1;
        }
    }


    public static int convBytesToInt(byte[] buff, int offset) {
        //4bytes 转为int，要考虑机器的大小端问题
        int len, byteValue;
        len = 0;
        byteValue = (0x000000FF & ((int) buff[offset]));
        len += byteValue << 24;
        byteValue = (0x000000FF & ((int) buff[offset + 1]));
        len += byteValue << 16;
        byteValue = (0x000000FF & ((int) buff[offset + 2]));
        len += byteValue << 8;
        byteValue = (0x000000FF & ((int) buff[offset + 3]));
        len += byteValue;
        return len;
    }


}
