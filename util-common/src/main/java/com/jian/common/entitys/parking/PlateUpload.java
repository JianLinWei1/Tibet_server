package com.jian.common.entitys.parking;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by MrJan on 2020/10/30
 */

@Data
public class PlateUpload {
    @JsonProperty("AlarmInfoPlate")
    private AlarmInfoPlate alarmInfoPlate;
}
