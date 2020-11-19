package com.mj.mainservice.entitys.parking;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by MrJan on 2020/10/28
 */

@Data
public class ParkingResponse {
    @JsonProperty("Response_AlarmInfoPlate")
    private ResponseAlarmInfoPlate Response_AlarmInfoPlate;

}
