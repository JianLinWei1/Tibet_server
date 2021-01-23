package com.mj.mainservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mj.mainservice.entitys.parking.ParkInfo;
import com.mj.mainservice.entitys.parking.ParkingUserInfo;
import com.mj.mainservice.resposity.parking.ParkingPersonResposity;
import com.mj.mainservice.resposity.parking.ParkingResposity;
import com.mj.mainservice.service.parking.ParkingDeviceService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * Created by MrJan on 2021/1/21
 */

@Service
@Log4j2
public class ParkingDeviceServiceImpl  implements ParkingDeviceService {
    @Resource
    private ParkingPersonResposity parkingPersonResposity;
    @Resource
    private ParkingResposity parkingResposity;

    @Override
    public ParkingUserInfo getUserInfoBySatus() {
        try {
          return  parkingPersonResposity.findFirstByStatusEquals(false);
        }catch (Exception e){
            log.error(e);
            return null;
        }

    }

    @Override
    public ParkInfo getParkInfoBySn(String sn) {
        try {
            Optional<ParkInfo>  parkInfo = parkingResposity.findById(sn);
            if(parkInfo.isPresent())
                return  parkInfo.get();
            return  null;
        }catch (Exception e){
            log.error(e);
            return null;
        }
    }

    @Override
    public boolean updateParkUserInfo(ParkingUserInfo parkingUserInfo) {
        try {
            parkingPersonResposity.save(parkingUserInfo);
            return  true;
        }catch (Exception e){
            log.error(e);
            return  false;
        }
    }
}
