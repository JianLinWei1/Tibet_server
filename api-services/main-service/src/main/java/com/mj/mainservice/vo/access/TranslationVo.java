package com.mj.mainservice.vo.access;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mj.mainservice.entitys.access.Translation;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @auther JianLinWei
 * @date 2020-12-03 23:50
 */
@Data
public class TranslationVo  extends Translation {

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    List<LocalDateTime>  dates;
}
