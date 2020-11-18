package com.jian.common.entitys.parking;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * Created by MrJan on 2020/10/27
 */

@Data
public class WhiteListData {
    private  String plate;//车牌（GB2312）
    private  Integer enable;//当前名单是否有效（0：无效， 1,：有效）
    private  Integer need_alarm;//当前名单是否为黑名单（0：否，1：黑名单）

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime enable_time;//当前名单生效时间，如：2018-01-01 11:11:11
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private  LocalDateTime overdue_time;//当前名单过期时间，如：2018-01-01 11:11:11

}
