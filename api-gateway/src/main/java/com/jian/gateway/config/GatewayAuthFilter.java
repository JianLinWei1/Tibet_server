package com.jian.gateway.config;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.exceptions.TokenExpiredException;

import com.jian.common.util.JwtUtil;
import com.jian.common.util.ResultUtil;
import com.jian.gateway.entity.SysLog;
import com.jian.gateway.service.SysLogFeign;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @auther JianLinWei
 * @date 2020-03-23 9:56
 */
@Component
@Log4j2
public class GatewayAuthFilter implements GlobalFilter, Ordered {

    @Autowired
     private SysLogFeign sysLogFeign;

    @Value("${jwt.skipUrls}")
    private String[] skipUrls;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

       final boolean skip = false;

        String url = exchange.getRequest().getURI().getPath();

        for(String u : skipUrls){
            if(StringUtils.equals(u, url) || StringUtils.contains(url , u)){
                log.info("请求路径{}跳过鉴权" , url);
                return chain.filter(exchange);
            }
        }



        //不拦截swagger
        String str = url.substring(url.indexOf("/", 1));
        if (StringUtils.equals(str, "/v2/api-docs")) {
            return chain.filter(exchange);
        }
        /* HttpResultImpl httpResult = authFeign.login();*/
        String token = exchange.getRequest().getHeaders().getFirst("token");
        if (StringUtils.isEmpty(token)) {
            log.info("********Token  is NULL********");
            String res = JSON.toJSONString(new ResultUtil(-1001, "No Token"));

            exchange.getResponse().getHeaders().add("Content-Type", ContentType.APPLICATION_JSON.getMimeType());
            return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(res.getBytes())));
        }

        try {
            Map<String, String> map = (Map<String, String>) JwtUtil.getInstance().checkJWT(token);
            MultiValueMap<String , Object> valueMap = new LinkedMultiValueMap<>();
            valueMap.add("username" , map.get("username"));
            valueMap.add("userId", map.get("userId"));
            valueMap.add("childs", map.get("childs"));

            URI uri = new URIBuilder(exchange.getRequest().getURI().toString()).addParameter("user_name" , map.get("username"))
                    .addParameter("userId" , map.get("userId")).build();

            ServerHttpRequest httpRequest = exchange.getRequest().mutate().uri(uri).build();
            log.info(exchange.getRequest().getURI());

           /* SysLog sysLog = new SysLog();
            sysLog.setIp(exchange.getRequest().getRemoteAddress().getHostString());
            sysLog.setPath(exchange.getRequest().getPath().value());
            sysLog.setUserName(map.get("username"));
            sysLog.setUserId(map.get("userId"));
            sysLogFeign.addSysLog(sysLog);*/
            log.info("Token:{}" ,token);
            return  chain.filter(exchange.mutate().request(httpRequest).build());



        } catch (TokenExpiredException e) {
            log.error(e.getMessage());
            String res = JSON.toJSONString(new ResultUtil(-1002, "Token Expired"));

            exchange.getResponse().getHeaders().add("Content-Type", ContentType.APPLICATION_JSON.getMimeType());
            return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(res.getBytes())));
        }catch (Exception e){
            log.info(e);
            String res = JSON.toJSONString(new ResultUtil(-1003, "Service Token Error"));

             exchange.getResponse().getHeaders().add("Content-Type", ContentType.APPLICATION_JSON.getMimeType());
            return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(res.getBytes())));
        }

        // return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }


}
