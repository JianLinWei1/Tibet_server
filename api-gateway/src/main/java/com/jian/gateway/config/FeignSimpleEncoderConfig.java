package com.jian.gateway.config;

import feign.codec.Encoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



/**
 * Created by MrJan on 2020/11/3
 */

@Configuration
public class FeignSimpleEncoderConfig {
    @Bean
    public Encoder encoder(){
        return new FormEncoder();
    }
}
