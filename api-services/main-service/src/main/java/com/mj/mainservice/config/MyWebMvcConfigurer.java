package com.mj.mainservice.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;



import java.io.IOException;

/**
 * 将接受的“” 转为null
 * @auther JianLinWei
 * @date 2020-11-24 21:03
 */
@Configuration
public class MyWebMvcConfigurer {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return new Jackson2ObjectMapperBuilderCustomizer() {
            @Override
            public void customize(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
                jacksonObjectMapperBuilder.deserializerByType(String.class, new StringDeserializer() {
                    @Override
                    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                        String deserialize = super.deserialize(p, ctxt);
                        if (deserialize != null && deserialize.equals("")) {
                            return null;
                        }
                        return deserialize;
                    }
                });
            }
        };
    }

}
