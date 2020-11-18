package com.jian.common.entitys.parking;

import lombok.Data;

/**
 * Created by MrJan on 2020/10/27
 */


@Data
public class PlateResult {


    private Integer bright; //亮度评价（预留）

    private Integer carBright; //车身亮度（预留）

    private Integer carColor;  //车身颜色（预留）

    private  Integer colorType;  //车牌颜色 0：未知、1：蓝色、2：黄色、3：白色、4：黑色、5：绿色

    private  Integer colorValue;

    private  Integer  confidence ;

    private  Integer direction ;  //车的行进方向，0：未知，1：左，2：右，3：上， 4：下

    private String imagePath ;

    private  String imageFile; //大图片

    private String imageFragmentFile;//小图片

    private String plateid; //车牌ID

    private String license;

    private ParkingTimeStamp timeStamp;

}
