package com.mj.mainservice.resposity.parking;


import com.mj.mainservice.entitys.parking.ParkingUserInfo;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;


/**
 * Created by MrJan on 2020/10/29
 */

public interface ParkingPersonResposity   extends MongoRepository<ParkingUserInfo, String> {


     Page<ParkingUserInfo>  findAll(Example<ParkingUserInfo> example , Query query , Pageable pageable);


     Page<ParkingUserInfo>   findAllByStatusIsNotAndActionIsAndSerialnoIs(boolean status , int action ,String serialno ,Pageable pageable);

     List<ParkingUserInfo>    findAllByPersonIdEquals(String pid);

     Optional<ParkingUserInfo>   findAllByPersonIdEqualsAndCarIdEquals(String personId, List<String> carId);

}
