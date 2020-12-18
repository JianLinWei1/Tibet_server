package com.mj.mainservice.resposity.parking;


import com.mj.mainservice.entitys.parking.ParkInfo;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ParkingResposity   extends MongoRepository<ParkInfo, String> {

    Page<ParkInfo>  findAll(Example<ParkInfo> example , Query query , Pageable pageable);

    ParkInfo  findBySerialnoEquals(String serNo);

    List<ParkInfo>  findAllByUserIdEquals(String userId);
}
