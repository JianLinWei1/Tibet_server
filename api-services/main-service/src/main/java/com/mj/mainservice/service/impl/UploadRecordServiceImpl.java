package com.mj.mainservice.service.impl;


import com.jian.common.util.FileUtils;

import com.mj.mainservice.entitys.parking.*;
import com.mj.mainservice.resposity.ParkingPersonResposity;
import com.mj.mainservice.resposity.ParkingResposity;
import com.mj.mainservice.resposity.ParkingResultResposity;
import com.mj.mainservice.service.UploadRecoedService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Created by MrJan on 2020/10/27
 */

@Service
@Log4j2
public class UploadRecordServiceImpl implements UploadRecoedService {
    @Resource
    private ParkingResposity parkingResposity;
    @Resource
    private ParkingPersonResposity parkingPersonResposity;
    @Resource
    private ParkingResultResposity parkingResultResposity;

    @Override
    public ParkingResponse heartBeat(ParkInfo parkInfo) {
        try {
//            ExampleMatcher exampleMatcher = ExampleMatcher.matching()
//                                            .withMatcher("serialno" ,)
            log.info("车辆道闸接收心跳：{}",parkInfo);
            if(parkInfo != null)
                parkingResposity.save(parkInfo);


            ParkingResponse parkingResponse =  getAddWihteList(parkInfo.getSerialno());
            if(parkingResponse.getResponse_AlarmInfoPlate().getWhite_list_operate().getWhite_list_data().size()>0 ){
                log.info("心跳白名单增加操作：{}", parkingResponse);
                return  parkingResponse;
            }
            ParkingResponse parkingResponseDel =getDelWihteList(parkInfo.getSerialno());
            if(parkingResponseDel.getResponse_AlarmInfoPlate().getWhite_list_operate().getWhite_list_data().size()>0 ){
                log.info("心跳白名单删除操作：{}", parkingResponse);
                return  parkingResponseDel;
            }
           return null;
        }catch (Exception e){
            log.error(e);
            return null;
        }

    }

    @Override
    public ParkingResponse getAddWihteList(String serialno) {

        WhiteListOperate operate=  new WhiteListOperate();
        operate.setOperate_type(0);
        Page<ParkingUserInfo>  page = parkingPersonResposity.findAllByStatusIsNotAndActionIsAndSerialnoIs(true , 0,serialno , PageRequest.of(0 ,5));
        List<ParkingUserInfo> infos = page.getContent();
        List<WhiteListData> whiteListData1 = new ArrayList<>();
        infos.stream().forEach(info ->{
            WhiteListData whiteListData = new WhiteListData();
            whiteListData.setPlate(info.getCarId());
            whiteListData.setEnable(info.getEnable());
            whiteListData.setNeed_alarm(info.getNeed_alarm());
            whiteListData.setEnable_time(info.getEnable_time());
            whiteListData.setOverdue_time(info.getOverdue_time());
            whiteListData1.add(whiteListData);
            info.setStatus(true);
            parkingPersonResposity.save(info);
        });

        operate.setWhite_list_data(whiteListData1);
        ResponseAlarmInfoPlate responseAlarmInfoPlate =new ResponseAlarmInfoPlate();
        responseAlarmInfoPlate.setWhite_list_operate(operate);
        ParkingResponse parkingResponse = new ParkingResponse();
        parkingResponse.setResponse_AlarmInfoPlate(responseAlarmInfoPlate);
        return parkingResponse;
    }

    @Override
    public ParkingResponse getDelWihteList(String serialno) {
        WhiteListOperate operate=  new WhiteListOperate();
        operate.setOperate_type(1);
        Page<ParkingUserInfo>  page = parkingPersonResposity.findAllByStatusIsNotAndActionIsAndSerialnoIs(true , 1,serialno , PageRequest.of(0 ,5));
        List<ParkingUserInfo> infos = page.getContent();
        List<WhiteListData> whiteListData1 = new ArrayList<>();
        infos.stream().forEach(info ->{
            WhiteListData whiteListData = new WhiteListData();
            whiteListData.setPlate(info.getCarId());
            whiteListData.setEnable(info.getEnable());
            whiteListData.setNeed_alarm(info.getNeed_alarm());
            whiteListData.setEnable_time(info.getEnable_time());
            whiteListData.setOverdue_time(info.getOverdue_time());
            whiteListData1.add(whiteListData);
            info.setStatus(true);
            parkingPersonResposity.save(info);
        });

        operate.setWhite_list_data(whiteListData1);
        ResponseAlarmInfoPlate responseAlarmInfoPlate =new ResponseAlarmInfoPlate();
        responseAlarmInfoPlate.setWhite_list_operate(operate);
        ParkingResponse parkingResponse = new ParkingResponse();
        parkingResponse.setResponse_AlarmInfoPlate(responseAlarmInfoPlate);
        return parkingResponse;
    }

    @Override
    public void plateUpload(PlateUpload plateUpload) {
        try {
            log.info("车辆推送：{}{}", plateUpload.getAlarmInfoPlate().getSerialno(),plateUpload.getAlarmInfoPlate().getDeviceName());
            ParkingResult  parkingResult = new ParkingResult();
            parkingResult.setSerialno(plateUpload.getAlarmInfoPlate().getSerialno());
            parkingResult.setDeviceName(plateUpload.getAlarmInfoPlate().getDeviceName());
            parkingResult.setIpaddr(plateUpload.getAlarmInfoPlate().getIpaddr());
            ParkInfo parkInfo = parkingResposity.findById(plateUpload.getAlarmInfoPlate().getSerialno()).get();
            parkingResult.setUserId(parkInfo.getUserId());
            parkingResult.setPlateid(plateUpload.getAlarmInfoPlate().getResult().getPlateResult().getPlateid());
            parkingResult.setLicense(plateUpload.getAlarmInfoPlate().getResult().getPlateResult().getLicense());
            long time = plateUpload.getAlarmInfoPlate().getResult().getPlateResult().getTimeStamp().getTimeval().getSec();
            parkingResult.setTime(LocalDateTime.ofInstant(Instant.ofEpochSecond(time) , ZoneId.systemDefault()));
            String base64 = plateUpload.getAlarmInfoPlate().getResult().getPlateResult().getImageFile();
            Base64.Decoder decoder = Base64.getDecoder();
            String img = (String) FileUtils.getInstance().saveCarImg(decoder.decode(base64)).getData();
            parkingResult.setImg(img);
            parkingResultResposity.save(parkingResult);



        }catch (Exception e){
          log.error(e);
        }


    }
}
