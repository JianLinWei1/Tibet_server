package com.mj.mainservice.resposity;

import com.jian.common.entitys.parking.ParkInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ParkingResposity   extends MongoRepository<ParkInfo , String> {
}
