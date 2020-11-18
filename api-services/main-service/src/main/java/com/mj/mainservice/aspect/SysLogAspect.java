package com.mj.mainservice.aspect;

import com.jian.authservice.annotation.SysLogInter;
import com.jian.authservice.entity.SysLog;
import com.jian.authservice.service.ISysLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * @auther JianLinWei
 * @date 2020-11-17 21:29
 */
@Aspect
@Component
public class SysLogAspect {
    @Autowired
    private ISysLogService sysLogService;


    @Pointcut("@annotation(com.jian.authservice.annotation.SysLogInter)")
    public void SysLg(){}

    @Before("SysLg()")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        SysLogInter sysLogInter = method.getAnnotation(SysLogInter.class);

        SysLog sysLog = new SysLog();
        sysLog.setAction(sysLogInter.value());
        sysLog.setTime(LocalDateTime.now());
        sysLog.setIp(request.getRemoteAddr());
        sysLog.setPath(request.getRequestURL().toString());
        sysLog.setUserName(request.getParameter("user_name"));
        sysLog.setUserId(request.getParameter("userId"));
        sysLogService.addSysLog(sysLog);

    }

}
