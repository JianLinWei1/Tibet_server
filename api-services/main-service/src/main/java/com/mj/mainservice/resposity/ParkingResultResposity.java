package com.mj.mainservice.resposity;


import com.mj.mainservice.entitys.parking.ParkingResult;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by MrJan on 2020/10/30
 */

public interface ParkingResultResposity  extends MongoRepository<ParkingResult, String> {

}
