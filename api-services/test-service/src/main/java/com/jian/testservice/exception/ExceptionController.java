package com.jian.testservice.exception;

import com.jian.common.util.ResultUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/***
 * @ClassName: ExceptionController
 * @Description:
 * @Author: JianLinWei
 * @Date: 2020/8/28 11:28
 * @version : V1.0
 */
@ControllerAdvice
@Log4j2
public class ExceptionController {


    @ResponseBody
    @ExceptionHandler(value =  Exception.class)
    public ResultUtil exception(Exception ex){
   log.error(ex.getMessage());

     return new ResultUtil(-500 , ex.getMessage());

    }
}
