package com.mj.mainservice.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jian.common.util.FileUtils;

import com.jian.common.util.MapCache;
import com.jian.common.util.ResultUtil;
import com.mj.mainservice.entitys.parking.*;
import com.mj.mainservice.resposity.parking.ParkingPersonResposity;
import com.mj.mainservice.resposity.parking.ParkingResposity;
import com.mj.mainservice.resposity.parking.ParkingResultResposity;
import com.mj.mainservice.service.parking.UploadRecoedService;
import com.mj.mainservice.util.parking.ParkingUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.Socket;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

/**
 * Created by MrJan on 2020/10/27
 */

@Service
@Log4j2
@Transactional
public class UploadRecordServiceImpl implements UploadRecoedService {
    @Resource
    private ParkingResposity parkingResposity;
    @Resource
    private ParkingPersonResposity parkingPersonResposity;
    @Resource
    private ParkingResultResposity parkingResultResposity;
    private MapCache mapCache = new MapCache();

    @Override
    public ParkingResponse heartBeat(ParkInfo parkInfo) {
        try {
//            ExampleMatcher exampleMatcher = ExampleMatcher.matching()
//                                            .withMatcher("serialno" ,)
            parkInfo.setUserId("1");
            //  log.info("车辆道闸接收心跳：{}",parkInfo);
            ParkInfo parkInfo1 = parkingResposity.findBySerialnoEquals(parkInfo.getSerialno());
            if (mapCache.get(parkInfo.getSerialno()) == null)
                mapCache.add(parkInfo.getSerialno(), 2, 30 * 1000);


            if (parkInfo != null && parkInfo1 == null)
                parkingResposity.save(parkInfo);

/*  去掉HTTP协议返回白名单
            ParkingResponse parkingResponse =  getAddWihteList(parkInfo.getSerialno());
            if(parkingResponse.getResponse_AlarmInfoPlate().getWhite_list_operate().getWhite_list_data().size()>0 ){
                log.info("心跳白名单增加操作：{}", parkingResponse);
                return  parkingResponse;
            }
            ParkingResponse parkingResponseDel =getDelWihteList(parkInfo.getSerialno());
            if(parkingResponseDel.getResponse_AlarmInfoPlate().getWhite_list_operate().getWhite_list_data().size()>0 ){
                log.info("心跳白名单删除操作：{}", parkingResponse);
                return  parkingResponseDel;
            }*/
            return null;
        } catch (Exception e) {
            log.error(e);
            return null;
        }

    }

    @Override
    public ParkingResponse getAddWihteList(String serialno) {

        WhiteListOperate operate = new WhiteListOperate();
        operate.setOperate_type(0);
        Page<ParkingUserInfo> page = parkingPersonResposity.findAllByStatusIsNotAndActionIsAndSerialnoIs(true, 0, serialno, PageRequest.of(0, 1));
        List<ParkingUserInfo> infos = page.getContent();
        List<WhiteListData> whiteListData1 = new ArrayList<>();
        infos.stream().forEach(info -> {
            info.getCarId().stream().forEach(car -> {
                WhiteListData whiteListData = new WhiteListData();
                whiteListData.setPlate(car);
                whiteListData.setEnable(info.getEnable());
                whiteListData.setNeed_alarm(info.getNeed_alarm());
                whiteListData.setEnable_time(info.getEnable_time());
                whiteListData.setOverdue_time(info.getOverdue_time());
                whiteListData1.add(whiteListData);
                info.setStatus(true);
                parkingPersonResposity.save(info);
            });

        });

        operate.setWhite_list_data(whiteListData1);
        ResponseAlarmInfoPlate responseAlarmInfoPlate = new ResponseAlarmInfoPlate();
        responseAlarmInfoPlate.setWhite_list_operate(operate);
        ParkingResponse parkingResponse = new ParkingResponse();
        parkingResponse.setResponse_AlarmInfoPlate(responseAlarmInfoPlate);
        //log.info("白名单返回{}", JSON.toJSONString(parkingResponse));
        return parkingResponse;
    }

    @Override
    public ParkingResponse getDelWihteList(String serialno) {
        WhiteListOperate operate = new WhiteListOperate();
        operate.setOperate_type(1);
        Page<ParkingUserInfo> page = parkingPersonResposity.findAllByStatusIsNotAndActionIsAndSerialnoIs(true, 1, serialno, PageRequest.of(0, 1));
        List<ParkingUserInfo> infos = page.getContent();
        List<WhiteListData> whiteListData1 = new ArrayList<>();
        infos.stream().forEach(info -> {
            info.getCarId().stream().forEach(car -> {
                WhiteListData whiteListData = new WhiteListData();
                whiteListData.setPlate(car);
                whiteListData.setEnable(info.getEnable());
                whiteListData.setNeed_alarm(info.getNeed_alarm());
                whiteListData.setEnable_time(info.getEnable_time());
                whiteListData.setOverdue_time(info.getOverdue_time());
                whiteListData1.add(whiteListData);
                info.setStatus(true);
                parkingPersonResposity.save(info);
            });

        });

        operate.setWhite_list_data(whiteListData1);
        ResponseAlarmInfoPlate responseAlarmInfoPlate = new ResponseAlarmInfoPlate();
        responseAlarmInfoPlate.setWhite_list_operate(operate);
        ParkingResponse parkingResponse = new ParkingResponse();
        parkingResponse.setResponse_AlarmInfoPlate(responseAlarmInfoPlate);
        return parkingResponse;
    }

    @Override
    public void plateUpload(PlateUpload plateUpload) {
        try {
            // log.info("车辆推送：{}", JSON.toJSONString(plateUpload));
            ParkingResult parkingResult = new ParkingResult();
            parkingResult.setSerialno(plateUpload.getAlarmInfoPlate().getSerialno());
            parkingResult.setDeviceName(plateUpload.getAlarmInfoPlate().getDeviceName());
            parkingResult.setIpaddr(plateUpload.getAlarmInfoPlate().getIpaddr());
            Optional<ParkInfo> optional = parkingResposity.findById(plateUpload.getAlarmInfoPlate().getSerialno());
            if (!optional.isPresent()) {
                log.error("当前不存在serilno:{},在服务器", plateUpload.getAlarmInfoPlate().getSerialno());
                return;
            }
            ParkInfo parkInfo = optional.get();
            parkingResult.setUserId(parkInfo.getUserId());
            parkingResult.setPlateid(plateUpload.getAlarmInfoPlate().getResult().getPlateResult().getPlateid());
            parkingResult.setLicense(plateUpload.getAlarmInfoPlate().getResult().getPlateResult().getLicense());
            long time = plateUpload.getAlarmInfoPlate().getResult().getPlateResult().getTimeStamp().getTimeval().getSec();
            parkingResult.setTime(LocalDateTime.ofInstant(Instant.ofEpochSecond(time), ZoneId.systemDefault()));
            String base64 = plateUpload.getAlarmInfoPlate().getResult().getPlateResult().getImageFile();
            Base64.Decoder decoder = Base64.getDecoder();
            if (StringUtils.isEmpty(base64)) {
                log.error("车辆图片为空");
            } else {
                String img = (String) FileUtils.getInstance().saveCarImg(decoder.decode(base64)).getData();
                parkingResult.setImg(img);
            }
            parkingResultResposity.save(parkingResult);


        } catch (Exception e) {
            log.error(e);
        }


    }

    @Override
    public Boolean getStatus(String sn) {
        if (mapCache.get(sn) != null)
            return true;
        return false;
    }

    @Override
    public ResultUtil repairTest() {
        try {
            List<String> errMsg = new ArrayList<>();
            List<String> sucMsg = new ArrayList<>();
            List<ParkingUserInfo> parkingUserInfos = parkingPersonResposity.findAllByActionIs(1);
            for (ParkingUserInfo parkingUserInfo : parkingUserInfos) {
                ParkInfo parkInfo = parkingResposity.findBySerialnoEquals(parkingUserInfo.getSerialno());
                try {
                    Socket socket = new Socket(parkInfo.getIpaddr(), 8131);
                    boolean tag = ParkingUtil.sendCmdPro(parkingUserInfo, socket, 0);
                    if (tag) {
                        sucMsg.addAll(parkingUserInfo.getCarId());
                        parkingPersonResposity.deleteById(parkingUserInfo.getId());
                    } else {
                        errMsg.add(parkingUserInfo.getCarId()+parkInfo.getDevice_name()+"连接设备删除失败");
                    }
                } catch (IOException e) {
                    log.error(e);
                    errMsg.add(parkInfo.getDevice_name() + "建立socket失败");

                }
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("成功消息", sucMsg);
            jsonObject.put("失败消息", errMsg);

            ResultUtil resultUtil = new ResultUtil();
            resultUtil.setCode(0);
            resultUtil.setData(jsonObject);
            resultUtil.setCount((long) sucMsg.size());
            return resultUtil;
        } catch (Exception e) {
            log.error(e);
            return new ResultUtil(-1, "修复车辆删除失败");
        }
    }
}
