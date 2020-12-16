package com.mj.mainservice.controller.system;


import com.jian.common.util.ResultUtil;
import com.mj.mainservice.service.access.AccessService;
import com.mj.mainservice.service.parking.ParkingVoService;
import com.mj.mainservice.service.person.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MrJan on 2020/12/16
 */

@RestController
@RequestMapping("/index")
public class IndexController {
    @Autowired
    private PersonService personService;
    @Autowired
    private AccessService accessService;
    @Autowired
    private ParkingVoService parkingVoService;


    @GetMapping("/count")
    public ResultUtil  count(String userId){
        Map<String , Long>  map = new HashMap<>();
        map.put("pCount" , personService.countPersons(userId));
        map.put("aCount" , accessService.countAccess(userId));
        map.put("paCount", parkingVoService.countParking(userId));

        return new ResultUtil(0 , map , "");
    }


}
