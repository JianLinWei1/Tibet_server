package com.mj.mainservice.resposity;


import com.mj.mainservice.entitys.parking.ParkInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ParkingResposity   extends MongoRepository<ParkInfo, String> {
}
