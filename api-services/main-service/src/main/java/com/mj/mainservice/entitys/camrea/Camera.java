package com.mj.mainservice.entitys.camrea;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jian.common.util.LocalDateTimeConverter;
import com.jian.common.util.PageHelper;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @auther JianLinWei
 * @date 2020-12-20 16:06
 */
@Data
@Document(collection = "Camera")
public class Camera  extends PageHelper {
    @Id
    private String cameraCode;
    private String cameraName;
    private String ip;
    private String orgName;
    @ExcelProperty(value = "日期时间", converter = LocalDateTimeConverter.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @Indexed
    private LocalDateTime time;

    private String userId;

    private CameraBind cameraBind;
}
