package com.mj.mainservice.resposity.parking;


import com.mj.mainservice.entitys.parking.ParkingResult;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by MrJan on 2020/10/30
 */

public interface ParkingResultResposity  extends MongoRepository<ParkingResult, String> {

    Page<ParkingResult>  findAll(Example<ParkingResult> example , Query query , Pageable pageable);

}
